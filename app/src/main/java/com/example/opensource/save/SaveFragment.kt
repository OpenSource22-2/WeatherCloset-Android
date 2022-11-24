package com.example.opensource.save

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.opensource.R
import com.example.opensource.Secret
import com.example.opensource.data.RetrofitObject
import com.example.opensource.data.remote.CreateRecordRequest
import com.example.opensource.data.remote.CreateRecordResponse
import com.example.opensource.databinding.FragmentSaveBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SaveFragment : BottomSheetDialogFragment() {

    companion object {
        private const val GALLERY = 101 // 갤러리 선택 시 인텐트로 보내는 값
        private const val TAG = "SAVE_FRAGMENT"
    }

    var imgFrom = 0 // 이미지 어디서 가져왔는지 (카메라 or 갤러리)
    private var imagePath = ""
    private var postUri = ""
    private lateinit var selectedChipList: Array<Boolean>

    @SuppressLint("SimpleDateFormat")
    var imageDate: SimpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
    private lateinit var intent: Intent

    private lateinit var ivGallery: ImageView
    private lateinit var btnUpload: Button
    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var imageUri: Uri
    var storage = FirebaseStorage.getInstance() // 파이어베이스 저장소 객체
    private lateinit var reference: StorageReference // 저장소 레퍼런스 객체 : storage 를 사용해 저장 위치를 설정

    private var heartState: Boolean = false

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

        ivGallery = binding.ivGallery
        btnUpload = binding.btnUpload

        initProgressDialog()
        clickIvGallery()
        clickBtnUpload()
        clickHeart()
        clickChip()

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
            (resources.displayMetrics.heightPixels * 0.94).toInt()
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    private fun clickIvGallery() {
        binding.ivGallery.setOnClickListener {
            intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY)
        }
    }

    private fun clickHeart() {
        binding.ivHeart.setOnClickListener {
            heartState = if (heartState) {
                binding.ivHeart.setImageResource(R.drawable.heart_empty)
                false
            } else {
                binding.ivHeart.setImageResource(R.drawable.heart_full)
                true
            }
        }
    }

    private fun countSelectedChips(): Int {
        var cnt = 0
        for (i in 0..9) {
            if (selectedChipList[i]) ++cnt
        }
        return cnt
    }

    private fun setChipList(chip: Chip, pos: Int) {
        val cnt = countSelectedChips()
        if (cnt == 3 && chip.isChecked)
            chip.isChecked = false
        else selectedChipList[pos] = chip.isChecked
    }

    private fun clickChip() {
        selectedChipList = Array(10) { false }
        binding.chip1.setOnClickListener { setChipList(binding.chip1, 0) }
        binding.chip2.setOnClickListener { setChipList(binding.chip2, 1) }
        binding.chip3.setOnClickListener { setChipList(binding.chip3, 2) }
        binding.chip4.setOnClickListener { setChipList(binding.chip4, 3) }
        binding.chip5.setOnClickListener { setChipList(binding.chip5, 4) }
        binding.chip6.setOnClickListener { setChipList(binding.chip6, 5) }
        binding.chip7.setOnClickListener { setChipList(binding.chip7, 6) }
        binding.chip8.setOnClickListener { setChipList(binding.chip8, 7) }
        binding.chip9.setOnClickListener { setChipList(binding.chip9, 8) }
        binding.chip10.setOnClickListener { setChipList(binding.chip10, 9) }
    }

    private fun clickBtnUpload() {
        btnUpload.setOnClickListener {
            if (imagePath.isNotEmpty() && imgFrom >= 100
                && binding.etMemo.text?.isNotEmpty() == true
                && binding.rbStar.rating > 0
                && countSelectedChips() > 0
            )
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
            // 좋아요 이미지 visible로 변경
            binding.ivHeart.visibility = View.VISIBLE
        }
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
//            CAMERA -> {
//                /*카메라 선택 시 생성했던 이미지파일명으로 reference 에 경로 세팅,
//                 * uploadTask 에서 생성한 이미지파일을 업로드하기로 설정*/reference = storage.reference.child("item")
//                    .child(imageFile.name) // imageFile.toString()을 할 경우 해당 파일의 경로 자체가 불러와짐
//                uploadTask = reference.putFile(Uri.fromFile(imageFile)) // 업로드할 파일과 업로드할 위치 설정
//            }
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
            postUri = uri.toString()
            saveRecord()    // data 전송
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

    // 서버에 데이터 전송(POST)
    private fun saveRecord() {
        val requestRecordData = CreateRecordRequest(
            comment = binding.etMemo.text.toString(),
            heart = heartState,
            imageUrl = postUri,
            stars = binding.rbStar.rating.toInt()
        )

        val call: Call<CreateRecordResponse> =
            RetrofitObject.provideWeatherClosetApi.createRecord(Secret.memberId, requestRecordData)

        call.enqueue(object : Callback<CreateRecordResponse> {
            override fun onResponse(
                call: Call<CreateRecordResponse>,
                response: Response<CreateRecordResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: success : ${response.body()}")
                } else {
                    Log.e(TAG, "onResponse: $response")
                }
            }
            override fun onFailure(call: Call<CreateRecordResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: $t")
            }
        })
    }
}