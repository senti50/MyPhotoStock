package com.example.myphotostock

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_albums.*
import kotlinx.android.synthetic.main.fragment_scanner.*


class AlbumsFragment : Fragment() {

    private lateinit var listOfAlbums: List<PhotoAlbum>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listOfAlbums = getAlbumsFromDatabase()

        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle(resources.getString(R.string.albums))
        setupListView()

    }

    private fun getAlbumsFromDatabase(): List<PhotoAlbum> {
        //TODO: Pobieranie listy albumów z firebase
        val listOfAlb: List<PhotoAlbum> = listOf(PhotoAlbum("Album nr 1", "1234"), PhotoAlbum("Album nr 2", "12345"), PhotoAlbum("Album nr 3", "123456"))
        return listOfAlb
    }

    private fun setupListView() {

        val albumsAdapter: ArrayAdapter<String> = ArrayAdapter<String>(activity!!, R.layout.cell_one_title_item, R.id.TV_Title, listOfAlbums.map { it.title } )

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