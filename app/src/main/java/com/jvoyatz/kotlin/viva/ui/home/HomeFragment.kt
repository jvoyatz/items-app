package com.jvoyatz.kotlin.viva.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jvoyatz.kotlin.viva.databinding.FragmentHomeBinding
import timber.log.Timber

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var viewmodel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewmodel = ViewModelProvider(this, /*HomeViewModel.Factory(5)*/).get(HomeViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.itemsLiveData.observe(viewLifecycleOwner, {
            Timber.i("dispatched value $it")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}