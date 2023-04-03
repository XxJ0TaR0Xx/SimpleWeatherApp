package com.l_volkov_l.simpleweatherapp.view.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<D>: RecyclerView.Adapter<BaseAdapter.BaseViewHolder>(){

    // Тут будут данные, которые приходят с сервера
    private val _mDatta by lazy { mutableListOf<D>()}
    protected val mData : List<D> = _mDatta

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindView(position)
    }

    override fun getItemCount() = _mDatta.size

    fun updateData(data: List<D>){
        if(_mDatta.isEmpty() && data.isEmpty()){
            _mDatta.addAll(data)
        }else {
            _mDatta.clear()
            _mDatta.addAll(data)
        }

        notifyDataSetChanged()
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view){

        abstract  fun bindView(position: Int)
    }
}