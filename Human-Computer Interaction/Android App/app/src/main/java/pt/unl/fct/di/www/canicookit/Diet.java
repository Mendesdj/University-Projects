package pt.unl.fct.di.www.canicookit;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

class Diet implements Comparable<Diet> {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("includedIngredients")
    @Expose
    private List<IngredientMain> includedIngredients;

    @SerializedName("excludedIngredients")
    @Expose
    private List<IngredientMain> excludedIngredients;

    public Diet(String name) {
        this.name = name;
        includedIngredients = new ArrayList<> ();
        excludedIngredients = new ArrayList<> ();
    }

    public List<IngredientMain> getIncludedIngredients() {
        return includedIngredients;
    }

    public List<IngredientMain> getExcludedIngredients() {
        return excludedIngredients;
    }

    public String getName() {
        return name;
    }


    public void excludeIngredient(IngredientMain ingredient) {
        excludedIngredients.add ( ingredient );

        int count = 0;
        for (IngredientMain ing : includedIngredients) {
            if (ing.getName ().equals ( ingredient.getName () )) {
                includedIngredients.remove ( count );
                break;
            }
            count++;
        }
    }

    public void inludeIngredient(IngredientMain ingredient) {
        includedIngredients.add ( ingredient );

        int count = 0;
        for (IngredientMain ing : excludedIngredients) {
            if (ing.getName ().equals ( ingredient.getName () )) {
                excludedIngredients.remove ( count );
                break;
            }
            count++;
        }
    }

    @Override
    public int compareTo(@NonNull Diet o) {
        return this.name.compareTo ( o.getName () );
    }
}
