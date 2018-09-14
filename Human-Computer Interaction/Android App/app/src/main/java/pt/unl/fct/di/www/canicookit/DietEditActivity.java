package pt.unl.fct.di.www.canicookit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import pt.unl.fct.di.www.canicookit.adapters.customAdapterActiveDietFilters;
import pt.unl.fct.di.www.canicookit.dataModels.ActiveFilterModel;

public class DietEditActivity extends AppCompatActivity {

    static String dietName;
    static String txtDietName;
    private static customAdapterActiveDietFilters adapter;
    ArrayList<ActiveFilterModel> dataModels;
    List<Diet> diets;
    TextView txtViewDietName;
    ImageView imgViewCheckmark;
    private ImageView imgViewInclude;
    private ImageView imgViewExclude;
    private ImageView imgViewDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_diet_edit );
        Toolbar toolbar = (Toolbar) findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );
        FrameLayout buttonFrameLayout = (FrameLayout) findViewById ( R.id.Button_frame );
        getLayoutInflater ().inflate ( R.layout.white_delete_view, buttonFrameLayout );

        txtViewDietName = (TextView) findViewById ( R.id.idDietName );
        imgViewCheckmark = (ImageView) findViewById ( R.id.idAcceptChanges );
        imgViewInclude = (ImageView) findViewById ( R.id.idFloatingInclude );
        imgViewExclude = (ImageView) findViewById ( R.id.idFloatingExclude );
        imgViewDelete = (ImageView) findViewById ( R.id.idDeleteImageView );

        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        if (getIntent ().getExtras () != null)
            dietName = getIntent ().getExtras ().getString ( "DietName" );


        imgViewDelete.setOnClickListener ( new View.OnClickListener () {
            public void onClick(View v) {

                new AlertDialog.Builder ( DietEditActivity.this )
                        .setTitle ( "Confirmation" )
                        .setMessage ( "Your diet will be permanently deleted!" )
                        .setIcon ( R.drawable.ic_dialog_alert_black )
                        .setPositiveButton ( android.R.string.yes, new DialogInterface.OnClickListener () {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                updateDiet ( txtViewDietName.getText ().toString (), true );
                                finish ();
                            }
                        } )
                        .setNegativeButton ( android.R.string.no, null ).show ();

            }
        } );
        imgViewCheckmark.setOnClickListener ( new View.OnClickListener () {
            public void onClick(View v) {
                updateDiet ( txtViewDietName.getText ().toString (), false );
                if (!txtViewDietName.getText ().toString ().equals ( "" )) {
                    Toast.makeText ( getBaseContext (), "Diet updated", Toast.LENGTH_SHORT ).show ();
                    new Handler ().postDelayed ( new Runnable () {
                        @Override
                        public void run() {
                            finish ();
                        }
                    }, 500 );
                }
            }
        } );
        imgViewInclude.setOnClickListener ( new View.OnClickListener () {
            public void onClick(View v) {
                Intent i = new Intent ( getApplicationContext (), IncludeExcludeActivity.class );

                readyIntent ( i, 1 );
                startActivityForResult ( i, 1 );
            }
        } );
        imgViewExclude.setOnClickListener ( new View.OnClickListener () {
            public void onClick(View v) {
                Intent i = new Intent ( getApplicationContext (), IncludeExcludeActivity.class );

                readyIntent ( i, 2 );
                startActivityForResult ( i, 2 );
            }
        } );

        if (dietName != null)
            txtViewDietName.setText ( dietName );
        dataModels = new ArrayList<> ();
        adapter = new customAdapterActiveDietFilters ( getApplicationContext (), dataModels );
        updateListView ();
        //Listview set up
        ListView lv = (ListView) findViewById ( R.id.activeFilters );

        RelativeLayout rl = (RelativeLayout) findViewById ( R.id.relativeLayout2 );
        View empty = getLayoutInflater ().inflate ( R.layout.empty_view, null, false );
        rl.addView ( empty, new FrameLayout.LayoutParams ( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ) );
        lv.setEmptyView ( empty );

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

    private void readyIntent(Intent i, int requestCode) {
        i.putExtra ( "requestCode", requestCode );
        List<String> alreadyAddedItems = new ArrayList<> ();
        if (requestCode == 1)
            for (ActiveFilterModel ing : dataModels) {
                if (ing.getIncluded ())
                    alreadyAddedItems.add ( ing.getName () );
            }
        else
            for (ActiveFilterModel ing : dataModels) {
                if (!ing.getIncluded ())
                    alreadyAddedItems.add ( ing.getName () );
            }
        Gson gson = new Gson ();
        String data = gson.toJson ( alreadyAddedItems );
        i.putExtra ( "Added", data );
    }

    private void updateListView() {
        diets = Utils.loadDietsFromFile ( getApplicationContext () );
        Collections.sort ( diets );
        dataModels.clear ();
        if (dietName != null) {
            for (Diet d : diets) {
                if (d.getName ().equals ( dietName )) {
                    List<IngredientMain> inc = d.getIncludedIngredients ();
                    List<IngredientMain> exc = d.getExcludedIngredients ();
                    for (IngredientMain ing : inc) {
                        dataModels.add ( new ActiveFilterModel ( ing.getName (), true ) );
                    }
                    for (IngredientMain ing : exc) {
                        dataModels.add ( new ActiveFilterModel ( ing.getName (), false ) );
                    }
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged ();
    }

    private void updateDiet(String title, Boolean delete) {
        if (title.equals ( "" ) && !delete) {
            Toast.makeText ( getApplicationContext (), "Please add a title", Toast.LENGTH_SHORT ).show ();
            return;
        }
        TextView dietNameUpdated = (TextView) findViewById ( R.id.idDietName );
        List<Diet> diets = Utils.loadDietsFromFile ( getApplicationContext () );

        if (dietName != "")
            for (int idx = 0; idx < diets.size (); idx++) {
                if (diets.get ( idx ).getName ().equals ( dietName )) {
                    diets.remove ( idx );
                    idx--;
                }
            }
        if (!delete) {
            Diet dietToAdd = new Diet ( dietNameUpdated.getText ().toString () );
            ListView lv = (ListView) findViewById ( R.id.activeFilters );
            IngredientMain ing;
            for (int idx = 0; idx < lv.getCount (); idx++) {
                ActiveFilterModel model = (ActiveFilterModel) lv.getItemAtPosition ( idx );
                ing = getIngredient ( model.getName () );
                if (model.getIncluded ())
                    dietToAdd.inludeIngredient ( getIngredient ( model.getName () ) );
                else
                    dietToAdd.excludeIngredient ( getIngredient ( model.getName () ) );
            }
            diets.add ( dietToAdd );
            dietName = dietToAdd.getName ();
        }
        Utils.saveDietsToFile ( getApplicationContext (), diets );
    }

    private IngredientMain getIngredient(String name) {
        List<IngredientMain> listIngre = Utils.loadIngredientsFromAsset ( getApplicationContext () );
        for (IngredientMain ing : listIngre) {
            if (ing.getName ().equalsIgnoreCase ( name ))
                return ing;
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null)
            return;

        List<String> ret;
        int pos;
        Type listType = new TypeToken<ArrayList<String>> () {
        }.getType ();
        ret = new Gson ().fromJson ( data.getStringExtra ( "result" ), listType );
        Collections.sort ( ret );
        ActiveFilterModel a;
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
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
        } else if (requestCode == 2) {
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
        adapter.notifyDataSetChanged ();
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current name
        savedInstanceState.putString ( "dietName", txtViewDietName.getText ().toString () );
        txtDietName = txtViewDietName.getText ().toString ();
        super.onSaveInstanceState ( savedInstanceState );
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState ( savedInstanceState );
        txtDietName = savedInstanceState.getString ( "dietName" );
        updateListView ();
    }

    private int getPos(String name) {
        for (int idx = 0; idx < dataModels.size (); idx++) {
            if (dataModels.get ( idx ).getName ().equalsIgnoreCase ( name ))
                return idx;
        }
        return -1;
    }

    @Override
    public void onResume() {
        super.onResume ();
        if (getIntent ().getExtras () == null)
            txtViewDietName.setText ( txtDietName );
        adapter.notifyDataSetChanged ();
    }
}
