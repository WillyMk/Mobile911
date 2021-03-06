package com.WillyFisky.mobile911

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {
    private lateinit var requestRecyclerViewAdapter: RequestRecyclerViewAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var request: Request

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val rcView = findViewById<RecyclerView>(R.id.rcView)

        database = FirebaseDatabase.getInstance()
        databaseRef = database.reference


        //Handling recyclerview
        requestRecyclerViewAdapter = RequestRecyclerViewAdapter(mutableListOf())
        rcView.layoutManager = LinearLayoutManager(this@AdminActivity)
        rcView.adapter = requestRecyclerViewAdapter
        //readData(databaseRef)

        databaseRef.child("Data").addValueEventListener(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue(Request::class.java)!!
                        request = Request(value.request.toString(), value.location.toString())
                        requestRecyclerViewAdapter.addRequestFunction(request)
                        println(value.toString())
                        println(value.request)
                        println(value.location)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        //Toast.makeText(this@AdminActivity, "Failed to load data from database", Toast.LENGTH_SHORT).show()
                    }
                }
        )

    }

    private fun readData(databaseReference: DatabaseReference){
        databaseReference.child("Data").addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(Request::class.java)!!
                    request = Request(value.request.toString(), value.location.toString())
                    requestRecyclerViewAdapter.addRequestFunction(request)
                    println(value.toString())
                    println(value.request)
                    println(value.location)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AdminActivity, "Failed to load data from database", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}