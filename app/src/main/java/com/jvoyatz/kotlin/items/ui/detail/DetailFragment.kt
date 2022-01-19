package com.jvoyatz.kotlin.items.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jvoyatz.kotlin.items.databinding.FragmentDetailBinding
import timber.log.Timber

/**
 * displaying item details
 */
class DetailFragment : Fragment() {
    private var _binding : FragmentDetailBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val args = DetailFragmentArgs.fromBundle(it)
            Timber.d("onViewCreated: " + args.item.description)
            binding.item = args.item
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}