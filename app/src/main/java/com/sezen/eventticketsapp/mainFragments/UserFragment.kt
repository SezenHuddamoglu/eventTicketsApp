package com.sezen.eventticketsapp.mainFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sezen.eventticketsapp.Login
import com.sezen.eventticketsapp.R


class UserFragment : Fragment(R.layout.fragment_user) {

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var userArrayList: ArrayList<Ticket>

    private val currentUserId: String? = FirebaseAuth.getInstance().currentUser?.uid

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRecyclerview = view.findViewById(R.id.previousTicketsRecyclerView)
        userArrayList = ArrayList()

        // RecyclerView için layout manager ve adapter ayarları
        val layoutManager = LinearLayoutManager(requireContext())
        userRecyclerview.layoutManager = layoutManager
        val adapter = PreviousTicketsAdapter(userArrayList)
        userRecyclerview.adapter = adapter

        getUserData()
        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), Login::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

    }

    private fun getUserData() {
        // Get database reference for the user's tickets
        dbref = FirebaseDatabase.getInstance().getReference("Users/$currentUserId/tickets")

        // Add event listener for data changes in the database
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the ArrayList before adding new data
                userArrayList.clear()
                // Iterate through the tickets in the snapshot and add them to the ArrayList
                for (ticketSnapshot in snapshot.children) {
                    val ticket = ticketSnapshot.getValue(Ticket::class.java)
                    ticket?.let { userArrayList.add(it) }
                }

                // Notify the adapter that the data has changed
                userRecyclerview.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("UserFragment", "Database error occurred: ${error.message}")
                Toast.makeText(requireContext(), "An error occurred: ${error.message}", Toast.LENGTH_SHORT).show()

            }
        })
        // Get current user's ID and email and display them in TextViews
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val currentUserId = it.uid
            val currentEmail = it.email

            val idTextView: TextView = view!!.findViewById(R.id.idTextView)
            idTextView.text = currentUserId

            val emailTextView: TextView = view!!.findViewById(R.id.emailTextView)
            emailTextView.text = currentEmail
        }
    }
}
