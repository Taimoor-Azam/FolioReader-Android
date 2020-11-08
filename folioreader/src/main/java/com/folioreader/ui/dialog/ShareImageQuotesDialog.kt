package com.folioreader.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.*
import com.folioreader.R
import kotlinx.android.synthetic.main.fragment_share_image_quotes_dialog.*


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
}