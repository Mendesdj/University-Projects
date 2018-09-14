package pt.unl.fct.di.www.canicookit;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.placeholderview.InfinitePlaceHolderView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends BaseActivity {

    private InfinitePlaceHolderView mLoadMoreView;
    private SearchView searchView;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        activity = "Favourites";
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);

        getLayoutInflater().inflate(R.layout.emptypage, contentFrameLayout);
        empty = (TextView) findViewById(R.id.emptyMain);
        empty.setVisibility(View.GONE);

        FrameLayout buttonFrameLayout = (FrameLayout) findViewById(R.id.Button_frame);
        getLayoutInflater().inflate(R.layout.white_search_view, buttonFrameLayout);

        mLoadMoreView = (InfinitePlaceHolderView) findViewById(R.id.loadMoreView);
        mLoadMoreView.getBuilder().setLayoutManager(new GridLayoutManager(this.getApplicationContext(), 2));

        searchView = (SearchView) findViewById(R.id.mySearchView);

        setupView();
        setupSearchView(searchView);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                setupView();
                return false;
            }
        });

        getSupportActionBar().setTitle("My Favourites");

        TextView ActivityTitle = (TextView) findViewById(R.id.textTitle);
        ActivityTitle.setVisibility(View.GONE);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        String query = searchView.getQuery().toString();
        if (query == null || query == "")
            setupView();
        else
            FilterView(query);

        closeDrawer();
    }

    private void setupSearchView(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FilterView(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FilterView(newText);
                return false;
            }


        });

    }

    private void setupView() {

        mLoadMoreView.removeAllViews();

        List<InfiniteFeedInfo> feedList = new ArrayList<>();
        mLoadMoreView.setLoadMoreResolver(new LoadMoreView(mLoadMoreView, feedList));

        feedList = Utils.loadInfiniteFavorites(this.getApplicationContext());

        Log.d("DEBUG", "LoadMoreView.LOAD_VIEW_SET_COUNT " + LoadMoreView.LOAD_VIEW_SET_COUNT);
        for (int i = 0; i < feedList.size() && i < LoadMoreView.LOAD_VIEW_SET_COUNT ; i++) {
            mLoadMoreView.addView(new ItemView(this.getApplicationContext(), feedList.get(i)));
        }

        mLoadMoreView.setLoadMoreResolver(new LoadMoreView(mLoadMoreView, feedList));

        if (feedList.size() == 0) {
            empty.setText("No Recipes Found,\nConsider Adding Recipes To Your Favorites");
            empty.setVisibility(View.VISIBLE);
        }
        else {
            empty.setVisibility(View.GONE);
        }

    }

    private void FilterView(String query) {

        mLoadMoreView.removeAllViews();

        if(query == null || query.equals("")  ) {
            setupView();
            return;
        }

        List<InfiniteFeedInfo> feedList = Utils.loadInfiniteFavorites(this.getApplicationContext(), query);
        mLoadMoreView.setLoadMoreResolver(new LoadMoreView(mLoadMoreView, feedList));

        Log.d("DEBUG", "LoadMoreView.LOAD_VIEW_SET_COUNT " + mLoadMoreView.getViewCount());

        for (int i = 0; i < feedList.size() &&  i < LoadMoreView.LOAD_VIEW_SET_COUNT; i++) {
            mLoadMoreView.addView(new ItemView(this.getApplicationContext(), feedList.get(i)));
        }

        if (mLoadMoreView.getViewCount() + 1 >= feedList.size() ){
            mLoadMoreView.noMoreToLoad();
            mLoadMoreView.loadingDone();
        }
        else
            mLoadMoreView.setLoadMoreResolver(new LoadMoreView(mLoadMoreView, feedList));

        if (feedList.size() == 0) {
            empty.setText("No Recipes Found,\nConsider Adjusting Your Search");
            empty.setVisibility(View.VISIBLE);
        }
        else {
            empty.setVisibility(View.GONE);
        }

    }

}