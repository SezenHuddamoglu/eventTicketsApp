package com.sezen.eventticketsapp.mainFragments


import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import com.sezen.eventticketsapp.R
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sezen.eventticketsapp.network.Event
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var searchView: SearchView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var viewModel: SearchViewModel
    private lateinit var backgroundText: TextView

    private lateinit var sortDialog: AlertDialog
    private lateinit var filterDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views and adapters
        searchView = view.findViewById(R.id.searchView)
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        backgroundText=view.findViewById(R.id.backgroundText)

        searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        searchAdapter = SearchAdapter(emptyList())
        searchRecyclerView.adapter = searchAdapter

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        // Observe search results
        viewModel.searchResults.observe(viewLifecycleOwner, { state ->
            performSearch(state, searchAdapter)
        })

        // Set up SearchView listeners
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        // Click listener for Sort TextView
        val textViewSort: TextView = view.findViewById(R.id.textViewSort)
        textViewSort.setOnClickListener {
            showSortDialog()
        }

        // Click listener for Filter TextView
        val textViewFilter: TextView = view.findViewById(R.id.textViewFilter)
        textViewFilter.setOnClickListener {
            showFilterDialog()
        }
    }

    // Perform search based on the query
    private fun performSearch(query: String) {
        viewModel.searchEvents(query)
        backgroundText.visibility = View.GONE
    }

    // Update UI based on the search state
    private fun performSearch(state: ScreenState<List<Event>?>, adapter: SearchAdapter) {
        when (state) {
            is ScreenState.Loading -> {
                progressBar.visibility = View.VISIBLE
            }
            is ScreenState.Success -> {
                progressBar.visibility = View.GONE
                backgroundText.visibility = View.GONE
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

    // Show the dialog for sorting options
    private fun showSortDialog() {
        val options = arrayOf(
            "Alphabetical Order by Name",
            "Ascending Sort by Date",
            "Decreasing Sort by Date",
            "Ascending Order by Time",
            "Decreasing Sort by Time",
            "Ascending Sort by Price",
            "Decreasing Sort by Price"
        )

        var selectedOption = -1

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Sort by")
        builder.setSingleChoiceItems(options, -1) { dialog, which ->
            selectedOption = which
        }

        builder.setPositiveButton("Apply") { dialog, _ ->
            when (selectedOption) {
                0 -> {
                    //This function from SearchViewModel
                    viewModel.sortByAlphabetical()
                }
                1 -> {
                    viewModel.sortByDateAscending()
                }
                2 -> {
                    viewModel.sortByDateDescending()
                }
                3 -> {
                    viewModel.sortByTimeAscending()
                }
                4 -> {
                    viewModel.sortByTimeDescending()
                }
                5 -> {
                    viewModel.sortByPriceAscending()
                }
                6 -> {
                    viewModel.sortByPriceDescending()
                }
            }

            dialog.dismiss()
        }

        sortDialog = builder.create()
        sortDialog.show()
    }

    // Show the dialog for filtering options
    @SuppressLint("MissingInflatedId")
    private fun showFilterDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Filter")

        val dialogView = layoutInflater.inflate(R.layout.dialog_filter, null)
        builder.setView(dialogView)

        val applyButton = dialogView.findViewById<Button>(R.id.btnApply)
        val timeRangeLayout = dialogView.findViewById<LinearLayout>(R.id.layoutTimeRange)
        val dateRangeLayout = dialogView.findViewById<LinearLayout>(R.id.layoutDateRange)
        val priceRangeLayout = dialogView.findViewById<LinearLayout>(R.id.layoutPriceRange)

        val timeFromEditText = dialogView.findViewById<EditText>(R.id.etTimeFrom)
        val timeToEditText = dialogView.findViewById<EditText>(R.id.etTimeTo)
        val dateFromEditText = dialogView.findViewById<EditText>(R.id.etDateFrom)
        val dateToEditText = dialogView.findViewById<EditText>(R.id.etDateTo)
        val priceFromEditText = dialogView.findViewById<EditText>(R.id.etPriceFrom)
        val priceToEditText = dialogView.findViewById<EditText>(R.id.etPriceTo)

        val timeRangeCheckbox = dialogView.findViewById<CheckBox>(R.id.checkboxTimeRange)
        val dateRangeCheckbox = dialogView.findViewById<CheckBox>(R.id.checkboxDateRange)
        val priceRangeCheckbox = dialogView.findViewById<CheckBox>(R.id.checkboxPriceRange)

        timeRangeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            timeRangeLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        dateRangeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            dateRangeLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        priceRangeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            priceRangeLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        val dialog = builder.create() // Dialog oluşturuluyor

        applyButton.setOnClickListener {
            val timeFrom = timeFromEditText.text.toString()
            val timeTo = timeToEditText.text.toString()
            val dateFrom = dateFromEditText.text.toString()
            val dateTo = dateToEditText.text.toString()
            val priceFrom = priceFromEditText.text.toString().toDoubleOrNull()
            val priceTo = priceToEditText.text.toString().toDoubleOrNull()


            if (timeFrom.isNotEmpty() && timeTo.isNotEmpty()) {
                viewModel.filterByTimeRange(timeFrom, timeTo)
            }

            if (dateFrom.isNotEmpty() && dateTo.isNotEmpty()) {
                viewModel.filterByDateRange(dateFrom, dateTo)
            }

            if (priceFrom != null && priceTo != null) {
                viewModel.filterByPriceRange(priceFrom, priceTo)
            }

            dialog.dismiss()
        }

        dialog.show()
    }

    // Optional function for handling filter button clicks
    fun onFilterClick(view: View) {
        showFilterDialog()
    }

}
