package com.folioreader.ui.folio.DialogPublic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.*
import com.folioreader.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_share_image_quotes_dialog.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File


class ShareImageQuotesDialog() : DialogFragment() {

    var title: String? = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share_image_quotes_dialog, container, false)
    }

    @SuppressLint("ValidFragment")
    constructor(title: String) : this() {
        this.title = title
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dl = super.onCreateDialog(savedInstanceState)
        dl.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dl.window?.setGravity(Gravity.CENTER)
        return dl
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quoteText?.text = title

        orangeColor?.setOnClickListener {  changeColorOfLayout(it)}
        greenColor?.setOnClickListener {  changeColorOfLayout(it)}
        blueColor?.setOnClickListener {  changeColorOfLayout(it)}
        grayColor?.setOnClickListener {  changeColorOfLayout(it)}
        darkGreenColor?.setOnClickListener {  changeColorOfLayout(it)}
        greenGridColor?.setOnClickListener {  changeColorOfLayout(it)}
        orangeGridColor?.setOnClickListener {  changeColorOfLayout(it)}
        camera?.setOnClickListener {  changeColorOfLayout(it)}
    }


    fun changeColorOfLayout(it: View) {
        when(it){
            orangeColor ->{
                activity?.let { it1 -> ContextCompat.getColor(it1,R.color.orange) }?.let { it2 -> imageLayout?.setBackgroundColor(it2) }
            }

            greenColor ->{
                activity?.let { it1 -> ContextCompat.getColor(it1,R.color.green) }?.let { it2 -> imageLayout?.setBackgroundColor(it2) }

            }

            blueColor ->{
                activity?.let { it1 -> ContextCompat.getColor(it1,R.color.light_blue) }?.let { it2 -> imageLayout?.setBackgroundColor(it2) }

            }

            grayColor ->{
                activity?.let { it1 -> ContextCompat.getColor(it1,R.color.gray) }?.let { it2 -> imageLayout?.setBackgroundColor(it2) }

            }

            darkGreenColor ->{
                activity?.let { it1 -> ContextCompat.getColor(it1,R.color.darkgreen) }?.let { it2 -> imageLayout?.setBackgroundColor(it2) }

            }

            orangeGridColor ->{
                activity?.let { it1 -> ContextCompat.getDrawable(it1,R.drawable.blue_gridiant) }?.let { it2 -> imageLayout?.background = it2 }

            }

            greenGridColor ->{
                activity?.let { it1 -> ContextCompat.getDrawable(it1,R.drawable.green_gridiant) }?.let { it2 -> imageLayout?.background = it2 }

            }

            camera ->{
                if (isPermissionGranted()) {
                    EasyImage.openChooserWithGallery(this, "Select an image or make new one" , 0)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(
                requestCode,
                resultCode,
                data,
                activity,
                object : DefaultCallback() {
                    override fun onImagesPicked(
                            imageFiles: MutableList<File>,
                            source: EasyImage.ImageSource?,
                            type: Int
                    ) {
                        activity?.let {
                            CropImage.activity(Uri.fromFile(imageFiles.first()))
                                    .setAspectRatio(1, 1)
                                    .start(it)
                        }
                    }
                })
    }
}