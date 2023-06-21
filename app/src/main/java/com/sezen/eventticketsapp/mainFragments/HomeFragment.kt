package com.sezen.eventticketsapp.mainFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sezen.eventticketsapp.R
import com.sezen.eventticketsapp.network.Event

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var musicAdapter: MainAdapter
    private lateinit var sportsAdapter: MainAdapter
    private lateinit var artsAdapter: MainAdapter
    private lateinit var musicRecyclerView: RecyclerView
    private lateinit var sportsRecyclerView: RecyclerView
    private lateinit var artsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progressBar)
        musicRecyclerView = view.findViewById(R.id.musicRv)
        sportsRecyclerView = view.findViewById(R.id.sportsRv)
        artsRecyclerView = view.findViewById(R.id.artsRv)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // I set up the RecyclerViews and Adapters
        musicRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        musicAdapter = MainAdapter(emptyList())
        musicRecyclerView.adapter = musicAdapter

        sportsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        sportsAdapter = MainAdapter(emptyList())
        sportsRecyclerView.adapter = sportsAdapter

        artsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        artsAdapter = MainAdapter(emptyList())
        artsRecyclerView.adapter = artsAdapter


        // OIn this part I observe the LiveData for each category
        viewModel.musicEventsLiveData.observe(viewLifecycleOwner) { state ->
            processEventResponse(state, musicAdapter)
        }

        viewModel.sportsEventsLiveData.observe(viewLifecycleOwner) { state ->
            processEventResponse(state, sportsAdapter)
        }

        viewModel.artsEventsLiveData.observe(viewLifecycleOwner) { state ->
            processEventResponse(state, artsAdapter)
        }

        // Fetch data for all categories
        fetchData()
    }

    private fun fetchData() {
        // Trigger the LiveData calls to fetch data for each category
        viewModel.musicEventsLiveData
        viewModel.sportsEventsLiveData
        viewModel.artsEventsLiveData
    }

    private fun processEventResponse(state: ScreenState<List<Event>?>, adapter: MainAdapter) {
        when (state) {
            is ScreenState.Loading -> {
                progressBar.visibility = View.VISIBLE
            }
            is ScreenState.Success -> {
                progressBar.visibility = View.GONE
                state.data?.let { events ->
                    adapter.setData(events)
                }
            }
            is ScreenState.Error -> {
                progressBar.visibility = View.GONE
                val view = progressBar.rootView
                Snackbar.make(view, state.message!!, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
