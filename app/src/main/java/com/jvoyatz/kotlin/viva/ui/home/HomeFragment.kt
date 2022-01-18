package com.jvoyatz.kotlin.viva.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jvoyatz.kotlin.viva.databinding.FragmentHomeBinding
import com.jvoyatz.kotlin.viva.domain.InitializationState
import com.jvoyatz.kotlin.viva.domain.Item
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber


//since parent activity of this fragment participates
//in Hilt injection, then this class needs to be annotated too.
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var viewmodel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewmodel = ViewModelProvider(this).get(HomeViewModel::class.java)


        binding.itemsList.layoutManager = LinearLayoutManager(requireContext())
        binding.itemsList.adapter = ItemsAdapter(
                    ItemDiffUtil(),
                    {
                        it?.let {
                            viewmodel.navigateItemDetails(it)
                        }
                    },
                    CoroutineScope(Dispatchers.Default)
                )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewmodel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewmodel.initCacheState.collect { state ->

                    when(state){
                        InitializationState.ERROR -> Timber.d("an error occured")
                        InitializationState.INITIALIZED -> Timber.d("cache already initialized")
                        InitializationState.NOT_INITIALIZED -> Timber.d("first time")
                        InitializationState.REFRESH -> Timber.d("refresh")
                        else -> Timber.d("unknown")
                    }
                }
            }
        }

        viewmodel.selectedItem.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(HomeFragmentDirections.actionListFragmentToDetailFragment(it))
                viewmodel.onDoneNavigating()
            }
        })

//        viewmodel.itemsLiveData.observe(viewLifecycleOwner, {
//            it?.let {
//                when(it){
//                    is Resource.Loading -> Timber.d("show loading")
//                    is Resource.Success -> {
//                        val adapter = binding.itemsList.adapter as ItemsAdapter
//                        adapter.submitWithHeader(getString(R.string.items_header_results), it.data)
//                    }
//                    is Resource.Error -> Timber.d("error")
//                }
//            }
//        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}