package com.vp.detail

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vp.common.database.DatabaseHelper
import com.vp.common.model.FavoriteMovie
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.model.MovieDetail
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var menu: Menu? = null

    private var movieDetail: MovieDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        val detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        queryProvider = this
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails()
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        detailViewModel.details().observe(this, Observer {
            movieDetail = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        this.menu = menu
        return true
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        doAsync {
            if (DatabaseHelper.isFavorite(this@DetailActivity, getMovieId())) {
                uiThread {
                    menu?.findItem(R.id.star)?.icon = ContextCompat.getDrawable(this@DetailActivity, R.drawable.ic_star_filled)
                }
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.star -> {
                Log.i(TAG, "Favorites button pressed")
                if (movieDetail != null) {
                    val dbFavoriteMovie = FavoriteMovie(getMovieId(), movieDetail!!.title, movieDetail!!.year,
                            movieDetail!!.runtime, movieDetail!!.director, movieDetail!!.plot, movieDetail!!.poster)
                    doAsync {
                        DatabaseHelper.addOrRemoveFavorite(this@DetailActivity, dbFavoriteMovie)
                        uiThread {
                            invalidateOptionsMenu()
                        }
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        lateinit var queryProvider: QueryProvider

        private val TAG = DetailActivity::class.java.simpleName

    }
}
