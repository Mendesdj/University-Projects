package pt.unl.fct.di.www.canicookit.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import pt.unl.fct.di.www.canicookit.R;
import pt.unl.fct.di.www.canicookit.ShoppingListDialogFragment;
import pt.unl.fct.di.www.canicookit.Utils;
import pt.unl.fct.di.www.canicookit.dataModels.ShoppingListModel;

public class CustomAdapterShoppingList extends ArrayAdapter<ShoppingListModel> {
    List<ShoppingListModel> ingredients;
    Context context;
    FragmentManager fragmentManager;
    CustomAdapterShoppingList adapter;

    public CustomAdapterShoppingList(Context context, List<ShoppingListModel> ingredients) {
        super ( context, R.layout.shopping_list_row_item, ingredients );
        this.ingredients = ingredients;
        this.context = context;
    }

    public void setIngredients(List<ShoppingListModel> ingredients) {
        this.ingredients = ingredients;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setAdapter(CustomAdapterShoppingList adapter) {
        this.adapter = adapter;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        final LayoutInflater inflter = (LayoutInflater.from ( getContext () ));
        view = inflter.inflate ( R.layout.shopping_list_row_item, null );

        final ShoppingListModel thisModel = ingredients.get ( position );
        //Image
        ImageView ingredientImage = (ImageView) view.findViewById ( R.id.idIngredientImage );
        Glide.with ( getContext () ).load ( thisModel.getIng ().getImgUrl () ).into ( ingredientImage );
        //Text
        final TextView ingredientText = (TextView) view.findViewById ( R.id.idIngredientNameTxtView );
        ingredientText.setText ( thisModel.getIng ().getName () );

        //Units
        final TextView ingredientUnitsText = (TextView) view.findViewById ( R.id.idUnits );
        ingredientUnitsText.setText ( thisModel.getQuantity () + " " + thisModel.getIng ().getUnitsMeasure () );

        //Menu item
        final ImageView menuImage = (ImageView) view.findViewById ( R.id.idThreeDotMenu );
        menuImage.setImageResource ( R.drawable.ic_menu_black_18dp );

        menuImage.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(final View v) {
                final PopupMenu popupMenu = new PopupMenu ( getContext (), menuImage );
                popupMenu.getMenuInflater ().inflate ( R.menu.shopping_list_item_menu, popupMenu.getMenu () );
                popupMenu.setOnMenuItemClickListener ( new PopupMenu.OnMenuItemClickListener () {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle ().toString ().equalsIgnoreCase ( "Remove" )) {
                            ingredients.remove ( position );
                            notifyDataSetChanged ();
                        }
                        else if (item.getTitle ().toString ().equalsIgnoreCase ( "Add to Inventory" )){
                            ShoppingListModel model = ingredients.remove ( position );
                            List<String> ingredientToAdd = Utils.loadInventory(getContext());
                            ingredientToAdd.add(model.getIng().getName());
                            Utils.writeToInventory(getContext(), ingredientToAdd);
                            notifyDataSetChanged ();
                        }
                        else {
                            ShoppingListDialogFragment fragment = new ShoppingListDialogFragment ();
                            fragment.setDataModels ( ingredients );
                            fragment.setPosition ( position );
                            fragment.setAdapter ( adapter );
                            fragment.show ( fragmentManager, "EditShoppingListItem" );
                        }
                        return false;
                    }
                } );
                popupMenu.show ();
            }
        } );
        return view;
    }


}
