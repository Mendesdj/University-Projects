package pt.unl.fct.di.www.canicookit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

/**
 * Created by ricardoesteves on 09/11/17.
 */

@Layout(R.layout.load_more_item_view)
public class ItemView {

    @View(R.id.titleTxt)
    private TextView titleTxt;

    @View(R.id.numberLikes)
    private TextView numberLikes;

    @View(R.id.time)
    private TextView timeTxt;

    @View(R.id.imageView)
    private ImageView imageView;

    @View(R.id.Card)
    private CardView card;

    private InfiniteFeedInfo mInfo;
    private Context mContext;

    public ItemView(Context context, InfiniteFeedInfo info) {

        mContext = context;
        mInfo = info;
    }

    @Resolve
    private void onResolved() {

        titleTxt.setText(mInfo.getTitle());
        numberLikes.setText(mInfo.getLikes());
        timeTxt.setText(mInfo.getTime());
        Glide.with(mContext).load(mInfo.getImageUrl()).into(imageView);
    }

    @Click(R.id.Card)
    private void onClick()  {
        Intent intent;
        intent = new Intent(mContext, RecipeShow.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(mContext.getString(R.string.RecipeId),mInfo.getTitle());
        mContext.startActivity(intent);
    }

}