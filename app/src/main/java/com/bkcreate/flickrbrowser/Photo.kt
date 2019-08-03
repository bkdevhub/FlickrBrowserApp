package com.bkcreate.flickrbrowser

import android.os.Parcel
import android.os.Parcelable
//import java.io.Serializable

private const val TAG = "Photo"

class Photo(var title: String, var author: String, var authorID: String, var link: String, var tags: String, var image: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun toString(): String {
        return "Photo(title='$title', author='$author', authorID='$authorID', link='$link', tags='$tags', image='$image')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(authorID)
        parcel.writeString(link)
        parcel.writeString(tags)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }


    /***** Serializable auskommentiert, um Parcelable zu nutzen.

    Generell wird behauptet das Parcelable schneller ist als Serializable.
    Alleridings ist es mit den implementierten Methoden (writeObject,readObject, readObjectNoData) oftmals sogar schneller!

    //companion object in Kotlin ist = static field in Java (nachschlagen!)
    companion object{
        private const val serialVersionUID = 1L
    }

     /// Durch die folgenden Funktionen steigert sich die Performance von "Serializable", da die JVM nicht mehr zur Laufzeit prüfen muss, was wohin kommt.
    @Throws(IOException::class)
        private fun writeObject(out: java.io.ObjectOutputStream) {
        Log.d(TAG,"writeObject: called")
        out.writeUTF(title)
        out.writeUTF(author)
        out.writeUTF(authorID)
        out.writeUTF(link)
        out.writeUTF(tags)
        out.writeUTF(image)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject (inStream: java.io.ObjectInputStream) {
        Log.d(TAG,"")
        title = inStream.readUTF()
        author = inStream.readUTF()
        authorID = inStream.readUTF()
        link = inStream.readUTF()
        tags = inStream.readUTF()
        image = inStream.readUTF()
    }

    @Throws(ObjectStreamException::class)
    private fun readObjectNoData(){
        Log.d(TAG,"readObjectNoData called")
    }
    */
}