package com.bkcreate.flickrbrowser

import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_photo_details.*

class PhotoDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)
        activateToolbar(true)

        // val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo
        val photo = intent.extras.getParcelable(PHOTO_TRANSFER) as Photo

        //photo_title.text = photo.title
        photo_title.text = resources.getString(R.string.photo_title_text, photo.title)
        photo_tags.text = resources.getString(R.string.photo_title_text, photo.tags)
        // getString holt den String aus "string.xml" und ersetzt der Platzhalter ("%s") aus dem XML mit dem übergebenen Parameter (photo.tags)
        // weitere Platzhalter können sein: %d für einen dezimal-Wert

        photo_author.text =  photo.author
        //photo_author.text =  resources.getString(R.string.photo_author_text, "my","red","car")

        Picasso.with(this).load(photo.link)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(photo_image)
    }
}
