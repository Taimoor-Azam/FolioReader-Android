package com.folioreader.ui.folio.DialogPublic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.Toast
import com.folioreader.R


import kotlinx.android.synthetic.main.fragment_share_image_quotes_dialog.*

import java.io.ByteArrayOutputStream
import java.io.File


@SuppressLint("ValidFragment")

class ShareImageQuotesDialog(val title: String, val author: String) : DialogFragment() {


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share_image_quotes_dialog, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quoteText?.text = title
        autherName?.text = author

        orangeColor?.setOnClickListener { changeColorOfLayout(it) }
        greenColor?.setOnClickListener { changeColorOfLayout(it) }
        blueColor?.setOnClickListener { changeColorOfLayout(it) }
        grayColor?.setOnClickListener { changeColorOfLayout(it) }
        darkGreenColor?.setOnClickListener { changeColorOfLayout(it) }
        greenGridColor?.setOnClickListener { changeColorOfLayout(it) }
        orangeGridColor?.setOnClickListener { changeColorOfLayout(it) }
        camera?.setOnClickListener { changeColorOfLayout(it) }

        close?.setOnClickListener {
            dialog?.dismiss()
        }

        shareIc?.setOnClickListener {
            getBitmapFromView(imageLayout)?.let { it1 -> shareImage(it1) }
        }
    }


    fun changeColorOfLayout(it: View) {
        when (it) {
            orangeColor -> {
                activity?.let { it1 -> ContextCompat.getColor(it1, R.color.orange) }?.let { it2 -> imageLayout?.setBackgroundColor(it2) }

            }

            greenColor -> {
                activity?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 -> imageLayout?.setBackgroundColor(it2) }
            }

            blueColor -> {
                activity?.let { it1 -> ContextCompat.getColor(it1, R.color.light_blue) }?.let { it2 -> imageLayout?.setBackgroundColor(it2) }

            }

            grayColor -> {
                activity?.let { it1 -> ContextCompat.getColor(it1, R.color.gray) }?.let { it2 -> imageLayout?.setBackgroundColor(it2) }

            }

            darkGreenColor -> {
                activity?.let { it1 -> ContextCompat.getColor(it1, R.color.darkgreen) }?.let { it2 -> imageLayout?.setBackgroundColor(it2) }

            }

            orangeGridColor -> {
                activity?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.blue_gridiant) }?.let { it2 -> imageLayout?.background = it2 }

            }

            greenGridColor -> {
                activity?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.green_gridiant) }?.let { it2 -> imageLayout?.background = it2 }

            }

            camera -> {
                if (isPermissionGranted()) {

                }

            }
        }

    }


    private fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            return if ((activity?.let {
                        ActivityCompat.checkSelfPermission(
                                it,
                                android.Manifest.permission.CAMERA
                        )
                    } === android.content.pm.PackageManager.PERMISSION_GRANTED)) {
                true
            } else {
                requestPermissions(
                        arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1
                )
                //                activity?.let {requestPermissions(it, arrayOf<String>(Manifest.permission.CALL_PHONE), 1) }
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    fun shareImage(b: Bitmap) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"
        val bytes = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(activity?.contentResolver, b, "Title", null)
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