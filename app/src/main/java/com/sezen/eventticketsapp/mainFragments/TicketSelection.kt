package com.sezen.eventticketsapp.mainFragments


import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.sezen.eventticketsapp.R
import org.json.JSONObject
import java.util.*


class TicketSelection : AppCompatActivity(), PaymentResultListener{
    private lateinit var categorySpinner: Spinner
    private lateinit var selectedTicketsTextView: TextView
    private lateinit var selectedTicketsListView: ListView
    private lateinit var continueButton: Button
    lateinit var success: TextView
    lateinit var failed: TextView
    lateinit var scrollView: ScrollView
    var totalPrice = 0.0
    private lateinit var database : DatabaseReference
    private lateinit var eventName: String
    private lateinit var eventDate: String

    // List to store all available tickets
    private val tickets = mutableListOf<Ticket>()

    // List of ticket categories
    private val ticketCategories = listOf(
        TicketCategory("", 0.0), // Added an empty category for the initial time
        TicketCategory(" Premium", 350.0),
        TicketCategory("Category A", 300.0),
        TicketCategory("Category B", 250.0),
        TicketCategory("Category C", 200.0),
        TicketCategory("Category D", 150.0),
        TicketCategory("Category E", 100.0),
        TicketCategory("Category F", 50.0)
    )
    // List to store selected tickets
    private val selectedTickets = mutableListOf<SelectedTicket>()

