package ru.profiles.ui.view

import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.service_card_layout.view.*
import ru.profiles.model.ServiceWithRelations
import ru.profiles.profiles.BuildConfig
import ru.profiles.profiles.R


class ServiceViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

    private var mServiceModel: ServiceWithRelations? = null

    fun bind(service: ServiceWithRelations?) {
        mServiceModel = service
        val u = service?.photoModels?.let{ if(it.isNotEmpty()) it[0] else null}
            ?.photos?.let { if(it.isNotEmpty()) it[0] else null }?.fileName
        u?.let{
            if(u.isNotEmpty()) mView.card_imageView.setImageURI(
                Uri.parse("${BuildConfig.MINIO_URL}/profiles/medium/$it")
            )
        }
        mView.card_serviceTitle.text = Html.fromHtml(service?.service?.title).toString()
        mView.card_serviceCost.text = "${service?.service?.cost} руб" // todo on empty cost
        mView.card_ratingValue.text = service?.service?.ratings?.common?.toString() ?: "0.0"
        mView.card_respText.text = service?.service?.membersCount?.toString() ?: "0"
    }

    companion object {
        fun create(parent: ViewGroup): ServiceViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.service_card_layout, parent, false)
            return ServiceViewHolder(view)
        }
    }
}