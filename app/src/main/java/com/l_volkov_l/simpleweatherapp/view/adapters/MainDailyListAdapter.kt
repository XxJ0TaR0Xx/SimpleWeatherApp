package com.l_volkov_l.simpleweatherapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.textview.MaterialTextView
import com.l_volkov_l.simpleweatherapp.R
import com.l_volkov_l.simpleweatherapp.business.model.DailyWeatherModel
import com.l_volkov_l.simpleweatherapp.view.*
import java.lang.StringBuilder
import java.util.*

class MainDailyListAdapter : BaseAdapter<DailyWeatherModel>() {

    lateinit var clickListener: DayItemClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewGroup {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_main_daily, parent, false)
        return DailyViewGroup(view)
    }

    interface DayItemClick { // клик который открывает другой фрагмент
        fun showDetails(data: DailyWeatherModel)
    }

    @SuppressLint("NonConstantResourceId")
    inner class DailyViewGroup(view: View) : BaseViewHolder(view) {

        @BindView(R.id.day_container)
        lateinit var container: CardView

        @BindView(R.id.item_daily_date_tv)
        lateinit var date: MaterialTextView

        @BindView(R.id.item_daily_pop_tv)
        lateinit var popRate: MaterialTextView

        @BindView(R.id.item_daily_max_temp_tv)
        lateinit var maxTemp: MaterialTextView

        @BindView(R.id.item_daily_min_temp_tv)
        lateinit var minTemp: MaterialTextView

        @BindView(R.id.item_daily_weather_condition_icon)
        lateinit var icon: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun bindView(position: Int) {
            val itemData = mData[position]
            //val defaultTextColor = date.textColors
            if (position == 0) {
                this.setIsRecyclable(false)
                date.setTextColor(ContextCompat.getColor(date.context, R.color.purple_500))
            }
//            else{
//                date.setTextColor(defaultTextColor)
//            }

            container.setOnClickListener {
                clickListener.showDetails(itemData)
            }

            /// Игнорируем 0 в датах (01, 02, 03 ...)
            if (mData.isNotEmpty()) {
                itemData.apply {
                    val dateOfDay = dt.toDateFormatOf(DAY_WEEK_DAY_LONG)
                    date.text = if (dateOfDay.startsWith(
                            "0",
                            true
                        )
                    ) dateOfDay.removePrefix("0") else dateOfDay
                    minTemp.text =
                        StringBuilder().append(temp.min.toDegree()).append("\u00B0").toString()
                    maxTemp.text =
                        StringBuilder().append(temp.max.toDegree()).append("\u00B0").toString()
                    icon.setImageResource(weather[0].icon.provideIcon())
                    if (pop < 0.01) {
                        popRate.visibility = View.INVISIBLE
                    } else {
                        popRate.visibility = View.VISIBLE
                        popRate.text = pop.toPercentString("%")
                    }
                }
            }
        }
    }
}