package pt.unl.fct.di.www.canicookit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import java.util.List;

import pt.unl.fct.di.www.canicookit.adapters.CustomAdapterShoppingList;
import pt.unl.fct.di.www.canicookit.dataModels.ShoppingListModel;

public class ShoppingListDialogFragment extends DialogFragment {
    List<ShoppingListModel> ingredients;
    int position;
    private CustomAdapterShoppingList adapter;


    public void setDataModels(List<ShoppingListModel> dataModels) {
        this.ingredients = dataModels;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder ( getActivity (),R.style.YourDialogStyle );

        LayoutInflater inflater = getActivity ().getLayoutInflater ();
        View dialogView = inflater.inflate ( R.layout.dialog_shopping_list_item_edit, null );


        final NumberPicker picker = (NumberPicker) dialogView.findViewById ( R.id.idUnitsPicker );
        picker.setMinValue ( 0 );
        picker.setMaxValue ( UnitMeasures.values ().length - 1 );
        String[] asd = UnitMeasures.getNames ( UnitMeasures.class );
        picker.setDisplayedValues ( asd );
        picker.setFormatter ( new NumberPicker.Formatter () {
            @Override
            public String format(int value) {
                return UnitMeasures.values ()[value].toString ();
            }
        } );

        String unit = ingredients.get ( position ).getIng ().getUnitsMeasure ();
        int count = 0;
        for (UnitMeasures s : UnitMeasures.values ()) {
            if (s.toString ().equalsIgnoreCase ( unit )) {
                picker.setValue ( count );
                break;
            }
            count++;
        }

        final EditText edittext = (EditText) dialogView.findViewById ( R.id.idQuantity );
        edittext.setText ( String.valueOf ( ingredients.get ( position ).getQuantity () ) );

        final ImageView upArrow = (ImageView) dialogView.findViewById ( R.id.idAddOne );
        upArrow.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                int x = Integer.parseInt ( edittext.getText ().toString () );
                edittext.setText ( String.valueOf ( x + 1 ) );
            }
        } );


        final ImageView downArrow = (ImageView) dialogView.findViewById ( R.id.idTakeOne );
        downArrow.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                int x = Integer.parseInt ( edittext.getText ().toString () );
                if (x > 0)
                    edittext.setText ( String.valueOf ( x - 1 ) );

            }
        } );

        builder.setView ( dialogView )
                .setTitle ( "Edit ingredient" )
                .setPositiveButton ( "SAVE", new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String tmp = edittext.getText ().toString ();
                                if (tmp.equalsIgnoreCase ( "" ))
                                    ingredients.get ( position ).setQuantity ( 0 );
                                else
                                    ingredients.get ( position ).setQuantity ( Integer.parseInt ( tmp ) );
                                ingredients.get ( position ).getIng ().setUnitsMeasure ( picker.getDisplayedValues ()[picker.getValue ()].toString () );
                                adapter.notifyDataSetChanged ();
                            }
                        }
                )
                .setNegativeButton ( "CANCEL", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                } );

        return builder.create ();
    }

    public void setAdapter(CustomAdapterShoppingList adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onResume() {
        int width = (int) TypedValue.applyDimension ( TypedValue.COMPLEX_UNIT_DIP, 250, getContext ().getResources ().getDisplayMetrics () );
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog ().getWindow ().setLayout ( width, height );

        super.onResume ();
    }
}
