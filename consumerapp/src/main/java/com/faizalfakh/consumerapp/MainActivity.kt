package com.faizalfakh.consumerapp

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.faizalfakh.consumerapp.adapter.UserFavoriteAdapter
import com.faizalfakh.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.faizalfakh.consumerapp.helper.MappingHelper
import com.faizalfakh.consumerapp.model.UserFavorite
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserFavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvUser.layoutManager = LinearLayoutManager(this)
        rvUser.setHasFixedSize(true)
        adapter = UserFavoriteAdapter()
        rvUser.adapter = adapter

        if (savedInstanceState == null) {
            loadUserFavorite()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserFavorite>(EXTRA_STATE) as ArrayList<UserFavorite>
            adapter.dataFavorite = list
        }

        val handleThread = HandlerThread("DataObserver")
        handleThread.start()
        val handler = Handler(handleThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUserFavorite()
            }
        }

        loadUserFavorite()

        adapter.setOnItemClickCallback(object : UserFavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserFavorite) { }
        })

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

    }

    private fun loadUserFavorite() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favData = deferredFav.await()
            if (favData.size > 0) {
                adapter.dataFavorite = favData

            } else {
                Toast.makeText(applicationContext, "Kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        loadUserFavorite()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.dataFavorite)
    }

}