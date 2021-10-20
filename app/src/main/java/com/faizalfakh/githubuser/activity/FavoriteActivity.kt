package com.faizalfakh.githubuser.activity

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.faizalfakh.githubuser.R
import com.faizalfakh.githubuser.adapter.UserFavoriteAdapter
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.faizalfakh.githubuser.helper.MappingHelper
import com.faizalfakh.githubuser.helper.QueryHelper
import com.faizalfakh.githubuser.model.User
import com.faizalfakh.githubuser.model.UserFavorite
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: UserFavoriteAdapter
    private lateinit var dbHelper: QueryHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        dbHelper = QueryHelper.getInstance(applicationContext)
        dbHelper.open()

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
            override fun onItemClicked(data: UserFavorite) {
                val dataIntent = User(username = data.username, type = null,avatar_url = data.avatar_url)
                val detailIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_USER, dataIntent)
                startActivity(detailIntent)
            }
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
                no_user.visibility = View.VISIBLE
                rvUser.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }

    override fun onResume() {
        super.onResume()
        loadUserFavorite()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.dataFavorite)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        menu!!.findItem(R.id.favorite).isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings -> startActivity(Intent(this, SettingActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}