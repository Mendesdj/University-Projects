package pt.unl.fct.di.www.canicookit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import at.markushi.ui.CircleButton;
import pt.unl.fct.di.www.canicookit.adapters.customAdapterActiveDietFilters;
import pt.unl.fct.di.www.canicookit.dataModels.ActiveFilterModel;

import static pt.unl.fct.di.www.canicookit.Utils.loadIngredientsFromAsset;
import static pt.unl.fct.di.www.canicookit.Utils.loadInventory;

/**
 * Created by MendesPC && ricardoesteves on 10/11/2017.
 */

public class AdvancedSearchActivity extends BaseActivity {

    private boolean addToInventory;
    private List<ActiveFilterModel> includeIngredients;
    private List<ActiveFilterModel> excludeIngredients;
    private ArrayList<ActiveFilterModel> dataModels;
    private customAdapterActiveDietFilters adapter;

    public void onCreate(Bundle savedInstanceState) {

        activity = "Advanced_Search";
        super.onCreate ( savedInstanceState );

        includeIngredients = new ArrayList<> ();
        excludeIngredients = new ArrayList<> ();
        dataModels = new ArrayList<> ();
        addToInventory = false;

        FrameLayout contentFrameLayout = (FrameLayout) findViewById ( R.id.content_frame ); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater ().inflate ( R.layout.advanced_search, contentFrameLayout );

        getSupportActionBar ().setTitle ( R.string.advanced_search );

        // Spinner Creation

        // Get reference of Spinner from XML layout
        final Spinner spinner = (Spinner) findViewById ( R.id.diets_spinner );

        // REPLACE BY JSON INTERPRETATION
        List<Diet> diets = Utils.loadDietsFromFile ( getApplicationContext () );

        List<String> dietsList = new ArrayList<> ();
        dietsList.add ( "Select diet..." );
        for (Diet d : diets) {
            dietsList.add ( d.getName () );
        }

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String> (
                this, R.layout.spinner_item, dietsList ) {
            @Override
            public boolean isEnabled(int position) {
                    return true;

            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView ( position, convertView, parent );
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor ( Color.GRAY );
                } else {
                    tv.setTextColor ( Color.BLACK );
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource ( R.layout.spinner_item );
        spinner.setAdapter ( spinnerArrayAdapter );

        spinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition ( position );
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text

                    Toast.makeText
                            ( getApplicationContext (), "Selected : " + selectedItemText, Toast.LENGTH_SHORT )
                            .show ();


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

        ImageView info = (ImageView) findViewById ( R.id.imageInfo );
        info.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder ( AdvancedSearchActivity.this ).create ();
                alertDialog.setTitle ( "Info" );
                alertDialog.setMessage ( "By selecting this option, only ingredients available in your Inventory will be used in the search " );
                alertDialog.setButton ( AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener () {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss ();
                            }
                        } );
                alertDialog.show ();
                TextView messageText = (TextView) alertDialog.findViewById ( android.R.id.message );
                messageText.setGravity ( Gravity.CENTER );

            }

        } );

        //Time SeekBar

        DiscreteSeekBar discreteMultiple = (DiscreteSeekBar) findViewById ( R.id.seekBarTime );
        final TextView seekbarTime = (TextView) findViewById ( R.id.currentTime );

        List<String> values = Arrays.asList ( "Any", "20 Mins.", "45 Mins.", "2 Hours", "Slow Cooker" );
        initMultipleOfSeekBar ( discreteMultiple, seekbarTime, values );

        //Difficulty SeekBar

        DiscreteSeekBar discreteMultipleDifficulty = (DiscreteSeekBar) findViewById ( R.id.seekBarDifficulty );
        final TextView seekbarDifficulty = (TextView) findViewById ( R.id.currentDifficulty );

        List<String> valuesDifficulty = Arrays.asList ( "Any", "Not too Tricky", "Showing Off" );
        initMultipleOfSeekBar ( discreteMultipleDifficulty, seekbarDifficulty, valuesDifficulty );

        //Protein SeekBar

        DiscreteSeekBar discreteMultipleProtein = (DiscreteSeekBar) findViewById ( R.id.seekBarProtein );
        final TextView seekbarProtein = (TextView) findViewById ( R.id.currentProtein );

        List<String> valuesProtein = Arrays.asList ( "Any", "10 g", "20 g", "30 g", "40 g", "50 g", "High Protein" );
        initMultipleOfSeekBar ( discreteMultipleProtein, seekbarProtein, valuesProtein );

        //Calories SeekBar

        DiscreteSeekBar discreteMultipleCalories = (DiscreteSeekBar) findViewById ( R.id.seekBarCalories );
        final TextView seekbarCalories = (TextView) findViewById ( R.id.currentCalories );

        List<String> valuesCalories = Arrays.asList ( "Any", "100 g", "200 g", "300 g", "400 g", "500 g", "High Calories" );
        initMultipleOfSeekBar ( discreteMultipleCalories, seekbarCalories, valuesCalories );

        //Fat SeekBar

        DiscreteSeekBar discreteMultipleFat = (DiscreteSeekBar) findViewById ( R.id.seekBarFat );
        final TextView seekbarFat = (TextView) findViewById ( R.id.currentFat );

        List<String> valuesFat = Arrays.asList ( "Any", "10 g", "20 g", "30 g", "40 g", "50 g", "High Fat" );
        initMultipleOfSeekBar ( discreteMultipleFat, seekbarFat, valuesFat );

        FloatingActionButton add = (FloatingActionButton) findViewById ( R.id.advancedInclude );
        add.setOnClickListener ( new View.OnClickListener () {
            public void onClick(View v) {
                Intent i = new Intent ( getApplicationContext (), IncludeExcludeActivity.class );
                readyIntent ( i, 1 );
                startActivityForResult ( i, 1 );
            }
        } );

        FloatingActionButton exc = (FloatingActionButton) findViewById ( R.id.advancedExclude );
        exc.setOnClickListener ( new View.OnClickListener () {
            public void onClick(View v) {
                Intent i = new Intent ( getApplicationContext (), IncludeExcludeActivity.class );
                readyIntent ( i, 2 );
                startActivityForResult ( i, 2 );
            }
        } );

        CircleButton search = (CircleButton) findViewById ( R.id.buttonSearch );
        search.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                Intent i = new Intent ( AdvancedSearchActivity.this, AdvancedSearchResultActivity.class );

                String[] included = new String[includeIngredients.size ()];
                for (int j = 0; j < includeIngredients.size (); j++) {
                    included[j] = includeIngredients.get ( j ).getName ();

                }

                i.putExtra ( "ingredientsInc", included );

                CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);

                String[] excluded;

                if (checkbox.isChecked()){

                    List<String> inventory = loadInventory(getApplicationContext());
                    List<IngredientMain> allIngredients = loadIngredientsFromAsset(getApplicationContext());

                    List<String> addToExclude = new ArrayList<>();

                    for (IngredientMain ing : allIngredients) {
                        boolean found = false;
                        for (String s : inventory) {
                            if (s.trim().toLowerCase().equals(ing.getName().trim().toLowerCase())){
                                found = true;
                                break;
                            }
                        }
                        if (!found )
                            addToExclude.add(ing.getName());
                    }

                    Log.d("Ingredients To Exclude1" , String.valueOf(excludeIngredients.size()));
                    Log.d("Ingredients To Exclude:" , String.valueOf(addToExclude.size()));

                    excluded = new String[excludeIngredients.size() + addToExclude.size()];

                    Log.d("TotalSize" , String.valueOf(excluded.length));
                    for (int j = 0; j < excludeIngredients.size (); j++) {
                        excluded[j] = excludeIngredients.get ( j ).getName ();
                    }
                    for (int p = excludeIngredients.size() , k = 0; p < excluded.length; p++ ,k++) {
                        excluded[p] = addToExclude.get(k);
                    }

                }
                else {
                    excluded = new String[excludeIngredients.size ()];
                    for (int j = 0; j < excludeIngredients.size (); j++) {
                        excluded[j] = excludeIngredients.get ( j ).getName ();
                    }
                }

                i.putExtra ( "ingredientsExc", excluded );

                startActivity ( i );
            }
        } );

        dataModels = new ArrayList<> ();
        adapter = new customAdapterActiveDietFilters ( getApplicationContext (), dataModels );
        updateListView ();
        //Listview set up
        ListView lv = (ListView) findViewById ( R.id.activeFilters );
        lv.setEmptyView ( findViewById ( R.id.empty ) );

        lv.setAdapter ( adapter );
        lv.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ActiveFilterModel dataModel = dataModels.get ( position );
                if (dataModel.getIncluded ())
                    Snackbar.make ( view, "Included " + dataModel.getName (), Snackbar.LENGTH_LONG )
                            .setAction ( "No action", null ).show ();
                else
                    Snackbar.make ( view, "Excluded " + dataModel.getName (), Snackbar.LENGTH_LONG )
                            .setAction ( "No action", null ).show ();
            }
        } );

    }

    private void fillSecondaryLists() {
        includeIngredients.clear ();
        excludeIngredients.clear ();
        for (ActiveFilterModel ing : dataModels) {
            if (ing.getIncluded ())
                includeIngredients.add ( ing );
            else
                excludeIngredients.add ( ing );
        }
    }

    private void updateListView() {

        fillSecondaryLists ();
        adapter.notifyDataSetChanged ();
    }

    private void readyIntent(Intent i, int requestCode) {
        i.putExtra ( "requestCode", requestCode );
        List<String> alreadyAddedItems = new ArrayList<> ();
        if (requestCode == 1) {
            for (ActiveFilterModel ing : dataModels) {
                if (ing.getIncluded ())
                    alreadyAddedItems.add ( ing.getName () );
            }
        } else {
            for (ActiveFilterModel ing : dataModels) {
                if (!ing.getIncluded ())
                    alreadyAddedItems.add ( ing.getName () );
            }
        }

        Gson gson = new Gson ();
        String data = gson.toJson ( alreadyAddedItems );
        i.putExtra ( "Added", data );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null)
            return;

        List<String> ret;
        Type listType = new TypeToken<ArrayList<String>> () {
        }.getType ();
        int pos;
        ActiveFilterModel a;
        ret = new Gson ().fromJson ( data.getStringExtra ( "result" ), listType );
        //Corre quando esta a devolver incluidos
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ret = new Gson ().fromJson ( data.getStringExtra ( "result" ), listType );
                Iterator<ActiveFilterModel> it = dataModels.iterator ();
                while (it.hasNext ()) {
                    a = it.next ();
                    if (a.getIncluded ())
                        it.remove ();
                }
                for (String newIng : ret) {
                    pos = getPos ( newIng );
                    if (pos >= 0)
                        dataModels.remove ( pos );
                    dataModels.add ( new ActiveFilterModel ( newIng, true ) );
                }
            }

        }
        //Corre quando esta a devolver excluidos
        else {
            if (resultCode == RESULT_OK) {

                Iterator<ActiveFilterModel> it = dataModels.iterator ();
                while (it.hasNext ()) {
                    a = it.next ();
                    if (!a.getIncluded ())
                        it.remove ();
                }

                for (String newIng : ret) {
                    pos = getPos ( newIng );
                    if (pos >= 0)
                        dataModels.remove ( pos );
                    dataModels.add ( new ActiveFilterModel ( newIng, false ) );
                }
            }
        }
        order ();
        updateListView ();
    }

    private int getPos(String name) {
        for (int idx = 0; idx < dataModels.size (); idx++) {
            if (dataModels.get ( idx ).getName ().equalsIgnoreCase ( name ))
                return idx;
        }
        return -1;
    }

    private void order() {
        ArrayList<ActiveFilterModel> inc = new ArrayList<> ();
        ArrayList<ActiveFilterModel> exc = new ArrayList<> ();
        for (ActiveFilterModel f : dataModels) {
            if (f.getIncluded ())
                inc.add ( f );
            else
                exc.add ( f );
        }
        dataModels.clear ();
        dataModels.addAll ( inc );
        dataModels.addAll ( exc );
    }
    //SeekBars

    private void initMultipleOfSeekBar(DiscreteSeekBar discreteSeekBar, final TextView seekbarTime, final List<String> values) {

        discreteSeekBar.setMin ( 0 );
        discreteSeekBar.setMax ( values.size () - 1 );

        discreteSeekBar.setProgress ( 0 );
        seekbarTime.setText ( values.get ( 0 ) );

        discreteSeekBar.setNumericTransformer ( new DiscreteSeekBar.NumericTransformer () {

            @Override
            public int transform(int value) {
                return 0;
            }

            @Override
            public String transformToString(int value) {
                return "";
            }

            @Override
            public boolean useStringTransform() {
                return true;
            }

        } );

        discreteSeekBar.setOnProgressChangeListener ( new DiscreteSeekBar.OnProgressChangeListener () {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

                seekbarTime.setText ( values.get ( value ) );
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        } );
    }

    @Override
    public void onResume() {

        super.onResume ();
        closeDrawer ();

    }

}










