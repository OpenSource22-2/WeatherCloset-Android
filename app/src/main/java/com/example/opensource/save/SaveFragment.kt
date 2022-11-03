package com.example.opensource.save

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.bumptech.glide.Glide
import com.example.opensource.R
import com.example.opensource.databinding.FragmentSaveBinding
import com.example.opensource.home.SetImageActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SaveFragment : BottomSheetDialogFragment() {

    companion object {
        private const val CAMERA = 100  // 카메라 선택시 인텐트로 보내는 값
        private const val GALLERY = 101 // 갤러리 선택 시 인텐트로 보내는 값
        private const val TAG = "SAVE_FRAGMENT"
    }

    var imgFrom = 0 // 이미지 어디서 가져왔는지 (카메라 or 갤러리)
    private var imagePath = ""

    @SuppressLint("SimpleDateFormat")
    var imageDate: SimpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
    private lateinit var intent: Intent

    private lateinit var btnCamera: Button
    private lateinit var ivGallery: ImageView
    private lateinit var btnMove: Button
    private lateinit var btnUpload: Button
    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var imageFile: File // 카메라 선택 시 새로 생성하는 파일 객체
    private lateinit var imageUri: Uri
    var storage = FirebaseStorage.getInstance() // 파이어베이스 저장소 객체
    private lateinit var reference: StorageReference // 저장소 레퍼런스 객체 : storage 를 사용해 저장 위치를 설정

    private lateinit var binding: FragmentSaveBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogHeight()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSaveBinding.inflate(inflater, container, false)

        btnCamera = binding.btnCamera
        ivGallery = binding.ivGallery
        btnMove = binding.btnMove
        btnUpload = binding.btnUpload

        initProgressDialog()
        clickBtnCamera()
        clickIvGallery()
        clickBtnMove()
        clickBtnUpload()

        return binding.root
    }

    private fun setDialogHeight() {
        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED // 높이 고정
            skipCollapsed = true // HALF_EXPANDED 안되게 설정
            isHideable = false
            isDraggable = false
        }
        binding.clDialog.layoutParams.height =
            (resources.displayMetrics.heightPixels * 0.90).toInt()
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    private fun clickBtnCamera() {
        btnCamera.setOnClickListener {
            intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                try {
                    imageFile = createImageFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val imageUri = FileProvider.getUriForFile(
                    getApplicationContext(),
                    requireContext().packageName,
                    imageFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, CAMERA)
            }
        }
    }

    private fun clickIvGallery() {
        binding.ivGallery.setOnClickListener {
            intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY)
        }
    }

    private fun clickBtnMove() {
        btnMove.setOnClickListener {
            intent = Intent(requireActivity(), SetImageActivity::class.java)
            intent.putExtra("path", imagePath)
            startActivity(intent)
        }
    }

    private fun clickBtnUpload() {
        btnUpload.setOnClickListener {
            if (imagePath.isNotEmpty() && imgFrom >= 100)
                uploadImg()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) { // 결과가 있을 경우
            // 갤러리를 선택한 경우 인텐트를 활용해 이미지 정보 가져오기
            if (requestCode == GALLERY) { // 갤러리 선택한 경우
                imageUri = data?.data!! // 이미지 Uri 정보
                imagePath = data.dataString!! // 이미지 위치 경로 정보
            }

            /*  카메라를 선택할 경우, createImageFile()에서 별도의 imageFile 을 생성 및 파일 절대경로 저장을 하기 때문에
            onActivityResult()에서는 별도의 작업 필요 없음 */

            // 저장한 파일 경로를 이미지 라이브러리인 Glide 사용하여 이미지 뷰에 세팅하기
            if (imagePath.isNotEmpty()) {
                Glide.with(this)
                    .load(imagePath)
                    .into(ivGallery)
                imgFrom = requestCode // 사진을 가져온 곳이 카메라일 경우 CAMERA(100), 갤러리일 경우 GALLERY(101)
                ivGallery.alpha = 1F
                ivGallery.setBackgroundResource(R.color.white)
                ivGallery.setPadding(0, 0, 0, 0)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File {
        // 이미지 파일 생성
        val timeStamp = imageDate.format(Date()) // 파일명 중복을 피하기 위한 "yyyyMMdd_HHmmss"꼴의 timeStamp
        val fileName = "IMAGE_$timeStamp" // 이미지 파일 명
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(
            fileName,
            ".jpg",
            storageDir
        ) // 이미지 파일 생성
        imagePath = file.absolutePath // 파일 절대경로 저장하기
        return file
    }

    private fun uploadImg() {
        Log.d(TAG, "uploadImg: uploading")
        // firebase storage 에 이미지 업로드하는 method
        showProgressDialog("업로드 중")
        var uploadTask: UploadTask? = null // 파일 업로드하는 객체
        when (imgFrom) {
            GALLERY -> {
                /*갤러리 선택 시 새로운 파일명 생성 후 reference 에 경로 세팅,
                 * uploadTask 에서 onActivityResult()에서 받은 인텐트의 데이터(Uri)를 업로드하기로 설정*/
                val timeStamp = imageDate.format(Date()) // 중복 파일명을 막기 위한 시간스탬프
                val imageFileName = "IMAGE_" + timeStamp + "_.png" // 파일명
                reference = storage.reference.child("item")
                    .child(imageFileName) // 이미지 파일 경로 지정 (/item/imageFileName)
                uploadTask = reference.putFile(imageUri) // 업로드할 파일과 업로드할 위치 설정
            }
            CAMERA -> {
                /*카메라 선택 시 생성했던 이미지파일명으로 reference 에 경로 세팅,
                 * uploadTask 에서 생성한 이미지파일을 업로드하기로 설정*/reference = storage.reference.child("item")
                    .child(imageFile.name) // imageFile.toString()을 할 경우 해당 파일의 경로 자체가 불러와짐
                uploadTask = reference.putFile(Uri.fromFile(imageFile)) // 업로드할 파일과 업로드할 위치 설정
            }
        }

        // 파일 업로드 시작
        uploadTask!!.addOnSuccessListener {
            // 업로드 성공 시 동작
            hideProgressDialog()
            Log.d(TAG, "onSuccess: upload")
            downloadUri() // 업로드 성공 시 업로드한 파일 Uri 다운받기
        }.addOnFailureListener { // 업로드 실패 시 동작
            hideProgressDialog()
            Log.d(TAG, "onFailure: upload")
        }
    }

    private fun downloadUri() {
        // 지정한 경로(reference)에 대한 uri 을 다운로드하는 method
        showProgressDialog("다운로드 중")
        reference.downloadUrl.addOnSuccessListener { uri -> // uri 다운로드 성공 시 동작
            // 다운받은 uri를 인텐트에 넣어 다른 액티비티로 이동
            Log.d(TAG, "onSuccess: download uri: $uri")
            /* setImageActivity로 점검
            intent = Intent(requireActivity(), SetImageActivity::class.java)
            intent.putExtra("path", uri.toString()) // 다운로드한 uri, String 형으로 바꿔 인텐트에 넣기
            startActivity(intent)
            */
        }.addOnFailureListener { // uri 다운로드 실패 시 동작
            Log.d(TAG, "onFailure: download")
        }
        hideProgressDialog()
        dismiss()
    }

    private fun initProgressDialog() {
        mProgressDialog = ProgressDialog(requireActivity())
    }

    private fun showProgressDialog(message: String?) {
        mProgressDialog.setMessage(message)
        mProgressDialog.isIndeterminate = true
        mProgressDialog.show()
    }

    private fun hideProgressDialog() {
        if (mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }
}