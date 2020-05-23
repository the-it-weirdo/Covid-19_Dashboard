package dev.kingominho.covid_19dashboard.ui.about

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kingominho.covid_19dashboard.R
import dev.kingominho.covid_19dashboard.databinding.AboutItemBinding
import dev.kingominho.covid_19dashboard.databinding.AboutItemGroupHeaderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1
private const val ITEM_VIEW_TYPE_ITEM_GROUP_TITLE = 2

class AboutAdapter : ListAdapter<AboutDataItem, RecyclerView.ViewHolder>(AboutDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun submitData(list: List<Any>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(AboutDataItem.Header)
                else -> listOf(AboutDataItem.Header) + list.map { item ->
                    when (item) {
                        is AboutItem -> AboutDataItem.AboutItemData(item)
                        is AboutHeading -> AboutDataItem.AboutHeadingData(item)
                        else -> throw ClassCastException("Unknown class $item")
                    }
                }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TitleViewHolder.from(
                parent
            )
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.from(
                parent
            )
            ITEM_VIEW_TYPE_ITEM_GROUP_TITLE -> ItemGroupTitleViewHolder.from(
                parent
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                val item = getItem(position) as AboutDataItem.AboutItemData
                holder.bind(item.aboutItem)
            }
            is ItemGroupTitleViewHolder -> {
                val item = getItem(position) as AboutDataItem.AboutHeadingData
                holder.bind(item.aboutHeading)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AboutDataItem.AboutItemData -> ITEM_VIEW_TYPE_ITEM
            is AboutDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is AboutDataItem.AboutHeadingData -> ITEM_VIEW_TYPE_ITEM_GROUP_TITLE
        }
    }

    /**
     * ViewHolder classes
     * */

    class ItemViewHolder private constructor(val binding: AboutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AboutItem) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AboutItemBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(
                    binding
                )
            }
        }
    }

    class ItemGroupTitleViewHolder private constructor(val binding: AboutItemGroupHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AboutHeading) {
            binding.itemGroupHeader = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemGroupTitleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AboutItemGroupHeaderBinding.inflate(layoutInflater, parent, false)
                return ItemGroupTitleViewHolder(
                    binding
                )
            }
        }
    }

    class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TitleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.about_header, parent, false)
                return TitleViewHolder(
                    view
                )
            }
        }
    }

}

class AboutDiffCallback : DiffUtil.ItemCallback<AboutDataItem>() {
    override fun areItemsTheSame(oldItem: AboutDataItem, newItem: AboutDataItem): Boolean {
        return oldItem.title == newItem.title
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: AboutDataItem, newItem: AboutDataItem): Boolean {
        return oldItem == newItem
    }

}

sealed class AboutDataItem {
    data class AboutItemData(val aboutItem: AboutItem) : AboutDataItem() {
        override val title: String
            get() = aboutItem.title
    }

    data class AboutHeadingData(val aboutHeading: AboutHeading) : AboutDataItem() {
        override val title: String
            get() = aboutHeading.heading
    }

    object Header : AboutDataItem() {
        override val title: String
            get() = "header"
    }

    abstract val title: String
}

data class AboutItem(
    val title: String,
    val description: String,
    val link: String
)

data class AboutHeading(
    val heading: String
)