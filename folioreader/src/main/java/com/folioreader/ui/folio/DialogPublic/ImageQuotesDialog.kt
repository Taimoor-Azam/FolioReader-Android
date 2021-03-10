package com.folioreader.ui.folio.DialogPublic

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.view.View
import com.folioreader.R
import com.folioreader.util.FileUtil
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_image_quotes_dialog.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.ByteArrayOutputStream
import java.io.File

class ImageQuotesDialog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_quotes_dialog)

        val titleText = intent.extras?.getString("selectedTxtKey")
        quoteText?.text = titleText
        autherName?.text = "by ${FileUtil.mBookAuthor}"
        bookname?.text = FileUtil.mBookname

        orangeColor?.setOnClickListener { changeColorOfLayout(it) }
        greenColor?.setOnClickListener { changeColorOfLayout(it) }
        blueColor?.setOnClickListener { changeColorOfLayout(it) }
        grayColor?.setOnClickListener { changeColorOfLayout(it) }
        darkGreenColor?.setOnClickListener { changeColorOfLayout(it) }
        greenGridColor?.setOnClickListener { changeColorOfLayout(it) }
        orangeGridColor?.setOnClickListener { changeColorOfLayout(it) }
        camera?.setOnClickListener { changeColorOfLayout(it) }

        close?.setOnClickListener {
            finish()
        }

        shareIc?.setOnClickListener {
            getBitmapFromView(imageLayout)?.let { it1 -> shareImage(it1) }
        }
    }


    fun changeColorOfLayout(it: View) {
        when (it) {
            orangeColor -> {
                imageLayout?.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))
            }

            greenColor -> {
                imageLayout?.setBackgroundColor(ContextCompat.getColor(this, R.color.green))

            }

            blueColor -> {
                imageLayout?.setBackgroundColor(ContextCompat.getColor(this, R.color.light_blue))

            }

            grayColor -> {
                imageLayout?.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))

            }

            darkGreenColor -> {
                imageLayout?.setBackgroundColor(ContextCompat.getColor(this, R.color.darkgreen))

            }

            orangeGridColor -> {
                imageLayout?.background = ContextCompat.getDrawable(this, R.drawable.blue_gridiant)

            }

            greenGridColor -> {
                imageLayout?.background = ContextCompat.getDrawable(this, R.drawable.green_gridiant)

            }

            camera -> {
                if (isPermissionGranted()) {
                    EasyImage.openChooserWithGallery(this, "Select an image or make new one", 0)
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                backgroundImage.setImageURI(result.uri)
            }
        }

        EasyImage.handleActivityResult(
                requestCode,
                resultCode,
                data,
                this,
                object : DefaultCallback() {
                    override fun onImagesPicked(
                            imageFiles: MutableList<File>,
                            source: EasyImage.ImageSource?,
                            type: Int
                    ) {
                        CropImage.activity(Uri.fromFile(imageFiles.first()))
                                .setAspectRatio(5, 5)
                                .start(this@ImageQuotesDialog)
                    }
                })
    }

    private fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            return if (checkSelfPermission(android.Manifest.permission.CAMERA) === android.content.pm.PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                requestPermissions(
                        arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1
                )
                false
            }
        } else {
            return true
        }
    }


    fun shareImage(b: Bitmap) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"
        val bytes = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(contentResolver, b, "Title", null)
        val imageUri = Uri.parse(path)
        share.putExtra(Intent.EXTRA_STREAM, imageUri)
        startActivity(Intent.createChooser(share, "Select"))
    }

    open fun getBitmapFromView(view: View): Bitmap? {
        var bitmap =
                Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}