package com.jvoyatz.kotlin.items.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jvoyatz.kotlin.items.databinding.FragmentHomeListItemBinding
import com.jvoyatz.kotlin.items.databinding.FragmentHomeListItemHeaderBinding
import com.jvoyatz.kotlin.items.databinding.ItemsLoadingItemBinding
import com.jvoyatz.kotlin.items.domain.Item
import kotlinx.coroutines.*
import java.lang.IllegalStateException

private const val LOADING_ITEM = -1
private const val HEADER_ITEM = 0
private const val ADAPTER_ITEM = 1

class ItemsAdapter(diffUtil: ItemDiffUtil,
                   val clickListener: (Item) -> Unit,
                   val scope: CoroutineScope): ListAdapter<ListAdapterItem, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when(item){
            is ListAdapterItem.Header -> HEADER_ITEM
            is ListAdapterItem.AdapterItem -> ADAPTER_ITEM
            is ListAdapterItem.Loading -> LOADING_ITEM
            else -> throw IllegalStateException("what?")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            HEADER_ITEM -> HeaderViewHolder.create(parent)
            ADAPTER_ITEM -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = FragmentHomeListItemBinding.inflate(inflater, parent, false)
                return ItemViewHolder(binding){
                    when (val dataItem = getItem(it)){
                        is ListAdapterItem.AdapterItem -> clickListener(dataItem.item)
                        else -> {} //do nothing
                    }
                }
            }
            LOADING_ITEM -> LoadingViewHolder.create(parent)
            else -> throw IllegalStateException("error case")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HeaderViewHolder ->{
                val header = getItem(position) as ListAdapterItem.Header
                holder.bind(header.message)
            }
            is ItemViewHolder -> {
                val adapterItem = getItem(position) as ListAdapterItem.AdapterItem
                //holder.itemView.setOnClickListener {
                //    clickListener.onClick(adapterItem.item)
                //}
                holder.bind(adapterItem.item)
            }
        }
    }

    fun submitWithHeader(headerMessage: String, list: List<Item>?){
        scope.launch {
            delay(750)
            ListAdapterItem.Header.message = headerMessage
            val items: List<ListAdapterItem> = when{
                list.isNullOrEmpty() -> listOf(ListAdapterItem.Header)
                else -> listOf(ListAdapterItem.Header) + list.map { ListAdapterItem.AdapterItem(it) }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }

    fun submitLoading(){
        submitList(listOf(ListAdapterItem.Loading))
    }
}

class LoadingViewHolder(binding: ItemsLoadingItemBinding): RecyclerView.ViewHolder(binding.root){
    companion object {
        fun create(parent: ViewGroup): LoadingViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemsLoadingItemBinding.inflate(inflater, parent, false)
            return LoadingViewHolder(binding)
        }
    }
}
class HeaderViewHolder(val binding: FragmentHomeListItemHeaderBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(txt: String){
        binding.obj = txt
        binding.executePendingBindings()
    }

    companion object{
        fun create(parent: ViewGroup): HeaderViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = FragmentHomeListItemHeaderBinding.inflate(inflater, parent, false)
            return HeaderViewHolder(binding)
        }
    }
}

class ItemViewHolder(val binding:FragmentHomeListItemBinding, clickPosition: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root){
    init {
        itemView.setOnClickListener {
            clickPosition(adapterPosition)
        }
    }
    fun bind(item: Item){
        binding.apply {
            this.item = item
            //this.listener = listener
            executePendingBindings()
        }
    }
}
interface ItemsListener{
    fun onClick(item: Item)
}
class ItemDiffUtil : DiffUtil.ItemCallback<ListAdapterItem>(){
    override fun areItemsTheSame(oldItem: ListAdapterItem, newItem: ListAdapterItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ListAdapterItem, newItem: ListAdapterItem): Boolean {
        return oldItem == newItem
    }
}

sealed class ListAdapterItem{
    abstract val id: Int

    data class AdapterItem(val item: Item): ListAdapterItem(){
        override val id = item.id
    }
    object Header: ListAdapterItem(){
        override val id = -111
        lateinit var message: String
    }
    object Loading: ListAdapterItem(){
        override val id = -112
    }
}