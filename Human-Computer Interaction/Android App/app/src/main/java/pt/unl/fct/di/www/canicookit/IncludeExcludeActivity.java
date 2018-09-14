package pt.unl.fct.di.www.canicookit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.unl.fct.di.www.canicookit.adapters.CustomAdapterIngredientsIncExc;
import pt.unl.fct.di.www.canicookit.dataModels.ActiveFilterModel;

public class IncludeExcludeActivity extends AppCompatActivity {

    List<IngredientMain> ingredients;
    private SearchView searchView;
    private Spinner ingredientsCat;
    private ImageView accept;
    private ListView listView;
    private CustomAdapterIngredientsIncExc customAdapter;
    private List<ActiveFilterModel> list;
    private List<String> addedIngredients;
    private String currentQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_include_exclude );
        Toolbar toolbar = (Toolbar) findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );
        toolbar.setTitleTextColor ( Color.WHITE );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        ingredientsCat = (Spinner) findViewById ( R.id.idCatSpinner );
        listView = (ListView) findViewById ( R.id.idListViewIngredients );
        accept = (ImageView) findViewById ( R.id.idAcceptChanges );

        accept.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                returnResults ();

            }
        } );

        //Setup toolbar
        FrameLayout contentFrameLayout = (FrameLayout) findViewById ( R.id.content_frame ); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater ().inflate ( R.layout.activity_main, contentFrameLayout );
        FrameLayout buttonFrameLayout = (FrameLayout) findViewById ( R.id.Button_frame );
        getLayoutInflater ().inflate ( R.layout.white_search_view, buttonFrameLayout );
        FrameLayout filterButtonLayout = (FrameLayout) findViewById ( R.id.extraButtonFrame );
        getLayoutInflater ().inflate ( R.layout.filter, filterButtonLayout );
        findViewById ( R.id.extraButtonFrame ).setVisibility ( View.GONE );
        findViewById ( R.id.extraButton ).setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( getApplicationContext (), AdvancedSearchActivity.class );
                intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
                getApplicationContext ().startActivity ( intent );

            }
        } );

        searchView = (SearchView) findViewById ( R.id.mySearchView );

        searchView.setQueryHint("Search for an Ingredient");
        setupSearchView ( searchView );

        //On search and search txt change
        searchView.setOnSearchClickListener ( new SearchView.OnClickListener () {
            @Override
            public void onClick(View v) {
                getSupportActionBar ().setDisplayShowTitleEnabled ( false );
                //findViewById(R.id.titleTxt).setVisibility(View.GONE);
            }
        } );

        searchView.setOnCloseListener ( new SearchView.OnCloseListener () {
            @Override
            public boolean onClose() {
                getSupportActionBar ().setDisplayShowTitleEnabled ( true );
                // setupView();
                //findViewById(R.id.titleTxt).setVisibility(View.VISIBLE);
                return false;
            }
        } );
        addSpinnerItems ();
        ingredientsCat.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                fillListview ( currentQuery, ingredientsCat.getSelectedItem ().toString () );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

        list = new ArrayList<> ();
        addedIngredients = new ArrayList<> ();
        readExtras ();
        ingredients = Utils.loadIngredientsFromAsset ( getApplicationContext () );

        customAdapter = new CustomAdapterIngredientsIncExc ( getApplicationContext (), list );
        customAdapter.setAddedList ( addedIngredients );
        listView.setAdapter ( customAdapter );
        fillListview ( "", "All" );
    }

    private void readExtras() {
        if (getIntent ().getExtras () != null) {
            if (getIntent ().getExtras ().getInt ( "requestCode" ) == 1) {
                getSupportActionBar ().setTitle ( "Include ingredients" );
            } else {
                getSupportActionBar ().setTitle ( "Exclude ingredients" );
            }
            Type listType = new TypeToken<ArrayList<String>> () {
            }.getType ();
            addedIngredients.addAll ( (List<String>) new Gson ().fromJson ( getIntent ().getExtras ().getString ( "Added" ), listType ) );
        }
    }

    private void fillListview(String filter) {
        //updateAddedIng ();
        // initiate a ListView
        list.clear ();
        for (IngredientMain i : ingredients) {
            String tmp = ingredientsCat.getSelectedItem ().toString ();
            if (ingredientsCat.getSelectedItem ().toString ().equals ( "All" ) || i.getType ().toString ().equalsIgnoreCase ( ingredientsCat.getSelectedItem ().toString () )) {
                if (i.getName ().toLowerCase ().contains ( filter.toLowerCase () ))
                    list.add ( new ActiveFilterModel ( i.getName (), isIncluded ( i.getName () ), i.getImgUrl () ) );
            }
        }
        Collections.sort ( list );
        customAdapter.notifyDataSetChanged ();
    }

    private void fillListview(String filter, String cat) {
        //updateAddedIng ();
        // initiate a ListView
        list.clear ();
        for (IngredientMain i : ingredients) {
            String tmp = ingredientsCat.getSelectedItem ().toString ();
            if (cat.equals ( "All" ) || i.getType ().toString ().equalsIgnoreCase ( cat )) {
                if (i.getName ().toLowerCase ().contains ( filter.toLowerCase () ))
                    list.add ( new ActiveFilterModel ( i.getName (), isIncluded ( i.getName () ), i.getImgUrl () ) );

            }
        }
        Collections.sort ( list );
        customAdapter.notifyDataSetChanged ();
    }

    private void updateAddedIng() {
        for (int idx = 0; idx < listView.getCount (); idx++) {
            CheckedTextView txtV = (CheckedTextView) listView.getChildAt ( idx );
            if (txtV.isChecked ())
                addedIngredients.add ( txtV.getText ().toString () );
        }
    }

    private int getPositionOf(String ingName) {
        int count = 0;
        for (ActiveFilterModel ing : list) {
            if (ing.getName ().equalsIgnoreCase ( ingName ))
                return count;
        }
        return -1;
    }

    private boolean isIncluded(String ingName) {
        for (String ing : addedIngredients) {
            if (ing.equalsIgnoreCase ( ingName ))
                return true;
        }
        return false;
    }

    private void addSpinnerItems() {
        List<String> list = new ArrayList<> ();
        list.add ( "All" );
        for (IngredientType cat : IngredientType.values ()) {
            list.add ( cat.toString () );
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<> ( this,
                android.R.layout.simple_spinner_item, list );
        dataAdapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        ingredientsCat.setAdapter ( dataAdapter );
        ingredientsCat.setSelection ( 0 );

    }

    private void setupSearchView(SearchView searchView) {
        searchView.setOnQueryTextListener ( new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //FilterView(query);
                //mLoadMoreView.getAdapter().notifyDataSetChanged();
                currentQuery = query;
                fillListview ( query );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //FilterView(newText);
                //mLoadMoreView.getAdapter().notifyDataSetChanged();
                currentQuery = newText;
                fillListview ( newText );
                return false;
            }


        } );
    }

    private void returnResults() {
        Gson gson = new Gson ();
        Collections.sort ( addedIngredients );
        String data = gson.toJson ( addedIngredients );

        Intent returnIntent = new Intent ();
        returnIntent.putExtra ( "result", data );
        setResult ( Activity.RESULT_OK, returnIntent );
        finish ();
    }

    @Override
    public void onResume() {
        Collections.sort ( ingredients );
        customAdapter.notifyDataSetChanged ();
        super.onResume ();
    }


}
