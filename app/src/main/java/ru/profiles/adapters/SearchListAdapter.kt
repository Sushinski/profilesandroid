package ru.profiles.adapters

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import ru.profiles.profiles.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.autocomplete_item.view.*
import ru.profiles.viewmodel.SearchViewModel


class SearchListAdapter(private val mContext: Context,
                        private val mViewModel: SearchViewModel)
    : ArrayAdapter<String>(mContext, R.layout.autocomplete_item) {

    private val mListFilter = ListFilter()
    var mResultsList: List<String>? = null

    override fun getCount(): Int {
        return mResultsList?.size ?: 0
    }

    override fun getItem(position: Int): String? {
        return mResultsList?.get(position)
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val v = view ?: LayoutInflater.from(parent.context).inflate(R.layout.autocomplete_item, parent, false)
        v.item_text.text = getItem(position)
        return v
    }

    override fun getFilter(): Filter {
        return mListFilter
    }

    inner class ListFilter : Filter() {
        private val lock = Any()

        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val results = FilterResults()

            if (prefix == null || prefix.isEmpty()) {
                synchronized(lock) {
                    results.values = ArrayList<String>()
                    results.count = 0
                }
            } else results.values = mViewModel.getSearchSuggestions(prefix.toString().toLowerCase())
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            mResultsList = results?.values?.let{ it as ArrayList<String>}.also{ notifyDataSetChanged() }

        }

    }
}