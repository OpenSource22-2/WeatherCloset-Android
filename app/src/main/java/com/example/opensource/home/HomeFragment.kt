package com.example.opensource.home

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.opensource.Secret
import com.example.opensource.data.RetrofitObject
import com.example.opensource.data.remote.HomeRecordResponse
import com.example.opensource.data.remote.RecordData
import com.example.opensource.data.remote.RecordResponse
import com.example.opensource.data.remote.WeatherData
import com.example.opensource.databinding.FragmentHomeBinding
import com.example.opensource.record.RecordFragment
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recordRvAdapter: HomeRecordRvAdapter

    companion object {
        const val TAG: String = "HOME_FRAGMENT"
        const val API_KEY: String = Secret.API_KEY
        const val WEATHER_URL: String = "https://api.openweathermap.org/data/2.5/weather"
        const val MIN_TIME: Long = 5000
        const val MIN_DISTANCE: Float = 1000F
        const val WEATHER_REQUEST: Int = 102
    }

    private lateinit var mLocationManager: LocationManager
    private lateinit var mLocationListener: LocationListener

    private lateinit var recordData: RecordData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

//      TODO: initPopUp()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setData() // 서버 통신
    }

    override fun onResume() {
        super.onResume()
        getWeatherInCurrentLocation()
    }

    private fun setData() {
        val call: Call<HomeRecordResponse> =
            RetrofitObject.provideWeatherClosetApi.getRecordList(Secret.memberId)

        call.enqueue(object : Callback<HomeRecordResponse> {
            override fun onResponse(
                call: Call<HomeRecordResponse>,
                response: Response<HomeRecordResponse>
            ) {
                if (response.isSuccessful) {
                    initAdapter(response.body()?.data!!)
                } else {
                    Log.e(TAG, "onResponse: response error: $response")
                }
            }

            override fun onFailure(call: Call<HomeRecordResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }
        })
    }

    private fun initAdapter(data: ArrayList<HomeRecordResponse.HomeRecordData>) {
        recordRvAdapter = HomeRecordRvAdapter(requireContext())
        recordRvAdapter.addItems(data)
        recordRvAdapter.notifyDataSetChanged()
        binding.rvRecord.adapter = recordRvAdapter
        clickRecordItemView()
    }

    fun getRecordInfo(recordId: Int) {
        val call: Call<RecordResponse> =
            RetrofitObject.provideWeatherClosetApi.getRecord(recordId)

        call.enqueue(object : Callback<RecordResponse> {
            override fun onResponse(
                call: Call<RecordResponse>,
                response: Response<RecordResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()}")
                    recordData = response.body()?.data!!
                    // show dialog
                    val dialog = RecordFragment(recordData)
                    dialog.show(childFragmentManager, "dialog")
                } else {
                    Log.e(TAG, "onResponse: response error: $response")
                }
            }

            override fun onFailure(call: Call<RecordResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }
        })
    }


    private fun clickRecordItemView() {
        recordRvAdapter.setItemClickListener(object :
            HomeRecordRvAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                // 기록 단건 조회
                val recordId = recordRvAdapter.recordList[position].id
                getRecordInfo(recordId)
            }
        })
    }

    private fun getWeatherInCurrentLocation() {
        mLocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mLocationListener = LocationListener { p0 ->
            val params = RequestParams()
            params.put("lat", p0.latitude)
            params.put("lon", p0.longitude)
            params.put("appid", API_KEY)
            doNetworking(params)
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                WEATHER_REQUEST
            )
            return
        }
        mLocationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            MIN_TIME,
            MIN_DISTANCE,
            mLocationListener
        )
        mLocationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME,
            MIN_DISTANCE,
            mLocationListener
        )
    }

    private fun doNetworking(params: RequestParams) {
        val client = AsyncHttpClient()

        client.get(WEATHER_URL, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                val weatherData = WeatherData().fromJson(response)
                if (weatherData != null) {
                    updateWeather(weatherData)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                super.onFailure(statusCode, headers, responseString, throwable)
            }
        })
    }

    private fun updateWeather(weather: WeatherData) {
        binding.tvTemperature.text = weather.tempString + "ºC"
        val resourceID = resources.getIdentifier(weather.icon, "drawable", activity?.packageName)
        binding.ivWeather.setImageResource(resourceID)
        val mFormat = SimpleDateFormat("yyyy. MM. dd")
        val mDate = System.currentTimeMillis()
        binding.tvTodayDate.text = mFormat.format(mDate).toString()
    }

    override fun onPause() {
        super.onPause()
        mLocationManager.removeUpdates(mLocationListener)
    }

}