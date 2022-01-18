package com.jvoyatz.kotlin.viva.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

/**
 * shows a recyclerview with a floaring action button
 * which gives the ability to refresh the local storage
 * and consequently the uiq
 */

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
        val itemsAdapter = ItemsAdapter(
            ItemDiffUtil(),
            {
                it?.let {
                    viewmodel.navigateItemDetails(it)
                }
            },
            CoroutineScope(Dispatchers.Default)
        )
        binding.itemsList.adapter = itemsAdapter

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewmodel

        binding.itemsRefreshFab.setOnClickListener {
            binding.swipeRefreshLayout.isRefreshing = true
            viewmodel.refresh()
            itemsAdapter.submitLoading()
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewmodel.refresh()
            itemsAdapter.submitLoading()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewmodel.initCacheState.collect { state ->
                    state?.let {
                        when (state) {
                            InitializationState.ERROR -> {
                                Toast.makeText(requireContext(),
                                    "An error occured while fetching items",
                                    Toast.LENGTH_SHORT).show()
                            }
                            InitializationState.INITIALIZED -> {}/*Toast.makeText(requireContext(), "Items Fetched", Toast.LENGTH_SHORT).show()*/
                            InitializationState.NOT_INITIALIZED -> {
                                Toast.makeText(requireContext(),
                                        "Fetching Items",
                                                Toast.LENGTH_SHORT).show()
                            }
                            InitializationState.REFRESH -> {
                                Toast.makeText(requireContext(),
                                    "Cache Refreshed",
                                            Toast.LENGTH_SHORT).show()
                                binding.swipeRefreshLayout.isRefreshing = false
                            }
                            else -> {
                                binding.swipeRefreshLayout.isRefreshing = false
                            }
                        }
                    }
                    viewmodel.onResetCacheStateValue()
                }
            }
        }

        viewmodel.selectedItem.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(HomeFragmentDirections.actionListFragmentToDetailFragment(it))
                viewmodel.onDoneNavigating()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}