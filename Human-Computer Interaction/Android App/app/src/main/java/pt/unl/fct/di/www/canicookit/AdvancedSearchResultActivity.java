package pt.unl.fct.di.www.canicookit;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mindorks.placeholderview.InfinitePlaceHolderView;

import java.util.ArrayList;
import java.util.List;

public class AdvancedSearchResultActivity extends BaseActivity {

    private InfinitePlaceHolderView mLoadMoreView;
    private String[] includedIngredients;
    private String[] excludedIngredients;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        includedIngredients = getIntent().getStringArrayExtra("ingredientsInc");
        excludedIngredients = getIntent().getStringArrayExtra("ingredientsExc");

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);

        getLayoutInflater().inflate(R.layout.emptypage, contentFrameLayout);
        empty = (TextView) findViewById(R.id.emptyMain);
        empty.setVisibility(View.GONE);
        empty.setText("No Recipes Found,\nConsider Adjusting Your Search");

        mLoadMoreView = (InfinitePlaceHolderView) findViewById(R.id.loadMoreView);
        mLoadMoreView.getBuilder().setLayoutManager(new GridLayoutManager(this.getApplicationContext(), 2));

        setupView();

        getSupportActionBar().setTitle("Results");

        TextView ActivityTitle = (TextView) findViewById(R.id.textTitle);
        ActivityTitle.setVisibility(View.GONE);



    }

    @Override
    public void onResume()
    {
        super.onResume();
        closeDrawer();
    }


    private void setupView() {

        mLoadMoreView.removeAllViews();

        List<InfiniteFeedInfo> feedList = new ArrayList<>();
        mLoadMoreView.setLoadMoreResolver(new LoadMoreView(mLoadMoreView, feedList));

        feedList = Utils.loadRecipeWithIngredientsList(getApplicationContext(), excludedIngredients, includedIngredients);

        Log.d("DEBUG", "LoadMoreView.LOAD_VIEW_SET_COUNT " + LoadMoreView.LOAD_VIEW_SET_COUNT);
        for (int i = 0; i < feedList.size() && i < LoadMoreView.LOAD_VIEW_SET_COUNT ; i++) {
            mLoadMoreView.addView(new ItemView(this.getApplicationContext(), feedList.get(i)));
        }

        mLoadMoreView.setLoadMoreResolver(new LoadMoreView(mLoadMoreView, feedList));

        if (feedList.size() == 0) {
            empty.setVisibility(View.VISIBLE);
        }
        else {
            empty.setVisibility(View.GONE);
        }

    }

}