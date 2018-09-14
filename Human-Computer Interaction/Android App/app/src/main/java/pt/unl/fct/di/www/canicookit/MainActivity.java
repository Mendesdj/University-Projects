package pt.unl.fct.di.www.canicookit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindorks.placeholderview.InfinitePlaceHolderView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private InfinitePlaceHolderView mLoadMoreView;
    private SearchView searchView;
    private TextView empty;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        activity = "Home";
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);

        getLayoutInflater().inflate(R.layout.emptypage, contentFrameLayout);
        empty = (TextView) findViewById(R.id.emptyMain);
        empty.setVisibility(View.GONE);
        empty.setText("No Recipes Found,\nConsider Adjusting Your Search");


        FrameLayout buttonFrameLayout = (FrameLayout) findViewById(R.id.Button_frame);
        getLayoutInflater().inflate(R.layout.white_search_view, buttonFrameLayout);

        FrameLayout filterButtonLayout  = (FrameLayout) findViewById(R.id.extraButtonFrame);
        getLayoutInflater().inflate(R.layout.filter, filterButtonLayout);

        title = (TextView) findViewById(R.id.textTitle);
        title.setText(getString(R.string.TopForYou));

        findViewById(R.id.extraButtonFrame).setVisibility(View.GONE);
        findViewById(R.id.extraButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdvancedSearchActivity.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

            }
        });


        mLoadMoreView = (InfinitePlaceHolderView) findViewById(R.id.loadMoreView);
        mLoadMoreView.getBuilder().setLayoutManager(new GridLayoutManager(this.getApplicationContext(), 2));

        searchView = (SearchView) findViewById(R.id.mySearchView);

        setupView();
        setupSearchView(searchView);

        searchView.setOnSearchClickListener(new SearchView.OnClickListener(){
            @Override
            public void onClick(View v) {

                findViewById(R.id.extraButtonFrame).setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                setupView();
                findViewById(R.id.extraButtonFrame).setVisibility(View.GONE);
                return false;
            }
        });

    }

    @Override
    public void onResume() {

        super.onResume ();
        closeDrawer();

    }

    private void setupSearchView(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FilterView(query);
                //mLoadMoreView.getAdapter().notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FilterView(newText);
                //mLoadMoreView.getAdapter().notifyDataSetChanged();
                return false;
            }


        });

    }

    private void setupView() {

        mLoadMoreView.removeAllViews();

        List<InfiniteFeedInfo> feedList = new ArrayList<>();
        mLoadMoreView.setLoadMoreResolver(new LoadMoreView(mLoadMoreView, feedList));

        feedList = Utils.loadInfiniteFeeds(this.getApplicationContext());

        Log.d("DEBUG", "LoadMoreView.LOAD_VIEW_SET_COUNT " + LoadMoreView.LOAD_VIEW_SET_COUNT);
        for (int i = 0; i < feedList.size() && i < LoadMoreView.LOAD_VIEW_SET_COUNT ; i++) {
            mLoadMoreView.addView(new ItemView(this.getApplicationContext(), feedList.get(i)));
        }

        mLoadMoreView.setLoadMoreResolver(new LoadMoreView(mLoadMoreView, feedList));

        if (feedList.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
        }
        else {
            empty.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
        }

    }

    private void FilterView(String query) {

        mLoadMoreView.removeAllViews();

        if(query == null || query.equals("")  ) {
            setupView();
            return;
        }

        List<InfiniteFeedInfo> feedList = Utils.loadInfiniteFeeds(this.getApplicationContext(), query);
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
            empty.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
        }
        else {
            empty.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
        }


    }

}