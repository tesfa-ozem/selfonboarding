package com.example.selfonboardin

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.selfonboardin.databinding.FragmentFirstBinding
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_first.view.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    val REQUEST_CODE = 100
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.buttonFirst.setOnClickListener { openGalleryForImage() }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
                imageView.setImageURI(data?.data) // handle chosen image
                if (data != null) {
                    activity?.let { data.dataString?.let { it1 -> UploadUtility(it.parent).uploadFile(it1) } }
                }
            }
        }catch (e:Exception){
            Log.e("File error", e.localizedMessage)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
}