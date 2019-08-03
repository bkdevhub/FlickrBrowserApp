package com.bkcreate.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


enum class DownloadStatus {
    OK,
    IDLE,
    NOT_INITIALISED,
    FAILED_OR_EMPTY,
    PERMISSION_ERROR,
    ERROR
}

private const val TAG = "GetRawData"

class GetRawData (private val listener: OnDownloadComplete): AsyncTask<String, Void, String>() {

    private var downloadStatus = DownloadStatus.IDLE

    interface OnDownloadComplete{
        fun onDownloadComplete(data: String, status:DownloadStatus)
    }

    override fun onPostExecute(result: String) {
        Log.d(TAG, "onPostExecute called")
        listener.onDownloadComplete(result, downloadStatus)
    }

    override fun doInBackground(vararg params: String?): String {
        Log.d(TAG, "doInBackground called")
        if (params[0] == null){
            downloadStatus = DownloadStatus.NOT_INITIALISED
            return "no URL specified"
        }
        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        } catch (e: Exception) {
           val errorMessage = when (e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "doInBackground: Error in URL: ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IO Exception reading data: ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSION_ERROR
                    "doInBackground: missing permissions? ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "doInBackground: Unknown Error: ${e.message}"
                }
            }
            Log.e(TAG,errorMessage)
            return errorMessage
        }

    }
}