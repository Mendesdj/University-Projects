package pt.unl.fct.di.www.canicookit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Iterator;
import java.util.List;

import pt.unl.fct.di.www.canicookit.R;
import pt.unl.fct.di.www.canicookit.dataModels.ActiveFilterModel;


public class CustomAdapterIngredientsIncExc extends ArrayAdapter<ActiveFilterModel> {
    List<ActiveFilterModel> ingredients;
    private List<String> addedList;

    public CustomAdapterIngredientsIncExc(Context context, List<ActiveFilterModel> ingredients) {
        super ( context, R.layout.ingredient_include_exclude_row_item, ingredients );
        this.ingredients = ingredients;
    }

    public void setAddedList(List<String> addedList) {
        this.addedList = addedList;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflter = (LayoutInflater.from ( getContext () ));
        view = inflter.inflate ( R.layout.ingredient_include_exclude_row_item, null );
        final CheckedTextView simpleCheckedTextView = (CheckedTextView) view.findViewById ( R.id.idCheckedTextView );
        ImageView ingredientImage = (ImageView) view.findViewById ( R.id.idIngredientImage );

        Glide.with(getContext ()).load(ingredients.get ( position ).getUrl ()).into(ingredientImage);

        simpleCheckedTextView.setText ( ingredients.get ( position ).getName () );
        if (ingredients.get ( position ).getIncluded ()) {
            simpleCheckedTextView.setCheckMarkDrawable ( R.drawable.ic_check_circle_black_18dp );
            simpleCheckedTextView.setChecked ( true );

        } else {
            simpleCheckedTextView.setCheckMarkDrawable ( 0 );
            simpleCheckedTextView.setChecked ( false );

        }

// perform on Click Event Listener on CheckedTextView
        simpleCheckedTextView.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (simpleCheckedTextView.isChecked ()) {
// set cheek mark drawable and set checked property to false
                    simpleCheckedTextView.setCheckMarkDrawable ( 0 );
                    simpleCheckedTextView.setChecked ( false );
                    ingredients.get ( position ).setIsIncluded ( false );

                    Iterator<String> it = addedList.iterator ();
                    while (it.hasNext ()) {
                        String str = (String) it.next ();
                        if (str.equalsIgnoreCase ( simpleCheckedTextView.getText ().toString () ))
                            it.remove ();
                    }

                } else {
// set cheek mark drawable and set checked property to true
                    simpleCheckedTextView.setCheckMarkDrawable ( R.drawable.ic_check_circle_black_18dp );
                    simpleCheckedTextView.setChecked ( true );
                    ingredients.get ( position ).setIsIncluded ( true );
                    addedList.add ( simpleCheckedTextView.getText ().toString () );
                }
            }
        } );
        return view;
    }

    private int getPositionOf(String ingName) {
        int count = 0;
        for (ActiveFilterModel ing : ingredients) {
            if (ing.getName ().equalsIgnoreCase ( ingName ))
                return count;
        }
        return -1;
    }

}

