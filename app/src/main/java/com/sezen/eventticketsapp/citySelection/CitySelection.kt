package com.sezen.eventticketsapp.citySelection


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sezen.eventticketsapp.R
import com.sezen.eventticketsapp.mainFragments.MainFragmentsActivity
import com.sezen.eventticketsapp.mainFragments.MainViewModel


class CitySelection : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var cityRecyclerView: RecyclerView
    private lateinit var okButton: Button
    private lateinit var cityList: List<String>
    private lateinit var adapter: CityAdapter
    private var selectedCity: String? = null
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_selection)

        //I get the city names to the cityList variable
        cityList = getCityList()

        // I created a adapter to the Recycler View
        adapter = CityAdapter(cityList, selectedCity) { city ->
            updateSelectedCity(city)
        }

        //I set the recycler view.
        cityRecyclerView = findViewById(R.id.cityRecyclerView)
        cityRecyclerView.adapter = adapter
        //I used grid layout to show 3 city item in a one row.
        cityRecyclerView.layoutManager = GridLayoutManager(this,3)

        //I defined other views
        searchEditText = findViewById(R.id.searchEditText)
        okButton = findViewById(R.id.okButton)

        //I defined that what happening when the user clicking to okButton
        okButton.setOnClickListener {
            if (!selectedCity.isNullOrEmpty()) {
                // It is a toast to say user which city he/she selected.
                mainViewModel.setCity(selectedCity!!)
                Toast.makeText(this, "Selected City: $selectedCity", Toast.LENGTH_SHORT).show()

                //I created a intent to redirect the user to the mainFragmentsActivity page
                val intent = Intent(this, MainFragmentsActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please choose a city", Toast.LENGTH_SHORT).show()
            }
        }
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        // I applied filtering to adapter when search text changes
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

    }

    private fun getCityList(): List<String> {
        //These are the examples I used to be able to create the layout.
        val cityList = mutableListOf<String>()
        cityList.add("New York")
        cityList.add("Los Angeles")
        cityList.add("Chicago")
        cityList.add("Houston")
        cityList.add("Phoenix")
        cityList.add("Philadelphia")
        cityList.add("San Antonio")
        cityList.add("San Diego")
        cityList.add("Dallas")
        cityList.add("San Jose")
        cityList.add("Austin")
        cityList.add("Jacksonville")
        cityList.add("Fort Worth")
        cityList.add("Columbus")
        cityList.add("Charlotte")
        cityList.add("San Francisco")
        cityList.add("Indianapolis")
        cityList.add("Seattle")
        cityList.add("Denver")
        cityList.add("Washington, D.C.")
        cityList.add("Boston")
        cityList.add("El Paso")
        cityList.add("Nashville")
        cityList.add("Detroit")
        cityList.add("Oklahoma City")
        cityList.add("Portland")
        cityList.add("Las Vegas")
        cityList.add("Memphis")
        cityList.add("Louisville")
        cityList.add("Baltimore")
        cityList.add("Milwaukee")

        return cityList
    }
    //This part updated the selectedcity and make enable the ok button we user clicked on a city
    private fun updateSelectedCity(city: String) {
        selectedCity = city
        adapter.setSelectedCity(city)
        okButton.isEnabled = true

    }
}
