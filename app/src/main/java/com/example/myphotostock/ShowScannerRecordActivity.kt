package com.example.myphotostock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.NavUtils
import kotlinx.android.synthetic.main.activity_show_scanner_record.*

class ShowScannerRecordActivity : AppCompatActivity() {

    private lateinit var idRecord: String
    private lateinit var contextRecord: String
    private lateinit var idOfList: String
    private lateinit var nameOfList: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idRecord = intent.getStringExtra("idRecord")
        contextRecord = intent.getStringExtra("contextRecord")

        idOfList = intent.getStringExtra("idList")
        nameOfList = intent.getStringExtra("nameList")

        setContentView(R.layout.activity_show_scanner_record)

        supportActionBar?.setTitle("")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editTextTextMultiLine.setText(contextRecord)

        fab_save.setOnClickListener { view ->
            saveRecord()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            val intent = Intent(this, ScannerListOfRecordsActivity::class.java)
            intent.putExtra("nameListScanner", nameOfList)
            intent.putExtra("idListScanner", idOfList)
            startActivity(intent)
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun saveRecord() {
        //TODO: zrobić zapisywanie wpisu skanera
    }

}