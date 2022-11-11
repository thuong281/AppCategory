package com.example.appcategory

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appcategory.databinding.AppItemBinding

class AppAdapter : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {
    companion object {
        const val UPDATE_DOWNLOAD_PERCENT = 1
        const val DOWNLOAD_FINISH = 2
    }

    lateinit var listApp: List<App>

    lateinit var updateApp: (App, Int) -> Unit

    fun setList(list: List<App>) {
        listApp = list
        notifyDataSetChanged()
    }

    class AppViewHolder(val binding: AppItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: App) {
            binding.appName.text = data.appName
            binding.appDescription.text = data.appDescription
            if (!data.isInstalled) {
                binding.appVersion.text = "Chưa cài đặt"
                binding.appVersion.setBackgroundResource(R.color.light_black)
            } else {
                if (data.hasUpdate) {
                    binding.appVersion.setBackgroundResource(R.color.yellow)
                } else {
                    binding.appVersion.setBackgroundResource(R.color.colorPrimary)
                }
                binding.appVersion.text = data.appVersionName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = AppItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(listApp[position])
        holder.binding.appVersion.setOnClickListener {
            updateApp.invoke(listApp[position], position)
        }
    }

    override fun onBindViewHolder(
        holder: AppViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isNotEmpty() && payloads[0] == UPDATE_DOWNLOAD_PERCENT) {
            holder.binding.progress.visibility = View.VISIBLE
            holder.binding.image.visibility = View.GONE
            holder.binding.appVersion.isClickable = false
            holder.binding.progress.progress = listApp[position].downloadPercent
            if (listApp[position].downloadPercent == 100) {
                holder.binding.progress.visibility = View.GONE
                holder.binding.image.visibility = View.VISIBLE
                holder.binding.appVersion.isClickable = true
            }
        }
        if (payloads.isNotEmpty() && payloads[0] == DOWNLOAD_FINISH) {
            holder.binding.progress.progress = 0
            holder.binding.progress.visibility = View.GONE
            holder.binding.image.visibility = View.VISIBLE
            holder.binding.appVersion.isClickable = true

        }
    }

    override fun getItemCount() = listApp.size

}