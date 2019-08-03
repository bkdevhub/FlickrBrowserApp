package com.bkcreate.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject


private const val TAG = "GetFlickrJsonData"

class GetFlickrJsonData (private val listener: OnDataAvailable): AsyncTask<String, Void, ArrayList<Photo>> () {

    interface OnDataAvailable {
        fun onDataAvailable (data: List<Photo>)
        fun onError(exception: Exception)
    }

    override fun doInBackground(vararg params: String?): ArrayList<Photo> {

        // JSON parsing is done by the objects in the org.jsonPackage, which seems to be pretty easy

        Log.d(TAG, "doInBackground starts")

        val photoList = ArrayList<Photo>()

        try {
            val jsonData = JSONObject(params[0]) // for i in 0?
            val itemsArray = jsonData.getJSONArray("items")

            for(i in 0 until itemsArray.length()) {
                val jsonPhoto = itemsArray.getJSONObject(i) //Get the individual object within then array on position i (i.e. 1)
                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorId = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")

                val jsonMedia = jsonPhoto.getJSONObject("media")
                val photoUrl = jsonMedia.getString("m")
                val link = photoUrl.replaceFirst("_m.jpg","_b.jpg") //bigger version of the photo

                val photoObject = Photo(title,author,authorId,link,tags,photoUrl)
                photoList.add(photoObject)
                Log.d(TAG, "doInBackground: Add $photoObject")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e(TAG, "doInBackground: Error while parsing JSON: ${e.message}")
            cancel(true) //Verhindert das die onPostExecute Methode bei einem Fehler aufgerufen wird.
            listener.onError(e)
        }
        Log.d(TAG, "doInBackground finished")
        return photoList
    }

    override fun onPostExecute(result: ArrayList<Photo>) {
        Log.d(TAG, "onPostExecute starts")
        super.onPostExecute(result)
        listener.onDataAvailable(result)
    }
}