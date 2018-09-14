package pt.unl.fct.di.www.canicookit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pt.unl.fct.di.www.canicookit.adapters.CustomAdapterDietList;
import pt.unl.fct.di.www.canicookit.dataModels.DietListItemModel;

public class DietsActivity extends BaseActivity {
    private static CustomAdapterDietList adapter;
    ArrayList<DietListItemModel> dataModels;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        activity = "Diets";
        super.onCreate ( savedInstanceState );

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_diets, contentFrameLayout);

        ListView lv = (ListView) findViewById ( R.id.dietsListView );
        //View empty = getLayoutInflater ().inflate ( R.layout.empty_view, null, false );
        //lv.addView ( empty );
        //lv.add

        //addContentView ( empty, new FrameLayout.LayoutParams ( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ) );
        // rl.addView ( empty , new FrameLayout.LayoutParams (  ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
        lv.setEmptyView ( findViewById ( R.id.empty ) );

        updateList();

        FloatingActionButton fab = (FloatingActionButton) findViewById ( R.id.fab );
        fab.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent ( DietsActivity.this, DietEditActivity.class );
                i.putExtra ( "DietName", "" );
                i.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity ( i );
            }
        } );
    }

    @Override
    public void onRestart() {
        // reload your list items if changed and adapter.notifydatastatechange();
        super.onRestart ();
        updateList();
    }

    @Override
    public void onResume() {

        super.onResume ();
        closeDrawer();

    }

    private void updateList(){
        //Listview set up
        ListView lv = (ListView) findViewById ( R.id.dietsListView );
        List<Diet> diets = Utils.loadDietsFromFile ( getApplicationContext () );
        dataModels = new ArrayList<> ();
        for (Diet d : diets) {
            dataModels.add ( new DietListItemModel ( d.getName () ) );
        }
        adapter = new CustomAdapterDietList ( getApplicationContext (), dataModels );
        lv.setAdapter ( adapter );

        lv.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(DietsActivity.this, "Item clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent ( DietsActivity.this, DietEditActivity.class );
                i.putExtra ( "DietName", dataModels.get ( position ).getName () );
                i.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity ( i );
            }
        } );
    }

}
