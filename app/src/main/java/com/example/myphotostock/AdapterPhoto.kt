package com.example.myphotostock

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_photo.view.*
import java.net.URI

class AdapterPhoto(private val context: Context, private val photosList: MutableList<Photo>): BaseAdapter() {

    override fun getCount(): Int {
        return photosList.count()
    }

    override fun getItem(position: Int): Any {
        return photosList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val photo = this.photosList[position]

        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val photoView = inflator.inflate(R.layout.cell_photo, null)
        //photoView.IV_photo.setImageResource(R.drawable.ic_baseline_cloud_download_24)
        Picasso.get().load(photo.urlToFile).error(R.drawable.ic_baseline_block_24).placeholder(R.drawable.ic_baseline_cloud_download_24).into(photoView.IV_photo)

        return photoView
    }

}