package pt.unl.fct.di.www.canicookit.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pt.unl.fct.di.www.canicookit.AdvancedSearchActivity;
import pt.unl.fct.di.www.canicookit.DietEditActivity;
import pt.unl.fct.di.www.canicookit.R;
import pt.unl.fct.di.www.canicookit.dataModels.DietListItemModel;

public class CustomAdapterDietList extends ArrayAdapter<DietListItemModel> implements View.OnClickListener {
    ArrayList<DietListItemModel> dietsArray;

    public CustomAdapterDietList(Context context, ArrayList<DietListItemModel> diets) {
        super ( context, R.layout.diet_row_item, diets );
        this.dietsArray = diets;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext ().getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
        View row = inflater.inflate ( R.layout.diet_row_item, parent, false );

        ImageView myImage = (ImageView) row.findViewById ( R.id.idItemEdit );
        TextView myName = (TextView) row.findViewById ( R.id.idName );

        myImage.setOnClickListener ( this );
        myImage.setTag ( dietsArray.get ( position ).getName () );

        myName.setText ( dietsArray.get ( position ).getName () );
        return row;
    }

    @Override
    public void onClick(View v) {
        String dName = (String) v.getTag ();
        switch (v.getId ()) {
            case R.id.idItemEdit:
                Intent intent = new Intent ( v.getContext (), DietEditActivity.class );
                intent.putExtra ( "DietName", dName );
                intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
                v.getContext ().startActivity ( intent );
                break;
        }
    }
}
