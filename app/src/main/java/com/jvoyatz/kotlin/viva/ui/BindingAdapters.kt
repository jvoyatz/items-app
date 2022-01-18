package com.jvoyatz.kotlin.viva.ui

import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jvoyatz.kotlin.viva.R
import com.jvoyatz.kotlin.viva.domain.Item
import com.jvoyatz.kotlin.viva.ui.home.ItemsAdapter
import com.jvoyatz.kotlin.viva.util.Resource
import timber.log.Timber

/**
 * custom xml attributes declared here
 */

@BindingAdapter("imgUrl")
fun bindImage(imgView: ImageView, imageUrl: String?){
    imageUrl?.let {
        val context = imgView.context
        Glide.with(context)
            .load(imageUrl)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.ic_baseline_broken_image_24))
            .into(imgView)
    }
}

@BindingAdapter("submitList")
fun submitListItems(recyclerView: RecyclerView, resource: Resource<List<Item>?>?){
    resource?.let {
        val adapter = recyclerView.adapter as ItemsAdapter
        when(it){
            is Resource.Loading -> adapter.submitLoading()
            is Resource.Success -> {
                val message = if (it.data.isNullOrEmpty()){
                                recyclerView.context.getString(R.string.items_header_exhausted)
                            }else {
                                recyclerView.context.getString(R.string.items_header_results)
                            }
                adapter.submitWithHeader(message, it.data)
            }
            is Resource.Error -> Toast.makeText(recyclerView.context, it.message ?: "Unknown Error Exception", Toast.LENGTH_SHORT).show()
        }
    }
}