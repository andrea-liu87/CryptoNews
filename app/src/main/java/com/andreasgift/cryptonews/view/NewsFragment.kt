package com.andreasgift.cryptonews.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreasgift.cryptonews.MyNewsRecyclerViewAdapter
import com.andreasgift.cryptonews.R
import com.andreasgift.cryptonews.RetrofitAPI
import com.andreasgift.cryptonews.databinding.FragmentNewsBinding
import com.andreasgift.cryptonews.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: MyNewsRecyclerViewAdapter
    lateinit var layoutManager: LinearLayoutManager

    lateinit var api: RetrofitAPI

    private val viewModel by activityViewModels<NewsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        api = RetrofitAPI.create(requireContext())

        val itemListener = object: MyNewsRecyclerViewAdapter.NewsItemListener{
            override fun itemClickListener(url: String) {
                val bundle = bundleOf("url" to url)
                NavHostFragment.findNavController(this@NewsFragment).navigate(R.id.action_newsFragment_to_detailFragment, bundle)
            }
        }

        adapter = MyNewsRecyclerViewAdapter(itemListener)
        layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = adapter
        binding.list.layoutManager = layoutManager
        binding.list.setHasFixedSize(true)

        viewModel.fetchDataFromNet()

        viewModel.newsList.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, {
            if (it == true){
                binding.loadingBar.visibility = View.VISIBLE
            } else {
                binding.loadingBar.visibility = View.INVISIBLE
                binding.refreshLayout.isRefreshing = false
            }
        })

        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchDataFromNet()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}