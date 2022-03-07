package com.andreasgift.cryptonews.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreasgift.cryptonews.MyNewsRecyclerViewAdapter
import com.andreasgift.cryptonews.R
import com.andreasgift.cryptonews.RetrofitAPI
import com.andreasgift.cryptonews.databinding.FragmentNewsBinding
import kotlinx.coroutines.*


class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: MyNewsRecyclerViewAdapter
    lateinit var layoutManager: LinearLayoutManager

    lateinit var api: RetrofitAPI

    var job: Job? = Job()

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

        binding.loadingBar.visibility = View.VISIBLE

        job =
            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                api = RetrofitAPI.create(requireContext())
                val list = api.fetchNews()
                withContext(Dispatchers.Main){
                    if (list.isSuccessful){
                        list.body()?.let { adapter.setData(it) }
                    } else {
                        Toast.makeText(requireContext(), "Error "+list.code(), Toast.LENGTH_SHORT).show()
                    }
                    binding.loadingBar.visibility = View.GONE
                }
            }

        binding.refreshLayout.setOnRefreshListener {
            if (this::api.isInitialized){
                job =
                    CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                        val list = api.fetchNews()
                        withContext(Dispatchers.Main){
                            if (list.isSuccessful){
                                list.body()?.let { adapter.setData(it) }
                            } else {
                                Toast.makeText(requireContext(), "Error "+list.code(), Toast.LENGTH_SHORT).show()
                            }
                            binding.refreshLayout.isRefreshing = false
                        }
                    }
            }
        }
    }

    override fun onDestroy() {
        job = null
        _binding = null
        super.onDestroy()
    }
}