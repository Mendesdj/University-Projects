package pt.unl.fct.di.www.canicookit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

/**
 * Created by MendesPC on 23/11/2017.
 */

public class NumberPickerDialog extends DialogFragment {
    private NumberPicker.OnValueChangeListener valueChangeListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle b = getArguments();
        int s = b.getInt("serves");

        LayoutInflater inflater = getActivity ().getLayoutInflater ();
        View dialogView = inflater.inflate ( R.layout.dialog_numberpicker, null );

        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById ( R.id.idServingsPicker );

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(15);
        numberPicker.setValue(s);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.YourDialogStyle);
        builder.setTitle("Choose Nº of Servings");


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());
            }
        });

        builder.setView(dialogView);

        return builder.create();
    }

    public NumberPicker.OnValueChangeListener getValueChangeListener() {
        return valueChangeListener;
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
}