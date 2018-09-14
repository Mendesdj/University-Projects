package pt.unl.fct.di.www.canicookit;

/**
 * Created by MendesPC on 24/11/2017.
 */

import android.support.v7.widget.CardView;

public interface CardAdapter {

    public final int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
