package com.example.myphotostock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.Window.FEATURE_ACTION_BAR
import android.widget.ArrayAdapter
import androidx.core.app.NavUtils
import kotlinx.android.synthetic.main.activity_scanner_list_of_records.*
import kotlinx.android.synthetic.main.fragment_scanner.*

class ScannerListOfRecordsActivity : AppCompatActivity() {

    private lateinit var listOfRecords: List<ScannerRecord>
    private lateinit var idOfList: String
    private lateinit var nameOfList: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idOfList = intent.getStringExtra("idListScanner")
        nameOfList = intent.getStringExtra("nameListScanner")

        listOfRecords = getScannerRecordsFromDatabase(idOfList)

        setContentView(R.layout.activity_scanner_list_of_records)
        setTitle(nameOfList)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupListView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this)
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun getScannerRecordsFromDatabase(listId: String): List<ScannerRecord> {
        //TODO: Pobieranie konkretnej listy skanera z firebase
        val listOfScanRecLists: List<ScannerRecord> = listOf(ScannerRecord("Pierwszy bardzo bardzo bardzo bardzo bardzo długi tekst", "1"), ScannerRecord("Drugi bardzo bardzo bardzo bardzo bardzo długi tekst", "1"), ScannerRecord("Trzeci bardzo bardzo bardzo bardzo bardzo długi tekst", "1"))
        return  listOfScanRecLists
    }

    private fun setupListView() {

        val scannerListAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.cell_one_title_item, R.id.TV_Title, listOfRecords.map { it.content } )

        LV_scanner_records.adapter = scannerListAdapter

    }

}