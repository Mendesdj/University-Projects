package pt.unl.fct.di.www.canicookit;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.mindorks.placeholderview.InfinitePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.infinite.LoadMore;

import java.util.List;

/**
 * Created by ricardoesteves on 09/11/17.
 */

@Layout(R.layout.load_more_view)
public class LoadMoreView {

    public static final int LOAD_VIEW_SET_COUNT = 6;

    private InfinitePlaceHolderView mLoadMoreView;
    private List<InfiniteFeedInfo> mFeedList;

    public LoadMoreView(InfinitePlaceHolderView loadMoreView, List<InfiniteFeedInfo> feedList) {
        this.mLoadMoreView = loadMoreView;
        this.mFeedList = feedList;
    }

    @LoadMore
    private void onLoadMore(){
        Log.d("DEBUG", "onLoadMore");
        new ForcedWaitedLoading();
    }

    class ForcedWaitedLoading implements Runnable{

        public ForcedWaitedLoading() {
            new Thread(this).start();
        }

        @Override
        public void run() {

            try {
                Thread.currentThread().sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    int actualViewCount;
                    if ( mLoadMoreView.getViewCount() != -1)
                        actualViewCount = mLoadMoreView.getViewCount();
                    else
                        actualViewCount = 0;


                    int count = actualViewCount;
                    Log.d("DEBUG", "count " + count);
                    for (int i = count ;
                         i < (count + LoadMoreView.LOAD_VIEW_SET_COUNT) ;
                         i++) {

                        Log.d("If condition", "count " + (mFeedList.size() - i));
                        Log.d("If condition", "FeedListSize " + mFeedList.size());

                        if(mFeedList.size() - i > 0) {
                            mLoadMoreView.addView(new ItemView(mLoadMoreView.getContext(), mFeedList.get(i)));
                        }

                        if(i >= mFeedList.size() ){
                            mLoadMoreView.noMoreToLoad();
                            break;
                        }
                    }
                    mLoadMoreView.loadingDone();
                }
            });
        }
    }
}