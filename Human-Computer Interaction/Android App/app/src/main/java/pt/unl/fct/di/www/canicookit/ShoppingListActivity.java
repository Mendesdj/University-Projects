package pt.unl.fct.di.www.canicookit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.unl.fct.di.www.canicookit.adapters.CustomAdapterShoppingList;
import pt.unl.fct.di.www.canicookit.dataModels.ShoppingListModel;

public class ShoppingListActivity extends BaseActivity {

    List<ShoppingListModel> dataModels;
    private CustomAdapterShoppingList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        activity = "Shopping_List";
        super.onCreate ( savedInstanceState );

        // setContentView ( R.layout.activity_shopping_list_acitivty );
        FrameLayout contentFrameLayout = (FrameLayout) findViewById ( R.id.content_frame ); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater ().inflate ( R.layout.activity_shopping_list_acitivty, contentFrameLayout );

        //Toolbar toolbar = (Toolbar) findViewById ( R.id.toolbar );
        //setSupportActionBar ( toolbar );
        FrameLayout buttonFrameLayout = (FrameLayout) findViewById ( R.id.Button_frame );
        getLayoutInflater ().inflate ( R.layout.white_delete_view, buttonFrameLayout );

        ImageView deleteEverything = (ImageView) findViewById ( R.id.idDeleteImageView );
        deleteEverything.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder ( ShoppingListActivity.this )
                        .setTitle ( "Confirmation" )
                        .setMessage ( "This will delete all items on your shopping list?" )
                        .setIcon ( R.drawable.ic_dialog_alert_black )
                        .setPositiveButton ( android.R.string.yes, new DialogInterface.OnClickListener () {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dataModels.clear ();
                                listviewSetup ();
                                Utils.saveShoppingListToFile ( getApplicationContext (), dataModels );
                            }
                        } )
                        .setNegativeButton ( android.R.string.no, null ).show ();
            }
        } );

        dataModels = Utils.loadShoppingListFromFile ( getApplicationContext () );

        ListView lv = (ListView) findViewById ( R.id.idShoppingList );

        // View empty = getLayoutInflater ().inflate ( R.layout.empty_view, null, false );
        // addContentView ( empty, new FrameLayout.LayoutParams ( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ) );
        // rl.addView ( empty , new FrameLayout.LayoutParams (  ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));

        lv.setEmptyView ( findViewById ( R.id.empty ) );

        listviewSetup ();

        FloatingActionButton fab = (FloatingActionButton) findViewById ( R.id.fab );
        fab.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent ( getApplicationContext (), IncludeExcludeActivity.class );
                readyIntent ( i, 1 );
                startActivityForResult ( i, 1 );
            }
        } );
        //getSupportActionBar ().setDisplayHomeAsUpEnabled ( false );
    }

    private void readyIntent(Intent i, int requestCode) {
        i.putExtra ( "requestCode", requestCode );
        List<String> alreadyAddedItems = new ArrayList<> ();
        for (ShoppingListModel ing : dataModels) {
            alreadyAddedItems.add ( ing.getIng ().getName () );
        }
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
        int pos;
        Type listType = new TypeToken<ArrayList<String>> () {
        }.getType ();
        ShoppingListModel a;
        if (resultCode == RESULT_OK) {
            ret = new Gson ().fromJson ( data.getStringExtra ( "result" ), listType );

            List<ShoppingListModel> newData = new ArrayList<> ();

            //dataModels.clear ();
            Iterator<String> it = ret.iterator ();
            while (it.hasNext ()) {
                String s = it.next ();
                for (ShoppingListModel s1 : dataModels) {
                    if (s.equalsIgnoreCase ( s1.getIng ().getName () )) {
                        newData.add ( s1 );
                        it.remove ();
                        break;
                    }
                }
            }
            dataModels = newData;

            for (String newIng : ret) {
                dataModels.add ( new ShoppingListModel ( newIng, 1, getApplicationContext () ) );
            }

        }
        listviewSetup ();
    }

    private void listviewSetup() {

        adapter = new CustomAdapterShoppingList ( getApplicationContext (), dataModels );
        adapter.setFragmentManager ( getSupportFragmentManager () );
        adapter.setAdapter ( adapter );


        //List view setup
        ListView lv = (ListView) findViewById ( R.id.idShoppingList );
        lv.setAdapter ( adapter );

    }

    @Override
    protected void onStop() {
        // call the superclass method first
        super.onStop ();

        Utils.saveShoppingListToFile ( getApplicationContext (), dataModels );
    }

    @Override
    public void onResume() {

        super.onResume ();
        closeDrawer ();

    }

}
