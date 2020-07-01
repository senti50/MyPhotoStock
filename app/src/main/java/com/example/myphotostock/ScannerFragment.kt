package com.example.myphotostock

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_scanner.*


class ScannerFragment : Fragment() {

    private lateinit var listOfScannerList: List<ScannerList>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        listOfScannerList = getScannerListsFromDatabase()

        return inflater.inflate(R.layout.fragment_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle(resources.getString(R.string.scanner_database))
        setupListView()

    }

    private fun getScannerListsFromDatabase(): List<ScannerList> {
        //TODO: Pobieranie listy list ze skanera z firebase
        val listOfScanLists: List<ScannerList> = listOf(ScannerList("1", "Lista skaner 1"), ScannerList("2", "Lista skaner 2"), ScannerList("3", "Lista skaner 3"))
        return  listOfScanLists
    }

    private fun setupListView() {

        val scannerListAdapter: ArrayAdapter<String> = ArrayAdapter(activity!!, R.layout.cell_one_title_item, R.id.TV_Title, listOfScannerList.map { it.name } )

        LV_scanner_lists.adapter = scannerListAdapter

        LV_scanner_lists.setOnItemClickListener {
            parent, view, position, id ->
            val intent = Intent(activity, ScannerListOfRecordsActivity::class.java)
            val item = listOfScannerList[id.toInt()]
            intent.putExtra("nameListScanner", item.name)
            intent.putExtra("idListScanner", item.id)
            startActivity(intent)
        }

    }

}