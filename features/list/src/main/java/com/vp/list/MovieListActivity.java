package com.vp.list;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MovieListActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    private static final String IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified";

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingActivityInjector;
    private SearchView searchView;
    private boolean searchViewExpanded = true;

    // Text in SearchView
    private String searchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new ListFragment(), ListFragment.TAG)
                    .commit();
        } else {
            searchViewExpanded = savedInstanceState.getBoolean(IS_SEARCH_VIEW_ICONIFIED);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        searchView = (SearchView) menuItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        searchView.setIconified(searchViewExpanded);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(ListFragment.TAG);
                listFragment.submitSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Apply saved query text
        if (searchText != null && !searchText.isEmpty()) {
            searchView.setQuery(searchText, false);
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView.isIconified());

        // Save current SearchView text
        if (searchView.getQuery() != null && searchView.getQuery().length() > 0) {
            outState.putString("SearchQuery", searchView.getQuery().toString());
        }
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String searchQueryStr = savedInstanceState.getString("SearchQuery");

        // Save in a variable, as the SearchView has not been initialized yet
        if (searchQueryStr != null && !searchQueryStr.isEmpty()) {
            searchText = searchQueryStr;
        } else {
            searchText = "";
        }
    }


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingActivityInjector;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(ListFragment.TAG);
            listFragment.refreshQuery(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
