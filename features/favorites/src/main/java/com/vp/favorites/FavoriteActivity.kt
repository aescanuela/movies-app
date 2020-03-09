package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.common.database.DatabaseHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class FavoriteActivity : AppCompatActivity(), ListAdapter.OnItemClickListener {


    private var listAdapter: ListAdapter? = null


    companion object {
        private val TAG = FavoriteActivity::class.java.simpleName
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        initList()
        showProgressBar()
        doAsync {
            val favorites = DatabaseHelper.getAllFavorites(this@FavoriteActivity)
            uiThread {
                // Log.d(TAG, fav2.toString())
                when {
                    favorites == null -> {
                        showError()
                    }
                    favorites.isEmpty() -> {
                        showError()
                        errorText.text = getString(R.string.empty_favorites)
                    }
                    else -> {
                        listAdapter!!.setItems(favorites)
                        showList()
                    }
                }
            }
        }
    }


    private fun initList() {
        listAdapter = ListAdapter()
        listAdapter!!.setOnItemClickListener(this)
        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this@FavoriteActivity,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView.layoutManager = layoutManager
    }


    private fun showProgressBar() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(recyclerView)
    }

    private fun showError() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(errorText)
    }


    override fun onItemClick(imdbID: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("app://movies/detail?imdbID=$imdbID")
        startActivity(intent)
    }

}