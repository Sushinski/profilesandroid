package ru.profiles.adapters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.profiles.model.ServiceModel
import ru.profiles.ui.view.ServiceViewHolder
import ru.profiles.profiles.R

class ServicesAdapter  : PagedListAdapter<ServiceModel, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val service: ServiceModel? = getItem(position)
        (holder as ServiceViewHolder).bind(service)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        onBindViewHolder(holder, position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.service_card_layout -> ServiceViewHolder.create(parent)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }


    override fun getItemViewType(position: Int): Int {
        return R.layout.service_card_layout
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<ServiceModel>() {

            override fun areItemsTheSame(oldService: ServiceModel,
                                         newService: ServiceModel) = oldService.id == newService.id

            override fun areContentsTheSame(oldService: ServiceModel,
                                            newService: ServiceModel) = oldService == newService
        }
    }
}