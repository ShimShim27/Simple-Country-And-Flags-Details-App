package com.countries.flag.ui.util

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import java.util.*

object MainUtil {

    fun getFlagsLink(shortName: String): String {
        return String.format(
            "https://raw.githubusercontent.com/hampusborgos/country-flags/main/png250px/${
                shortName.toLowerCase(
                    Locale.ROOT
                )
            }.png",
            shortName
        )
    }


    fun loadImageUrl(context: Context, uri: Uri, into: ImageView) {
        Glide.with(context).load(uri).into(into)
    }


    fun makeToast(context: Context,message:Any?){
        Toast.makeText(context,"$message",Toast.LENGTH_SHORT).show()
    }
}