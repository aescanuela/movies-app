package com.vp.list.viewmodel;

import android.util.Log;

import com.vp.list.model.ListItem;
import com.vp.list.model.SearchResponse;
import com.vp.list.service.SearchService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends ViewModel {

    private static final String TAG = ListViewModel.class.getSimpleName();


    private MutableLiveData<SearchResult> liveData = new MutableLiveData<>();
    private SearchService searchService;

    private String currentTitle = "";
    private List<ListItem> aggregatedItems = new ArrayList<>();

    @Inject
    ListViewModel(@NonNull SearchService searchService) {
        this.searchService = searchService;
    }

    public LiveData<SearchResult> observeMovies() {
        return liveData;
    }

    public void searchMoviesByTitle(@NonNull String title, int page) {

        if (page == 1 && !title.equals(currentTitle)) {
            aggregatedItems.clear();
            currentTitle = title;
            liveData.setValue(SearchResult.inProgress());
        }
        searchService.search(title, page).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {

                if (response.isSuccessful()) {
                    SearchResponse result = response.body();
                    if (result != null) {
                        aggregatedItems.addAll(result.getSearch());
                        liveData.setValue(SearchResult.success(aggregatedItems, aggregatedItems.size()));
                    }
                } else {
                    try {
                        Log.e(TAG, "Error in search: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    liveData.setValue(SearchResult.error());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {

                t.printStackTrace();
                liveData.setValue(SearchResult.error());
            }
        });
    }
}
