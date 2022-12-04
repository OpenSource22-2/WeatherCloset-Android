package com.example.opensource.record

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.opensource.MySharedPreference
import com.example.opensource.R
import com.example.opensource.data.RetrofitObject
import com.example.opensource.data.remote.BaseResponse
import com.example.opensource.data.remote.CreateRecordRequest
import com.example.opensource.data.remote.RecordData
import com.example.opensource.databinding.ActivityRecordModifyBinding
import com.example.opensource.databinding.ViewEditRecordBinding
import com.example.opensource.record.RecordFragment.Companion.RECORD_DATA
import com.google.android.material.chip.Chip
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class RecordModifyActivity : AppCompatActivity() {

    companion object {
        private const val GALLERY = 101 // 갤러리 선택 시 인텐트로 보내는 값
        private const val TAG = "RECORD_MODIFY_FRAGMENT"
    }

    private lateinit var binding: ActivityRecordModifyBinding

    var imgFrom = 0 // 이미지 어디서 가져왔는지 (카메라 or 갤러리)
    private var imagePath = ""
    private var postUri = ""
    private lateinit var selectedChipList: Array<Boolean>
    private var recordId: Int = 0

    @SuppressLint("SimpleDateFormat")
    var imageDate: SimpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
    private lateinit var imageUri: Uri
    var storage = FirebaseStorage.getInstance() // 파이어베이스 저장소 객체
    private lateinit var reference: StorageReference // 저장소 레퍼런스 객체 : storage 를 사용해 저장 위치를 설정

    private var heartState: Boolean = false
    private var recordDate = ""
    private var changeImg = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickChip(binding.layoutEdit)
        clickDate()
        clickIvGallery()
        clickHeart()
        clickBtnSave()
        clickBtnDelete()
        setData()
    }

    private fun clickChip(layout: ViewEditRecordBinding) {
        selectedChipList = Array(10) { false }
        layout.chip1.setOnClickListener {
            setChipList(
                binding.layoutEdit.chip1,
                0
            )
        }
        layout.chip2.setOnClickListener {
            setChipList(
                binding.layoutEdit.chip2,
                1
            )
        }
        layout.chip3.setOnClickListener {
            setChipList(
                binding.layoutEdit.chip3,
                2
            )
        }
        layout.chip4.setOnClickListener {
            setChipList(
                binding.layoutEdit.chip4,
                3
            )
        }
        layout.chip5.setOnClickListener {
            setChipList(
                binding.layoutEdit.chip5,
                4
            )
        }
        layout.chip6.setOnClickListener {
            setChipList(
                binding.layoutEdit.chip6,
                5
            )
        }
        layout.chip7.setOnClickListener {
            setChipList(
                binding.layoutEdit.chip7,
                6
            )
        }
        layout.chip8.setOnClickListener {
            setChipList(
                binding.layoutEdit.chip8,
                7
            )
        }
        layout.chip9.setOnClickListener {
            setChipList(
                binding.layoutEdit.chip9,
                8
            )
        }
        layout.chip10.setOnClickListener {
            setChipList(
                binding.layoutEdit.chip10,
                9
            )
        }
    }

    private fun setChipList(chip: Chip, pos: Int) {
        val cnt = countSelectedChips()
        if (cnt == 3 && chip.isChecked)
            chip.isChecked = false
        else selectedChipList[pos] = chip.isChecked
    }

    private fun countSelectedChips(): Int {
        var cnt = 0
        for (i in 0..9) {
            if (selectedChipList[i]) ++cnt
        }
        return cnt
    }

    private fun setData() {
        intent.let {
            val recordData = it.getParcelableExtra<RecordData>(RECORD_DATA)
            Log.d(TAG, "setData: recordData: $recordData")
            val layout = binding.layoutEdit
            layout.tvSelectDate.text = recordData?.recordDate
            Glide.with(this).load(recordData?.imageUrl).into(layout.ivGallery)
            layout.ivGallery.alpha = 1F
            layout.ivGallery.setBackgroundResource(R.color.white)
            layout.ivHeart.visibility = View.VISIBLE
            layout.ivGallery.setPadding(0, 0, 0, 0)
            layout.rbStar.rating = recordData?.stars?.toFloat() ?: 0f
            layout.rbStar.stepSize = 1f
            layout.etMemo.setText(recordData?.comment)
            for (i in recordData?.tags!!) {
                setTag(layout, i)
            }
            postUri = recordData?.imageUrl.toString()
            recordDate = recordData?.recordDate.toString()
            setHeart(recordData?.heart)
            recordId = recordData?.id!!
        }
    }

    private fun setHeart(heart: Boolean?) {
        heartState = if (heart == true) {
            binding.layoutEdit.ivHeart.setImageResource(R.drawable.heart_white_line)
            true
        } else {
            binding.layoutEdit.ivHeart.setImageResource(R.drawable.heart_empty)
            false
        }
    }

    private fun setTag(layout: ViewEditRecordBinding, tag: String) {
        when (tag) {
            "더워요" -> {
                layout.chip1.isChecked = true
                selectedChipList[0] = true
            }
            "따뜻해요" -> {
                layout.chip2.isChecked = true
                selectedChipList[1] = true
            }
            "습해요" -> {
                layout.chip3.isChecked = true
                selectedChipList[2] = true
            }
            "추워요" -> {
                layout.chip4.isChecked = true
                selectedChipList[3] = true
            }
            "일교차가 커요" -> {
                layout.chip5.isChecked = true
                selectedChipList[4] = true
            }
            "선선해요" -> {
                layout.chip6.isChecked = true
                selectedChipList[5] = true
            }
            "쌀쌀해요" -> {
                layout.chip7.isChecked = true
                selectedChipList[6] = true
            }
            "건조해요" -> {
                layout.chip8.isChecked = true
                selectedChipList[7] = true
            }
            "바람이 불어요" -> {
                layout.chip9.isChecked = true
                selectedChipList[8] = true
            }
            "햇빛이 강해요" -> {
                layout.chip10.isChecked = true
                selectedChipList[9] = true
            }
        }
    }

    private fun clickDate() {
        binding.layoutEdit.tvSelectDate.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    recordDate = "$year. ${month + 1}. $dayOfMonth"
                    binding.layoutEdit.tvSelectDate.text = recordDate
                }
            val datePickerDialog = DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.spinnersShown = true
            datePickerDialog.show()
        }
    }

    private fun clickIvGallery() {
        binding.layoutEdit.ivGallery.setOnClickListener {
            changeImg = true
            intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY)
        }
    }

    private fun clickHeart() {
        binding.layoutEdit.ivHeart.setOnClickListener {
            heartState = if (heartState) {
                binding.layoutEdit.ivHeart.setImageResource(R.drawable.heart_empty)
                false
            } else {
                binding.layoutEdit.ivHeart.setImageResource(R.drawable.heart_white_line)
                true
            }
        }
    }

    private fun deleteRecord() {
        val call = RetrofitObject.provideWeatherClosetApi.deleteRecord(recordId)
        call.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: 삭제 성공")
                    finish()
                } else {
                    Log.d(TAG, "onResponse: 삭제 실패")
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun clickBtnDelete() {
        binding.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("삭제하시겠습니까?")
            builder.setPositiveButton("확인") { _, _ ->
                deleteRecord()
                finish()
            }
            builder.setNegativeButton("취소") { _, _ -> }
            builder.show()
        }
    }

    private fun validDate(): Boolean {
        if (recordDate.isEmpty()) {
            Toast.makeText(this, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
            return false
        } else {
            val date = recordDate.split(". ")
            val year = date[0].toInt()
            val month = date[1].toInt()
            val day = date[2].toInt()
            val cal = Calendar.getInstance()
            val curYear = cal.get(Calendar.YEAR)
            val curMonth = cal.get(Calendar.MONTH) + 1
            val curDay = cal.get(Calendar.DAY_OF_MONTH)
            if (year > curYear || (year == curYear && month > curMonth) || (year == curYear && month == curMonth && day > curDay)) {
                Toast.makeText(this, "오늘 이전 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun clickBtnSave() {
        binding.tvSave.setOnClickListener {
            if (binding.layoutEdit.etMemo.text?.isNotEmpty() == true
                && binding.layoutEdit.rbStar.rating > 0
                && countSelectedChips() > 0 && validDate()
            ) {
                if (changeImg) {
                    uploadImg()
                } else {
                    saveRecord()
                }
            } else {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show() // test
            }
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
                    .into(binding.layoutEdit.ivGallery)
                imgFrom = requestCode // 사진을 가져온 곳이 카메라일 경우 CAMERA(100), 갤러리일 경우 GALLERY(101)
            }
        }
    }

    private fun uploadImg() {
        Log.d(TAG, "uploadImg: uploading")
        // firebase storage 에 이미지 업로드하는 method
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
        }

        // 파일 업로드 시작
        uploadTask!!.addOnSuccessListener {
            // 업로드 성공 시 동작
            Log.d(TAG, "onSuccess: upload")
            downloadUri() // 업로드 성공 시 업로드한 파일 Uri 다운받기
        }.addOnFailureListener { // 업로드 실패 시 동작
            Log.d(TAG, "onFailure: upload")
        }
    }

    private fun downloadUri() {
        // 지정한 경로(reference)에 대한 uri 을 다운로드하는 method
        reference.downloadUrl.addOnSuccessListener { uri -> // uri 다운로드 성공 시 동작
            // 다운받은 uri를 인텐트에 넣어 다른 액티비티로 이동
            Log.d(TAG, "onSuccess: download uri: $uri")
            postUri = uri.toString()
            saveRecord()    // data 전송
        }.addOnFailureListener { // uri 다운로드 실패 시 동작
            Log.d(TAG, "onFailure: download")
        }
    }

    // 서버에 데이터 전송(POST)
    private fun saveRecord() {
        val requestRecordData = CreateRecordRequest(
            comment = binding.layoutEdit.etMemo.text.toString(),
            heart = heartState,
            imageUrl = postUri,
            stars = binding.layoutEdit.rbStar.rating.toInt(),
            recordDate = recordDate,
            tag = getTagList()
        )

        Log.d(TAG, "saveRecord: $requestRecordData")

        val call: Call<BaseResponse> =
            RetrofitObject.provideWeatherClosetApi.updateRecord(
                MySharedPreference.getMemberId(this),
                recordId,
                requestRecordData
            )

        call.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: success : ${response.body()}")
                } else {
                    Log.e(TAG, "onResponse: $response")
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: $t")
            }
        })
        finish()
    }

    private fun getTagList(): List<Long> {
        val tagList = mutableListOf<Long>()
        if (binding.layoutEdit.chip1.isChecked) {
            tagList.add(1)
        }
        if (binding.layoutEdit.chip2.isChecked) {
            tagList.add(2)
        }
        if (binding.layoutEdit.chip3.isChecked) {
            tagList.add(3)
        }
        if (binding.layoutEdit.chip4.isChecked) {
            tagList.add(4)
        }
        if (binding.layoutEdit.chip5.isChecked) {
            tagList.add(5)
        }
        if (binding.layoutEdit.chip6.isChecked) {
            tagList.add(6)
        }
        if (binding.layoutEdit.chip7.isChecked) {
            tagList.add(7)
        }
        if (binding.layoutEdit.chip8.isChecked) {
            tagList.add(8)
        }
        if (binding.layoutEdit.chip9.isChecked) {
            tagList.add(9)
        }
        if (binding.layoutEdit.chip10.isChecked) {
            tagList.add(10)
        }
        return tagList
    }
}