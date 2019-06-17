package com.example.superheroesfromaliexpress.dialog

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.crazylegend.kotlinextensions.bitmap.toByteArray
import com.crazylegend.kotlinextensions.context.shortToast
import com.crazylegend.kotlinextensions.glide.GlideApp
import com.crazylegend.kotlinextensions.livedata.compatProvider
import com.crazylegend.kotlinextensions.views.getString
import com.example.superheroesfromaliexpress.R
import com.example.superheroesfromaliexpress.core.SuperHeroAbstractActivity
import com.example.superheroesfromaliexpress.database.MyHeroModel
import com.example.superheroesfromaliexpress.viewmodels.MyHeroDatabaseVM
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.add_my_hero_dialog.*

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

class AddMyHeroDialog : SuperHeroAbstractActivity() {
    override fun disposeResources() {
    }

    override fun setView(): Int = R.layout.add_my_hero_dialog

    override fun initView() {
        addImage = d_addImage
        nameInput = d_name
        genderInput = d_gender
        raceInput = d_race
        cancel = d_cancel
        save = d_save
    }


    fun Activity.pickImageNow(PICK_IMAGES_CODE: Int, allowMultiple: Boolean = false, title: String = "Pick images") {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
                .setType("image/*")

        startActivityForResult(Intent.createChooser(intent, title), PICK_IMAGES_CODE)
    }

    override fun initLateInitVars() {
        myHeroDatabaseVM = compatProvider()

        addImage.setOnClickListener {
            this.pickImageNow(REQUEST_IMAGE_PICK)
        }

        save.setOnClickListener {

            if (nameInput.getString.isEmpty()) {
                nameInput.error = "Enter a name"
                return@setOnClickListener
            }
            if (genderInput.getString.isEmpty()) {
                genderInput.error = "Enter a gender"
                return@setOnClickListener
            }
            if (raceInput.getString.isEmpty()) {
                raceInput.error = "Enter a race"
                return@setOnClickListener
            }

            if (imageBytes == null) {
                shortToast("Pick an image")
                return@setOnClickListener
            }

            myHeroDatabaseVM.insertModel(MyHeroModel(name = nameInput.getString, gender = genderInput.getString, image = imageBytes!!, race = raceInput.getString)) {
                finish()
            }

            shortToast("Saved")


        }

        cancel.setOnClickListener {
            finish()
        }
    }

    private lateinit var myHeroDatabaseVM: MyHeroDatabaseVM
    private lateinit var addImage: AppCompatImageView
    private lateinit var nameInput: TextInputEditText
    private lateinit var genderInput: TextInputEditText
    private lateinit var raceInput: TextInputEditText
    private lateinit var cancel: MaterialButton
    private lateinit var save: MaterialButton
    private val REQUEST_IMAGE_PICK = 122
    private var imageBytes: ByteArray? = null

    private fun loadImage(bytes: ByteArray?) {
        GlideApp.with(this)
                .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .load(bytes)
                .into(addImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri = data?.data

        if (resultCode == Activity.RESULT_OK) {
            imageBytes = null
            when (requestCode) {
                REQUEST_IMAGE_PICK -> {
                    uri?.apply {

                        val pfd = contentResolver.openFileDescriptor(this, "r")

                       val bmp = BitmapFactory.decodeFileDescriptor(pfd?.fileDescriptor)

                        bmp.toByteArray(Bitmap.CompressFormat.JPEG, 1)?.apply {
                            subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        imageBytes = it
                                        loadImage(it)
                                    },{
                                        it.printStackTrace()
                                    })
                        }

                    }
                }

            }
        }

    }
}