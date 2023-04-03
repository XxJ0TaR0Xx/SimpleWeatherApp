package com.l_volkov_l.simpleweatherapp

import android.graphics.Point
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.l_volkov_l.simpleweatherapp.business.model.DailyWeatherModel
import com.l_volkov_l.simpleweatherapp.view.adapters.MainDailyListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_daily_list.*

class DailyListFragment: DailyBaseFragment<List<DailyWeatherModel>>() {

    private val dailyAdapter = MainDailyListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_out_right)
        enterTransition = inflater.inflateTransition(R.transition.slide_out_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daily_list,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {// 21 - view создалась и вызывается данный метод
        super.onViewCreated(view, savedInstanceState)

        dailyList.apply {
            adapter = dailyAdapter
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            dailyAdapter.clickListener = clickListener
        }
        mData?.let { updateView() } // если не null обновить
    }


    override fun updateView() {
        dailyAdapter.updateData(mData!!)
    }



    private val clickListener = object : MainDailyListAdapter.DayItemClick{
        override fun showDetails(data: DailyWeatherModel) {
            val fragment = DailyInfoFragment() // создаем объект который нам необходимо открыть
            fragment.setData(data) // передаём информацию для отображения
            fn.beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit()// replace - вместо dailyInformation open daily fragment, addToBackStack - кнопка назад
            // манипулирование фрагментами происходит между beginTransaction - ...  -commit
            Log.d("clickListener", "ROBIT")
        }
    }




}