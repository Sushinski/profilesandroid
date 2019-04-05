package ru.profiles.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import ru.profiles.model.CategoryModel
import android.widget.TextView
import android.view.ViewGroup
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.category_card_item.view.*
import ru.profiles.profiles.R


class ResultCardsAdapter(context: Context,
                         resourceId: Int,
                         val mCategories: Array<CategoryModel>)
    : ArrayAdapter<CategoryModel>(context, resourceId){

    private val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = convertView ?: mInflater.inflate(R.layout.category_card_item, parent, false)
        val vh = convertView?.tag as ViewHolder? ?: ViewHolder( v.categoryText, v.categoryImage )
        vh.categoryText.text= mCategories[position].title
        //vh.categoryImage.setImageURI(mCategories[position].)
        return v
    }

    override fun getItem(position: Int): CategoryModel? {
        return mCategories[position]
    }

    data class ViewHolder( val categoryText: TextView, val categoryImage: SimpleDraweeView)
}