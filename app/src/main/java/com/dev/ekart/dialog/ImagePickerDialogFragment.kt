package com.dev.ekart.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.dev.ekart.databinding.DialogImagePickerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.http.Url
import java.io.File
import java.util.Date

class ImagePickerDialogFragment:BottomSheetDialogFragment() {
    private lateinit var arrayOfCameraPermission: Array<String>
    private lateinit var binding: DialogImagePickerBinding
    private val CAMERA_PERMISSION=1001
    private val CAMERA_IMAGE_REQUEST= 101

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogImagePickerBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrayOfCameraPermission = arrayOf<String>(Manifest.permission.CAMERA)
        arrayOfCameraPermission.plusElement(Manifest.permission.READ_EXTERNAL_STORAGE)
        arrayOfCameraPermission.plusElement(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    public fun captureImage(){

        val imageFolderPath = (Environment.getExternalStorageDirectory().toString()
                + "/EkartApp")
        val imagesFolder = File(imageFolderPath)
        imagesFolder.mkdirs()
        val imageName = Date().toString() + ".png"

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            Uri.fromFile(File(imageFolderPath,imageName))
        )
        requireActivity().startActivityFromFragment(
            this,
            takePictureIntent,
            CAMERA_IMAGE_REQUEST
        )
    }

    public fun openCamera(){
        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ){
            captureImage()
        } else {

            ActivityCompat.requestPermissions(
                requireActivity(), arrayOfCameraPermission,
                CAMERA_PERMISSION
            )
        }
    }

    public override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==CAMERA_PERMISSION){
            if(requireActivity().checkSelfPermission(arrayOfCameraPermission[0])==PackageManager.PERMISSION_GRANTED
                && requireActivity().checkSelfPermission(arrayOfCameraPermission[1])==PackageManager.PERMISSION_GRANTED
                && requireActivity().checkSelfPermission(arrayOfCameraPermission[2])==PackageManager.PERMISSION_GRANTED){}
        }else{
            ActivityCompat.requestPermissions(
                requireActivity(),arrayOfCameraPermission,
                CAMERA_PERMISSION
            )

        }
    }

    fun openGallery(){

    }
}