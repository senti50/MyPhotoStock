package com.example.myphotostock

import android.content.Context
import android.content.DialogInterface.OnShowListener
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.baoyz.swipemenulistview.SwipeMenu
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.baoyz.swipemenulistview.SwipeMenuListView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.alert_input_text.view.*
import kotlinx.android.synthetic.main.fragment_albums.*
import kotlinx.android.synthetic.main.fragment_albums.view.*
import java.util.*


class AlbumsFragment : Fragment() {

    private lateinit var listOfAlbums: MutableList<PhotoAlbum>
    private lateinit var refDbPhotoAlbum: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.show()
        activity?.setTitle(resources.getString(R.string.albums))

        refDbPhotoAlbum = (activity as MainActivity).refDatabase.child("photoAlbum")
        refDbPhotoAlbum.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(databaseSnapshot: DataSnapshot) {
                listOfAlbums = mutableListOf()

                for(i in databaseSnapshot.children){
                    val title: String = i.getValue(String::class.java) ?: ""
                    val newRow = PhotoAlbum(title, i.ref.key.toString())
                    listOfAlbums.add(newRow)
                }

                setupListView()
            }

        })

        fab_add.setOnClickListener {
            showAlertAddNewAlbum()
        }

        //Swipe to delete

        val creator = SwipeMenuCreator { menu ->

            // create "delete" item
            val deleteItem = SwipeMenuItem(activity)
            // set item background
            deleteItem.background = ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25))
            // set item width
            deleteItem.width = 200
            // set a icon
            deleteItem.setIcon(R.drawable.ic_baseline_delete_24)
            // add to menu
            menu.addMenuItem(deleteItem)
        }

        // set creator
        LV_albums.setMenuCreator(creator)

        //Swipe to delete - listener

        LV_albums.setOnMenuItemClickListener(object : SwipeMenuListView.OnMenuItemClickListener {
            override fun onMenuItemClick(position: Int, menu: SwipeMenu?, index: Int): Boolean {
                when (index) {
                    0 -> {
                        alertAndDeleteFromDatabase(position)
                    }
                }
                return false
            }
        })

    }

    private fun alertAndDeleteFromDatabase(position: Int) {

        MaterialAlertDialogBuilder(activity!!)
            .setTitle(resources.getString(R.string.delete_question))
            .setMessage(resources.getString(R.string.not_back))
            .setNegativeButton(resources.getString(R.string.no)) { dialog, which ->
                // nothing
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                refDbPhotoAlbum.child("${listOfAlbums[position].albumId}").removeValue()
            }
            .show()

    }

    private fun showAlertAddNewAlbum() {
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.setTitle(resources.getString(R.string.alert_add_new_album))
        val view = LayoutInflater.from(activity!!).inflate(R.layout.alert_input_text, null)

        dialogBuilder.setView(view)

        dialogBuilder.setPositiveButton(resources.getString(R.string.add)) { dialog, which ->
            if (!view.ET_inputText.text.toString().trim().isEmpty()) {
                addNewAlbum(view.ET_inputText.text.toString())
            }
        }

        dialogBuilder.setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->

        }


        val alertDialog = dialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setOnShowListener(OnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity!!, R.color.colorAccent))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activity!!, R.color.danger_color))
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                if (!view.ET_inputText.text.toString().trim().isEmpty()) {
                    addNewAlbum(view.ET_inputText.text.toString())
                    alertDialog.dismiss()
                }
            }
        })

        alertDialog.show()
    }

    private fun addNewAlbum(text: String) {
        val firebaseInput = PhotoAlbum(text, "${Date().time}")
        refDbPhotoAlbum.child(firebaseInput.albumId).setValue(firebaseInput.title)
    }

    private fun setupListView() {

        val albumsAdapter: ArrayAdapter<String> = ArrayAdapter<String>(activity, R.layout.cell_one_title_item, R.id.TV_Title, listOfAlbums.map { it.title } )

        LV_albums.adapter = albumsAdapter

        LV_albums.setOnItemClickListener {
                parent, view, position, id ->
            val intent = Intent(activity, PhotoGalleryActivity::class.java)
            val item = listOfAlbums[id.toInt()]
            intent.putExtra("idAlbum", item.albumId)
            intent.putExtra("titleAlbum", item.title)
            startActivity(intent)
        }



    }

}