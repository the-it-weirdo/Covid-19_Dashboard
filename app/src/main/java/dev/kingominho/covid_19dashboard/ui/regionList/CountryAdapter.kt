package dev.kingominho.covid_19dashboard.ui.regionList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kingominho.covid_19dashboard.R
import dev.kingominho.covid_19dashboard.databinding.ListItemCountryBinding
import dev.kingominho.covid_19dashboard.domain.DomainCountry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class CountryAdapter(private val clickListener: CountryClickListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(
        CountryDiffCallback()
    ), Filterable {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    private var filteredData: List<DataItem>? = null
    private var allData : List<DataItem>? = null

    fun submitData(list: List<DomainCountry>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map {
                    DataItem.DomainCountryItem(
                        it
                    )
                }
            }
            withContext(Dispatchers.Main) {
                if(allData == null) {
                    allData = items
                }
                //filter = CustomFilter(list, this@CountryAdapter)
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(
                parent
            )
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(
                parent
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val countryItem = getItem(position) as DataItem.DomainCountryItem
                holder.bind(countryItem.domainCountry, clickListener)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.DomainCountryItem -> ITEM_VIEW_TYPE_ITEM
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
        }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                if(constraint.isNullOrEmpty()) {
                    filteredData = allData
                } else {
                    allData?.let {
                        val filteredList = arrayListOf<DataItem>()
                        for(dataItem in allData!!) {
                            if(dataItem is DataItem.DomainCountryItem) {
                                val filterString = constraint.toString().toLowerCase()
                                if(dataItem.domainCountry.countryName.toLowerCase().contains(filterString)) {
                                    filteredList.add(dataItem)
                                }
                            }
                        }
                        filteredData = filteredList
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredData
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredData = results?.values as List<DataItem>
                submitList(filteredData)
            }
        }
    }

    /**
     * ViewHolder class
     * */

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.country_list_header, parent, false)
                return TextViewHolder(
                    view
                )
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DomainCountry, clickListener: CountryClickListener) {
            binding.country = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCountryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class CountryDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.slug == newItem.slug
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class CountryClickListener(val clickListener: (slug: String) -> Unit) {
    fun onClick(country: DomainCountry) = clickListener(country.slug)
}

/**
 * DataItem class
 */
sealed class DataItem {
    data class DomainCountryItem(val domainCountry: DomainCountry) : DataItem() {
        override val slug = domainCountry.slug
    }

    object Header : DataItem() {
        override val slug = "header"
    }

    abstract val slug: String
}
