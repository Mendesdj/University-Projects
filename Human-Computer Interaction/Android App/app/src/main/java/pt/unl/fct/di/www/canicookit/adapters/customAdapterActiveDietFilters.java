package pt.unl.fct.di.www.canicookit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.unl.fct.di.www.canicookit.dataModels.ActiveFilterModel;
import pt.unl.fct.di.www.canicookit.R;

public class customAdapterActiveDietFilters extends ArrayAdapter<ActiveFilterModel> implements View.OnClickListener {
    ArrayList<ActiveFilterModel> dietsArray;

    public customAdapterActiveDietFilters(Context context, ArrayList<ActiveFilterModel> diets) {
        super ( context, R.layout.diet_active_filter_row_item, diets );
        this.dietsArray = diets;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext ().getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
        View row = inflater.inflate ( R.layout.diet_active_filter_row_item, parent, false );

        ImageView myImageType = (ImageView) row.findViewById ( R.id.idTypeFilter );
        TextView myName = (TextView) row.findViewById ( R.id.idName );
        ImageView myImage = (ImageView) row.findViewById ( R.id.idItemRemove );

        myImage.setOnClickListener ( this );
        myImage.setTag ( position );

        myName.setText ( dietsArray.get ( position ).getName () );
        if (dietsArray.get ( position ).getIncluded ())
            myImageType.setImageResource ( R.drawable.ic_add_black_18dp );
        else
            myImageType.setImageResource ( R.drawable.ic_minus_black_18dp );

        return row;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag ();
        switch (v.getId ()) {
            case R.id.idItemRemove:
                dietsArray.remove ( position );
                notifyDataSetChanged ();
                break;
        }
    }
}