    // Adapter for displaying selected tickets in a ListView
    private lateinit var selectedTicketsAdapter: SelectTicketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_selection)

        // Retrieve event details from intent
        val intent = intent
        val eventName = intent.getStringExtra("eventName")
        val eventTime = intent.getStringExtra("eventTime")
        val eventDate = intent.getStringExtra("eventDate")
        val eventPlace = intent.getStringExtra("eventPlace")
        val eventPosterByteArray = intent.getByteArrayExtra("eventPoster")

        // Initialize UI elements
        val eventCard = findViewById<CardView>(R.id.eventCard)
        val eventPosterImageView = findViewById<ImageView>(R.id.eventPosterImageView)
        val eventNameTextView = findViewById<TextView>(R.id.eventNameTextView)
        val eventPlaceTextView = findViewById<TextView>(R.id.eventPlaceTextView)
        val eventTimeTextView = findViewById<TextView>(R.id.eventTimeTextView)
        val eventDateTextView = findViewById<TextView>(R.id.eventDateTextView)


        eventNameTextView.text = eventName
        eventPlaceTextView.text = eventPlace
        eventTimeTextView.text = eventTime
        eventDateTextView.text = eventDate

        // Set event poster image if available
        if (eventPosterByteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(eventPosterByteArray, 0, eventPosterByteArray.size)
            eventPosterImageView.setImageBitmap(bitmap)
        }

        categorySpinner = findViewById(R.id.categorySpinner)
        selectedTicketsTextView = findViewById(R.id.selectedTicketsTextView)
        selectedTicketsListView = findViewById(R.id.selectedTicketsListView)
        continueButton = findViewById(R.id.continueButton)

        // Set up the adapter for the selected tickets list view
        selectedTicketsAdapter = SelectTicketAdapter(this, selectedTickets)
        selectedTicketsListView.adapter = selectedTicketsAdapter

        setupTicketCategoriesSpinner()
        setupSelectTicketButton()
        setupContinueButton()
        setupRemoveTicketListener()

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }
    // Set up the spinner for ticket categories
    private fun setupTicketCategoriesSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ticketCategories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = parent?.getItemAtPosition(position) as? TicketCategory
                selectedCategory?.let {
                    val priceText = "Total Price: ${calculateTotalPrice()} $"
                    selectedTicketsTextView.text = priceText
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedTicketsTextView.text = ""
            }
        }
    }
    // Set up the listener for the "Select Ticket" button
    private fun setupSelectTicketButton() {
        val selectTicketButton: Button = findViewById(R.id.selectTicketButton)
        selectTicketButton.setOnClickListener {
            val selectedCategory = categorySpinner.selectedItem as? TicketCategory

            if (selectedCategory != null && selectedCategory.name != "") {
                val selectedTicketsCount = selectedTickets.count { it.category == selectedCategory }

                if (selectedTicketsCount < 5) {
                    val newTicket = SelectedTicket(selectedCategory, 1)
                    selectedTicketsAdapter.addTicket(newTicket)
                    updateContinueButton()
                    updateSelectedTicketsTextView()
                } else {
                    Toast.makeText(this, "You can choose up to 5 tickets.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun setupContinueButton() {
        continueButton.setOnClickListener {
            if (selectedTickets.size >= 1 && selectedTickets.size <= 5) {
                intent.putExtra("totalPrice", calculateTotalPrice())
                makePayment()
            } else {
                Toast.makeText(this,
                        "Please select a minimum of 1 and a maximum of 5 tickets.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupRemoveTicketListener() {
        selectedTicketsListView.setOnItemClickListener { _, _, position, _ ->
            showRemoveTicketDialog(position)
        }
    }
    // Show a dialog to confirm ticket cancellation
    private fun showRemoveTicketDialog(position: Int) {
        val ticket = selectedTickets[position]
        val message = "${ticket.quantity} adet ${ticket.category.name} - ${ticket.category.price * ticket.quantity} TL biletini iptal etmek istiyor musunuz?"

        AlertDialog.Builder(this)
            .setTitle("Ticket Cancellation")
            .setMessage(message)
            .setPositiveButton("Yes") { _, _ ->
                removeSelectedTicket(position)
            }
            .setNegativeButton("No", null)
            .show()
    }


  private fun removeSelectedTicket(position: Int) {
      if (position >= 0 && position < selectedTickets.size) {
          selectedTickets.removeAt(position)
          updateSelectedTicketsList()
          updateContinueButton()
          updateSelectedTicketsTextView()
      }
  }


    private fun updateSelectedTicketsList() {
        selectedTicketsAdapter.notifyDataSetChanged()
    }

    private fun updateContinueButton() {
        continueButton.isEnabled = selectedTickets.isNotEmpty()
    }

    private fun updateSelectedTicketsTextView() {
        val priceText = "Toplam Ücret: ${calculateTotalPrice()} TL"
        selectedTicketsTextView.text = priceText
    }

    private fun calculateTotalPrice(): Double {
        var totalPrice = 0.0
        for (ticket in selectedTickets) {
            totalPrice += ticket.category.price * ticket.quantity
        }
        return totalPrice
    }

    // Make a payment using the selected tickets
    //I used RazerPay for the payment
    private fun makePayment() {
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name", "Event Ticket App")
            options.put("description", "Payment")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            //set the amount of price
            options.put("amount", (calculateTotalPrice() * 100).toString())

            val prefill = JSONObject()
            prefill.put("email", "example@mail.com")
            prefill.put("contact", "+902345678999")

            options.put("prefill", prefill)


            co.open(this, options)

        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

   override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Error $p1", Toast.LENGTH_LONG).show()

       continueButton = findViewById(R.id.continueButton)
        continueButton.visibility = View.GONE
        scrollView = findViewById(R.id.scrollView)
        scrollView.visibility = View.GONE
        failed = findViewById(R.id.failed)
        failed.visibility = View.VISIBLE
    }

  //If payment is successful the add the tickets to database
  //I used Firebase RealTime Database
  //I added tickets with userId
  override fun onPaymentSuccess(paymentId: String?) {
      try {
          Toast.makeText(this, "Payment is Successful $paymentId ", Toast.LENGTH_LONG).show()
          continueButton = findViewById(R.id.continueButton)
          continueButton.visibility = View.GONE
          scrollView = findViewById(R.id.scrollView)
          scrollView.visibility = View.GONE
          success = findViewById(R.id.success)
          success.visibility = View.VISIBLE
          eventName = intent.getStringExtra("eventName").toString()
          eventDate = intent.getStringExtra("eventDate").toString()
          database = FirebaseDatabase.getInstance().getReference("Users")

          val currentuser = FirebaseAuth.getInstance().currentUser
          val userId = currentuser?.uid

          val currentDate = Date()

          val tickets = selectedTickets.map { SelectedTicket(it.category, it.quantity) }
          val ticketList = tickets.flatMap { List(it.quantity) { _ -> it.category } }
          val ticketDocuments = ticketList.map { Ticket(eventName, eventDate, currentDate.toString(), false, userId ?: "") }

          if (userId != null) {
              val userRef = database.child(userId)
              userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                  override fun onDataChange(snapshot: DataSnapshot) {
                      if (snapshot.exists()) {
                          val existingUser = snapshot.getValue(User::class.java)
                          existingUser?.let {
                              val updatedTickets = it.tickets.toMutableList()
                              updatedTickets.addAll(ticketDocuments)
                              userRef.child("tickets").setValue(updatedTickets)
                                  .addOnSuccessListener {
                                      Toast.makeText(this@TicketSelection, "Successfully Saved", Toast.LENGTH_SHORT).show()
                                  }
                                  .addOnFailureListener {
                                      Toast.makeText(this@TicketSelection, "Failed", Toast.LENGTH_SHORT).show()
                                  }
                          }
                      } else {
                          userRef.setValue(User(userId ?: "", ticketDocuments))
                              .addOnSuccessListener {
                                  Toast.makeText(this@TicketSelection, "Successfully Saved", Toast.LENGTH_SHORT).show()
                              }
                              .addOnFailureListener {
                                  Toast.makeText(this@TicketSelection, "Failed", Toast.LENGTH_SHORT).show()
                              }
                      }
                  }

                  override fun onCancelled(error: DatabaseError) {
                      Toast.makeText(this@TicketSelection, "Failed: ${error.message}", Toast.LENGTH_SHORT).show()
                  }
              })
          }
      } catch (e: Exception) {
          Toast.makeText(this, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
      }
  }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()

    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
