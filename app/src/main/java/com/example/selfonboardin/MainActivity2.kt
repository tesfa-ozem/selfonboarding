package com.example.selfonboardin

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.example.selfonboardin.databinding.ActivityLoginBinding
import com.example.selfonboardin.ui.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_first.button
import kotlinx.android.synthetic.main.fragment_first.imageView
import kotlinx.android.synthetic.main.fragment_first.view.*

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val REQUEST_CODE = 100
    lateinit var imageData : Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main2)

        button.setOnClickListener { openGalleryForImage() }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
                imageView.setImageURI(data?.data) // handle chosen image
                if (data != null) {
                    button.visibility = GONE
                    upload.visibility = VISIBLE
                    imageData = data
                }
            }
        }catch (e:Exception){
            Log.e("File error", e.localizedMessage)
        }
    }
    private fun openGalleryForImage() {
        var intent = Intent(Intent.ACTION_PICK)

        if (Build.MANUFACTURER.equals("samsung", ignoreCase = true)) {
            intent = Intent("com.sec.android.app.myfiles.PICK_DATA")
            intent.putExtra("CONTENT_TYPE", "*/*")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
        } else {
            val mimeTypes = arrayOf(
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",  // .doc & .docx
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",  // .ppt & .pptx
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",  // .xls & .xlsx
                "text/plain",
                "application/pdf",
                "application/zip",
                "application/vnd.android.package-archive"
            )
            intent = Intent(Intent.ACTION_GET_CONTENT) // or ACTION_OPEN_DOCUMENT
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        }
        startActivityForResult(intent, REQUEST_CODE)
    }
    private fun uploadImage(data: Intent?){
        if (data != null) {
            this.let { data.dataString?.let { it1 -> UploadUtility(it.parent).uploadFile(it1) } }
        }
    }
}