package pt.unl.fct.di.www.canicookit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import at.markushi.ui.CircleButton;
import pt.unl.fct.di.www.canicookit.adapters.MultiSelectAdapter;
import pt.unl.fct.di.www.canicookit.dataModels.SampleModel;
import pt.unl.fct.di.www.canicookit.dataModels.ShoppingListModel;

import static pt.unl.fct.di.www.canicookit.Utils.createListFromIngredientsStrings;

/**
 * Created by MendesPC && ricardoesteves on 10/11/2017.
 */

public class MyInventoryActivity extends BaseActivity implements AlertDialogHelper.AlertDialogListener {

    RecyclerView recyclerView;
    TextView emptyView;
    MultiSelectAdapter multiSelectAdapter;

    ArrayList<SampleModel> ingredients_list = new ArrayList<> ();
    ArrayList<SampleModel> multiselect_list = new ArrayList<> ();

    AlertDialogHelper alertDialogHelper;

    public void onCreate(Bundle savedInstanceState) {

        activity = "Inventory";
        super.onCreate ( savedInstanceState );

        FrameLayout contentFrameLayout = (FrameLayout) findViewById ( R.id.content_frame ); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater ().inflate ( R.layout.my_inventory, contentFrameLayout );

        getSupportActionBar ().setTitle ( "My Inventory" );

        CircleButton add = (CircleButton) findViewById ( R.id.buttonAddInventory );

        add.setOnClickListener ( new View.OnClickListener () {
            public void onClick(View v) {
                Intent i = new Intent ( getApplicationContext (), IncludeExcludeActivity.class );
                readyIntent ( i, 1 );
                startActivityForResult ( i, 1 );
            }
        } );

        CircleButton qr = (CircleButton) findViewById ( R.id.buttonQR );

        qr.setOnClickListener ( new View.OnClickListener () {
            public void onClick(View v) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission ( MyInventoryActivity.this,
                        Manifest.permission.CAMERA )
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions ( MyInventoryActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            5 );

                } else {
                    Intent i = new Intent ( MyInventoryActivity.this, cameraActivity.class );
                    startActivity ( i );
                }
            }
        } );

        alertDialogHelper = new AlertDialogHelper ( this );

        recyclerView = (RecyclerView) findViewById ( R.id.recycler_view );
        emptyView = (TextView) findViewById(R.id.empty_view);

        data_load ();

        multiSelectAdapter = new MultiSelectAdapter ( this, ingredients_list, multiselect_list );
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager ( this, 3 );
        ;
        recyclerView.setLayoutManager ( mLayoutManager );
        recyclerView.setItemAnimator ( new DefaultItemAnimator () );
        recyclerView.setAdapter ( multiSelectAdapter );

        recyclerView.addOnItemTouchListener ( new RecyclerItemClickListener ( this, recyclerView, new RecyclerItemClickListener.OnItemClickListener () {
            @Override
            public void onItemClick(View view, int position) {

                multi_select ( position );

                if (multiselect_list.size() != 0){

                FrameLayout filterButtonLayout = (FrameLayout) findViewById ( R.id.extraButtonFrame );
                getLayoutInflater ().inflate ( R.layout.menu_common_activity, filterButtonLayout );

                filterButtonLayout.findViewById ( R.id.action_delete ).setOnClickListener ( new View.OnClickListener () {
                        @Override
                        public void onClick(View v) {alertDialogHelper.showAlertDialog ( "", "Are you sure you want to remove these ingredients from your inventory ?", "Delete", "Cancel", 1, false );
                        }
                    } );
                }

                else {
                    FrameLayout filterButtonLayout = (FrameLayout) findViewById ( R.id.extraButtonFrame );
                    filterButtonLayout.removeAllViews ();
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

                multi_select ( position );

                if (multiselect_list.size() != 0){

                    FrameLayout filterButtonLayout = (FrameLayout) findViewById ( R.id.extraButtonFrame );
                    getLayoutInflater ().inflate ( R.layout.menu_common_activity, filterButtonLayout );

                    filterButtonLayout.findViewById ( R.id.action_delete ).setOnClickListener ( new View.OnClickListener () {
                        @Override
                        public void onClick(View v) {alertDialogHelper.showAlertDialog ( "", "Are you sure you want to remove these ingredients from your inventory ?", "Delete", "Cancel", 1, false );
                        }
                    } );
                }

                else {
                    FrameLayout filterButtonLayout = (FrameLayout) findViewById ( R.id.extraButtonFrame );
                    filterButtonLayout.removeAllViews ();
                }

            }
        } ) );

        try {
            String value = getIntent ().getStringExtra ( "QR" );
            if (value != null) {
                List<String> ingredients = Arrays.asList ( value.split ( "," ) );
                List<SampleModel> toAdd = createListFromIngredientsStrings ( getApplicationContext (), ingredients );
                for (SampleModel sam : toAdd) {
                    if (!ingredients_list.contains ( sam )) {
                        ingredients_list.add ( sam );
                    }
                }

                List<String> inv = new ArrayList<> ();
                for (SampleModel sim : ingredients_list) {
                    inv.add ( sim.getName () );
                }

                Utils.writeToInventory ( getApplicationContext (), inv );
                List<ShoppingListModel> shop = Utils.loadShoppingListFromFile(getApplicationContext());
                Iterator<ShoppingListModel> iterator = shop.iterator();

                while (iterator.hasNext()) {
                    ShoppingListModel item = iterator.next();
                    for (SampleModel s: toAdd) {
                        if (item.getIng().getName().toLowerCase().trim().equals(s.getName().toLowerCase().trim())){
                            iterator.remove();
                        }
                    }
                }

                Utils.saveShoppingListToFile( getApplicationContext(), shop);

                data_load ();
                refreshAdapter ();

            }
        } catch (Exception e) {

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 5: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent i = new Intent ( MyInventoryActivity.this, cameraActivity.class );
                    startActivity ( i );

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText ( getApplicationContext (), "Camera permission was denied", Toast.LENGTH_SHORT ).show ();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void readyIntent(Intent i, int requestCode) {
        i.putExtra ( "requestCode", requestCode );
        List<String> alreadyAddedItems = Utils.loadInventory ( getApplicationContext () );

        Gson gson = new Gson ();
        String data = gson.toJson ( alreadyAddedItems );
        i.putExtra ( "Added", data );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            return;
        }

        List<String> ret;
        Type listType = new TypeToken<ArrayList<String>> () {
        }.getType ();
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ret = new Gson ().fromJson ( data.getStringExtra ( "result" ), listType );

                Utils.writeToInventory ( getApplicationContext (), ret );
                List<ShoppingListModel> shop = Utils.loadShoppingListFromFile(getApplicationContext());
                Iterator<ShoppingListModel> iterator = shop.iterator();

                while (iterator.hasNext()) {
                    ShoppingListModel item = iterator.next();
                    for (String s: ret) {
                        if (item.getIng().getName().toLowerCase().trim().equals(s.toLowerCase().trim())){
                            iterator.remove();
                        }
                    }
                }

                Utils.saveShoppingListToFile( getApplicationContext(), shop);

                data_load ();
                refreshAdapter ();
            }
        }
    }

    @Override
    public void onResume() {

        super.onResume ();
        closeDrawer ();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_common_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ();


        switch (id) {
            case R.id.action_delete:
                Toast.makeText ( getApplicationContext (), "Ingredients Deleted", Toast.LENGTH_SHORT ).show ();
                // remove from inventory and refresh adapter
                return true;
        }

        return super.onOptionsItemSelected ( item );
    }

    public void data_load() {
        List<IngredientMain> ingredients = Utils.loadIngredientsClassFromInventory ( getApplicationContext () );
        ingredients_list.clear ();

        for (IngredientMain ing : ingredients) {
            SampleModel mSample = new SampleModel ( ing );
            ingredients_list.add ( mSample );
        }

        if (ingredients_list.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

    }


    public void multi_select(int position) {
        //if (mActionMode != null) {
        if (multiselect_list.contains ( ingredients_list.get ( position ) ))
            multiselect_list.remove ( ingredients_list.get ( position ) );
        else
            multiselect_list.add ( ingredients_list.get ( position ) );

            /*
            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");
                */

        refreshAdapter ();

        //}
    }


    public void refreshAdapter() {
        multiSelectAdapter.selected_usersList = multiselect_list;
        multiSelectAdapter.usersList = ingredients_list;
        multiSelectAdapter.notifyDataSetChanged ();


    }


    // AlertDialog Callback Functions

    @Override
    public void onPositiveClick(int from) {
        if (from == 1) {
            if (multiselect_list.size () > 0) {
                for (int i = 0; i < multiselect_list.size (); i++)
                    ingredients_list.remove ( multiselect_list.get ( i ) );

                multiSelectAdapter.notifyDataSetChanged ();

                Toast.makeText ( getApplicationContext (), "Ingredients Deleted", Toast.LENGTH_SHORT ).show ();
                multiselect_list = new ArrayList<SampleModel> ();
                refreshAdapter ();
                FrameLayout filterButtonLayout = (FrameLayout) findViewById ( R.id.extraButtonFrame );
                filterButtonLayout.removeAllViews ();

                List<String> newInventory = new ArrayList<> ();

                for (SampleModel s : ingredients_list) {
                    newInventory.add ( s.getName () );
                }
                Utils.writeToInventory ( getApplicationContext (), newInventory );

                if (ingredients_list.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }

            }
        }
    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }
}