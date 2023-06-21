package com.sezen.eventticketsapp.mainFragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import coil.load
import com.sezen.eventticketsapp.network.ApiClient.apiService
import com.sezen.eventticketsapp.network.Event
import com.sezen.eventticketsapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class EventDetail : AppCompatActivity() {
    private lateinit var event: Event

    private lateinit var eventName: TextView
    private lateinit var eventTime: TextView
    private lateinit var eventDate: TextView
    private lateinit var eventPlace: TextView
    private lateinit var imageOfPoster: ImageView

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        repository = Repository(apiService)

        eventName = findViewById(R.id.eventName)
        eventTime = findViewById(R.id.eventTime)
        eventDate = findViewById(R.id.eventDate)
        eventPlace = findViewById(R.id.eventPlace)
        imageOfPoster = findViewById(R.id.imageOfPoster)

        //I got the event id to get the properties of the clicked event.
        val eventId = intent.getStringExtra("eventId")

        coroutineScope.launch {
            try {
                val response = eventId?.let { repository.getEventDetails(it) }
                if (response != null) {
                    event = response
                    updateUI()
                } else {
                    Toast.makeText(
                        this@EventDetail,
                        "ERROR: response returned null",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@EventDetail,
                    "ERROR: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
           // When the buy a ticket button is clicked, the actions required to use some features in the ticket selection
            val intent = Intent(this, TicketSelection::class.java)
            intent.putExtra("eventName", eventName.text.toString())
            intent.putExtra("eventTime", eventTime.text.toString())
            intent.putExtra("eventDate", eventDate.text.toString())
            intent.putExtra("eventPlace", eventPlace.text.toString())

            //I transferred the event poster to the ticket selection in this way.
            val drawable = imageOfPoster.drawable as? BitmapDrawable
            val bitmap = drawable?.bitmap

            bitmap?.let {
                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                intent.putExtra("eventPoster", byteArray)
            }


            startActivity(intent)
        }

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun updateUI() {
        if (event == null) {
            Toast.makeText(
                this@EventDetail,
                "ERROR: response returned null",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (::event.isInitialized) {
            // ImageView için görseli ayarlayın
            val imageOfPoster = findViewById<ImageView>(R.id.imageOfPoster)
            imageOfPoster.load(event.images[0].url) {
                target(imageOfPoster)
            }

            //I placed the values I got from the api in the layout

            eventName.text = event.name
            eventPlace.text = event?._embedded?.venues?.get(0)?.name ?: null
            eventDate.text = event.dates?.start?.localDate
            eventTime.text = event.dates?.start?.localTime
            val eventAddress = findViewById<TextView>(R.id.eventAdress)

            eventAddress.text = "${event._embedded?.venues?.get(0)?.address?.line1 } /" +
                    " ${event._embedded?.venues?.get(0)?.city?.name} /${event._embedded?.venues?.get(0)?.state?.name }"

            val segment = findViewById<TextView>(R.id.genre1)
            segment.text = event.classifications?.get(0)?.segment?.name

            val genre = findViewById<TextView>(R.id.genre2)
            genre.text = event.classifications?.get(0)?.genre?.name


            //I checked if the values are null, if the values are null then these text views are not shown to the user.
            if(event.promoter?.name !=null){
                val eventInfo = findViewById<TextView>(R.id.eventText)
                eventInfo.visibility= View.VISIBLE
                eventInfo.text = "Promoter:  \n"+ event.promoter?.name +"\n" +event.promoter?.description
            }

            if(event._embedded?.venues?.get(0)?.generalInfo?.generalRule != null){
                val generalInfo = findViewById<TextView>(R.id.summary)
                generalInfo.visibility= View.VISIBLE
                generalInfo.text = "Rules:  \n"+ event._embedded?.venues?.get(0)?.generalInfo?.generalRule
            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel() // cancelling the coroutine scope
    }

    //The function required to return to the previous page when clicking the back icon in the toolbar
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}


