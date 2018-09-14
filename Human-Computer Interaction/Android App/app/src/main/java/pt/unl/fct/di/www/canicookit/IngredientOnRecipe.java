package pt.unl.fct.di.www.canicookit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import at.markushi.ui.CircleButton;

/**
 * Created by ricardoesteves on 15/11/17.
 */
/*
@Layout(R.layout.ingredient_on_recipe)
public class IngredientOnRecipe {

    @View(R.id.IngredientButton)
    private CircleButton IngredientButton;

    @View(R.id.IngredientText)
    private TextView ingredientText;

    private Context mContext;
    private Ingredient ingredient;
    private boolean isMissing;
    private double multiplier;

    public IngredientOnRecipe(Context context, boolean isMissing, Ingredient ingredient, float multiplier) {

        mContext = context;
        this.ingredient = ingredient;
        this.isMissing = isMissing;
        this.multiplier = multiplier;

    }

    @Resolve
    private void onResolved() {

        if (!isMissing) {
            IngredientButton.setVisibility(android.view.View.GONE);

        }
        else {
            IngredientButton.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    Toast.makeText(mContext, ingredient.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }


        int quantity = Integer.valueOf(ingredient.getQuantity());
        quantity *= multiplier;

        Log.d("QUANTITY: ", String.valueOf(quantity));
        String text = quantity + " " + ingredient.getMeasure() + " " + ingredient.getName();
        ingredientText.setText(text);
    }

}

*/