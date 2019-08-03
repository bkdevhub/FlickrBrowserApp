package com.bkcreate.flickrbrowser

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

private const val TAG = "FlickrRecyclerViewAdapt" //TAG-Namen dürfen max. 23 Zeichen lang sein, andernfalls crasht die App

class FlickrImageViewHolder(view: View) : RecyclerView.ViewHolder(view){
    var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    var title: TextView = view.findViewById(R.id.title)
}

class FlickrRecyclerViewAdapter(private var photoList: List<Photo>) : RecyclerView.Adapter<FlickrImageViewHolder>() {

    //Häufiger Fehler! Die ViewHolder-Klasse als "inner class" des Recycler Adapter deklarieren.
    // Dies kann zu memory leaks führen.

    fun loadNewData(newPhotos: List<Photo>){
        photoList = newPhotos
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int) : Photo? {
        return if(photoList.isNotEmpty()) photoList[position] else null //null als return sollte möglichst vermieden werden, manchmal geht es aber nicht ohne.
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        //Called by Layoutmanager when it needs a new View
        Log.d(TAG, "onCreateViewHolder called (new view requested)")

        //Variable Inflate Layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
                                                                //Weiterer häufiger Fehler! attachToRoot=null. Die App weiß somit nicht, welcher Style
                                                                //angewendet werden soll und vergibt Standard Styles.
        return FlickrImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        //Log.d(TAG, "getItemCount called")
        return if(photoList.isNotEmpty()) photoList.size else 1
    }

    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {
        //This method is called by the recycler view when it wants new data to be stored in an existing view holder so that it can display it

        if(photoList.isEmpty()){
            holder.thumbnail.setImageResource(R.drawable.placeholder)
            holder.title.setText(R.string.placeholder_no_image_text)
        } else {

            Log.d(TAG, "onBindViewHolder called")

            val photoItem = photoList[position]

            Picasso.with(holder.thumbnail.context).load(photoItem.image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail)

            holder.title.text = photoItem.title
        }

    }
}

