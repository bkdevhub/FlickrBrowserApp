package com.bkcreate.flickrbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.content_main.*

private const val TAG = "MainActivity"

class MainActivity : BaseActivity(), GetRawData.OnDownloadComplete,
    GetFlickrJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener {

    private val flickrRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activateToolbar(false)

        recycler_View.layoutManager = LinearLayoutManager(this)
        recycler_View.addOnItemTouchListener(RecyclerItemClickListener(this,recycler_View,this))
        recycler_View.adapter = flickrRecyclerViewAdapter
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, "onItemClick called on position $position")
        Toast.makeText(this,"onItemClick called on position $position", Toast.LENGTH_SHORT ).show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG, "onItemLongClick called on position $position")

        val photo = flickrRecyclerViewAdapter.getPhoto(position)
        if (photo != null) {
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER, photo) // put key value pair für das Objekt das weitergereicht wird.
            startActivity(intent)
        }

    }

    private fun createUri(baseUrl: String, tags: String, lang: String, matchAll: Boolean) :String {
        Log.d(TAG, "createUri starts")

        return Uri.parse(baseUrl).
            buildUpon().
            appendQueryParameter("tags",tags).
            appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY").
            appendQueryParameter("lang", lang).
            //fixed params
            appendQueryParameter("format","json"). // give JSON
            appendQueryParameter("nojsoncallback","1"). // for valid JSON
            build().toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG,"onCreateOptionsMenu called")
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG,"onOptionsItemSelected called")
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus){
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: Download successful! $data")

            val getFlickrJsonData = GetFlickrJsonData(this) //"this" gibt die aktuelle Instanz der Klasse mit, damit man mitbekommt, wenn der Async-Task fertig ist bzw. ob/welche Fehler aufgetreten sind
            getFlickrJsonData.execute(data)

        } else {
            Log.d(TAG, "onDownloadComplete: Error during download with status: $status and Error Message: $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG,"onDataAvailable called:"/*$data"*/)

        flickrRecyclerViewAdapter.loadNewData(data)

        Log.d(TAG,"onDataAvailable finished")
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, "onError called with $exception")
    }

    override fun onPostResume() {
        Log.d(TAG,".onResume starts")
        super.onPostResume()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult = sharedPref.getString(FLICKR_QUERY, "") as String
        if (queryResult.isNotEmpty()){
            val url = createUri("https://api.flickr.com/services/feeds/photos_public.gne",queryResult, "en-us", true)
            val getRawData = GetRawData(this)
            getRawData.execute(url)
        }
        Log.d(TAG,".onResume ends")
    }
}