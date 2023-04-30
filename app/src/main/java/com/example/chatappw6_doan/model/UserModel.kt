package com.example.chatappw6_doan.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

data class UserModel (
    var name: String?,
    var status: String?,
    var image: String?,
    var number: String?,
    var uID: String?,
    var online: String?,
    var typing: String?,
    var token: String?,
){
    constructor() : this("","","","","","","","")

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: CircleImageView, image: String?) {
            if(image== null){

            }
            else {
                Glide.with(view.context).load(image).into(view)
            }

        }

    }
}