package pt.unl.fct.di.www.canicookit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

/**
 * Created by ricardoesteves on 09/11/17.
 */



@Layout(R.layout.load_more_item_view_empty)
public class ItemViewEmpty {

    @View(R.id.emptyText)
    private TextView emptyText;

    private Context mContext;
    private String text;

    public ItemViewEmpty(Context context, String text) {

        mContext = context;
        this.text = text;
    }

    @Resolve
    private void onResolved() {
        emptyText.setText(text);
    }

    @Click(R.id.Card)
    private void onClick()  {

    }

}