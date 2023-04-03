package com.l_volkov_l.simpleweatherapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.l_volkov_l.simpleweatherapp.business.model.DailyWeatherModel
import com.l_volkov_l.simpleweatherapp.business.model.HourlyWeatherModel
import com.l_volkov_l.simpleweatherapp.business.model.WeatherDataModel
import com.l_volkov_l.simpleweatherapp.presenters.MainPresenter
import com.l_volkov_l.simpleweatherapp.view.*
import com.l_volkov_l.simpleweatherapp.view.adapters.MainHourlyListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.lang.StringBuilder
import kotlin.math.roundToInt


const val TAG = "GEO_TEST"
const val COORDINATES = "Coordinates"
class MainActivity : MvpAppCompatActivity(), MainView {

    private val mainPresenter by moxyPresenter { MainPresenter() }

    // Наши геоданные
    private val geoService by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val tokenSource: CancellationTokenSource = CancellationTokenSource()
    private val locationRequest by lazy {
        LocationRequest.Builder(Priority.PRIORITY_LOW_POWER,100000)
            .apply {
                setWaitForAccurateLocation(false)
                setMinUpdateIntervalMillis(5000)
                setMaxUpdateAgeMillis(600000)
            }.build()
    }

    private lateinit var mLocation: Location
    private var supportRefresh:Int = 0

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkGeoAvailability()
        initBottomSheets()
        initSwipeRefresh()

        ///При открытии, отображать загрузку
        refresh.isRefreshing = true

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer,DailyListFragment(), DailyListFragment::class.simpleName)
            .commit()


        if (!intent.hasExtra(COORDINATES)) {
            getGeo()
            Log.d(TAG, "onCreate: ")
        } else {
            val coord = intent.extras!!.getBundle(COORDINATES)!!
            val loc = Location("")
            loc.latitude = coord.getString("lat")!!.toDouble()
            loc.longitude = coord.getString("lon")!!.toDouble()
            mLocation = loc
            mainPresenter.refresh(
                lat = mLocation.latitude.toString(),
                lon = mLocation.longitude.toString()
            )
        }

        main_menu_btn.setOnClickListener{
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out)
        }

        main_settings_btn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out)
        }

        main_hourly_list.apply {
            adapter = MainHourlyListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        mainPresenter.enable()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        supportRefresh++
        if (supportRefresh > 1){
            mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
        }
    }

    // -------------------- Moxy code --------------------------

    // MainView
    override fun displayLocation(data: String) {
        main_city_name_tv.text = data
    }
    override fun displayCurrentData(data: WeatherDataModel) {
        data.apply {
            main_data_tv.text = current.dt.toDateFormatOf(DAY_FULL_MONTH_NAME)
            main_weather_condition_icon.setImageResource(current.weather[0].icon.provideIcon())
            main_temp.text = StringBuilder().append(current.temp.toDegree()).append("\u00B0").toString()
            daily[0].temp.apply {// daily[0] сегодняшний день
                main_temp_min_tv.text = min.toDegree()
                main_temp_avr_tv.text = eve.toDegree()
                main_temp_max_tv.text = max.toDegree()
            }

            val pressureSet = SettingsHolder.pressure
            main_pressure_mu_tv.text = getString(
                pressureSet.mesureUnitStringRes.toInt(),
                pressureSet.getValue(current.pressure.toDouble())
            )

            val windSpeedSet = SettingsHolder.windSpeed
            main_wind_speed_mu_tv.text = getString(
                windSpeedSet.mesureUnitStringRes.toInt(),
                windSpeedSet.getValue(current.wind_speed)
            )

            main_weather_image.setImageResource(current.weather[0].icon.provideImage())
            main_humidity_mu_tv.text = StringBuilder().append(current.humidity.toString()).append(" %").toString()
            main_sunrise_mu_tv.text = current.sunrise.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
            main_sunset_mu_tv.text = current.sunset.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
            main_weather_condition_description.text = current.weather[0].description
        }
    }

    override fun displayHourlyData(data: List<HourlyWeatherModel>) {
        (main_hourly_list.adapter as MainHourlyListAdapter).updateData(data)
    }

    override fun displayDailyData(data: List<DailyWeatherModel>) {
        (supportFragmentManager.findFragmentByTag(DailyListFragment::class.simpleName) as DailyListFragment).setData(data)
    }

    override fun displayError(error: Throwable) {
        Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
    }

    override fun setLocation(flag: Boolean) {
        refresh.isRefreshing = flag
    }

    // -------------------- Moxy code --------------------------


    // ------------------- location code ---------------

    @SuppressLint("MissingPermission")
    private fun getGeo(){
        geoService
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, tokenSource.token)
            .addOnSuccessListener {
                Log.d(TAG, "getGeo: ")
                if(it!=null){
                    mLocation = it
                    mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
                }else{
                    displayError(Exception("Geodata is not available"))
                }
                Log.d(TAG, "requestGeo: $it")
            }
    }

    ///Проверяем включина ли геопозиция на самом телефоне
    private fun checkGeoAvailability() {
        Log.d(TAG, "checkGeoAvailability: ")
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, 100)
                } catch (sendEx: IntentSender.SendIntentException) {

                }
            }
        }
    }

    // ------------------- location code ---------------
    ///Ограничение BottomSheets до guideline
    private fun initBottomSheets() {
        main_bottom_sheets.isNestedScrollingEnabled = true
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        val openSheets = CoordinatorLayout.LayoutParams(size.x, (size.y * 0.55).roundToInt())
        bottom_sheets_container.layoutParams = openSheets

    }

    ///Обновление layout
    private fun initSwipeRefresh() {
        refresh.apply {
            setProgressBackgroundColorSchemeColor(resources.getColor(R.color.bg_bottom_sheet))
            setColorSchemeResources(R.color.text_color)
            setProgressViewEndTarget(false,280)
            setOnRefreshListener {
                mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
            }
        }
    }


}