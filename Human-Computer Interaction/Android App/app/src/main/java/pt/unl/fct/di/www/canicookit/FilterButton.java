package pt.unl.fct.di.www.canicookit;

import android.content.Context;
import android.content.Intent;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;


/**
 * Created by ricardoesteves on 14/11/17.
 */

@Layout(R.layout.filter)
public class FilterButton {

    private Context mContext;

    public FilterButton (Context context) {

        mContext = context;
    }

    @Resolve
    private void onResolved(){

    }

    @Click(R.id.extraButton)
    private void onClick()  {
        Intent intent;
        intent = new Intent(mContext, AdvancedSearchActivity.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

}