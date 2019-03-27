package ru.profiles.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.profiles.model.ServiceWithRelations
import ru.profiles.ui.view.ServiceViewHolder
import io.reactivex.subjects.PublishSubject
import ru.profiles.profiles.R
import io.reactivex.Observable


class ServicesAdapter  : PagedListAdapter<ServiceWithRelations, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private val onClickSubject = PublishSubject.create<ServiceWithRelations>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val service: ServiceWithRelations? = getItem(position)
        (holder as ServiceViewHolder).itemView.setOnClickListener {
            getItem(position)?.let{ onClickSubject.onNext(it) }
        }
        holder.bind(service)
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
            DiffUtil.ItemCallback<ServiceWithRelations>() {

            override fun areItemsTheSame(oldService: ServiceWithRelations,
                                         newService: ServiceWithRelations) = oldService.service?.id == newService.service?.id

            override fun areContentsTheSame(oldService: ServiceWithRelations,
                                            newService: ServiceWithRelations) = oldService == newService
        }
    }

    fun getPositionClicks(): Observable<ServiceWithRelations> {
        return onClickSubject.share()
    }
}