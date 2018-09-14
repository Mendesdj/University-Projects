package pt.unl.fct.di.www.canicookit.dataModels;

import android.content.Context;
import android.util.Log;

import java.util.List;

import pt.unl.fct.di.www.canicookit.IngredientMain;
import pt.unl.fct.di.www.canicookit.Utils;

public class ShoppingListModel {
    private IngredientMain ing;
    private int quantity;

    public ShoppingListModel(IngredientMain ing, int quantity) {
        this.ing = ing;
        this.quantity = quantity;
    }

    public ShoppingListModel(String ing, int quantity, Context context) {
        this.ing = Utils.getIngFromAsset ( ing, context );
        this.quantity = quantity;
    }


    public IngredientMain getIng() {
        return ing;
    }

    public int getQuantity() {
        return quantity;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
