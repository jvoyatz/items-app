package com.jvoyatz.kotlin.viva.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jvoyatz.kotlin.viva.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewmodel

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