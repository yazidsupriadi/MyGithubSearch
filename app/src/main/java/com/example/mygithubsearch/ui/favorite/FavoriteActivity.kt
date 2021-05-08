package com.example.mygithubsearch.ui.favorite

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubsearch.R
import com.example.mygithubsearch.databinding.ActivityFavoriteBinding
import com.example.mygithubsearch.adapter.FavoriteAdapter
import com.example.mygithubsearch.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.mygithubsearch.database.FavoriteHelper
import com.example.mygithubsearch.data.Favorite
import com.example.mygithubsearch.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter

    private lateinit var binding: ActivityFavoriteBinding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorites"
        binding.recycleViewFavorite.layoutManager = LinearLayoutManager(this)
        binding.recycleViewFavorite.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.recycleViewFavorite.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorites = list
            }
        }
    }



    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBarFavorite.visibility = View.VISIBLE
            val favoriteHelper = FavoriteHelper.getInstance(applicationContext)
            favoriteHelper.open()
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBarFavorite.visibility = View.INVISIBLE
            val favorites = deferredFavorites.await()
            if (favorites.size > 0) {
                adapter.listFavorites = favorites
            } else {
                adapter.listFavorites = ArrayList()
                showSnackbarMessage()
            }

            favoriteHelper.close()
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorites)
    }

    private fun showSnackbarMessage() {
        Toast.makeText(this, getString(R.string.add_favorite), Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadNotesAsync()
    }
}