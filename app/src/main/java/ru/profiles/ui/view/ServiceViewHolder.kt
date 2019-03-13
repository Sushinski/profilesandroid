package ru.profiles.ui.view

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.service_card_layout.view.*
import ru.profiles.model.ServiceModel
import ru.profiles.profiles.R


class ServiceViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {

    private var mServiceModel: ServiceModel? = null

    fun bind(service: ServiceModel?) {
        mServiceModel = service
        //mView.card_imageView.setImageURI(service?.files?.get(0)  as String?)// todo get image
        mView.card_serviceTitle.text = Html.fromHtml(Html.fromHtml(service?.title).toString())
        mView.card_serviceCost.text = service?.cost ?: "0.0" // todo on empty cost
        mView.card_ratingValue.text = service?.ratings?.common?.toString() ?: "0.0"
        mView.card_respText.text = service?.membersCount?.toString() ?: "0"
    }

    companion object {
        fun create(parent: ViewGroup): ServiceViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.service_card_layout, parent, false)
            return ServiceViewHolder(view)
        }
    }
}