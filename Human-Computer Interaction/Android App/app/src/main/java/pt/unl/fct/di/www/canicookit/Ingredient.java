package pt.unl.fct.di.www.canicookit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ricardoesteves on 15/11/17.
 */

public class Ingredient {

    @SerializedName("measure")
    @Expose
    private String measure;

    @SerializedName("quantity")
    @Expose
    private String quantity;

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Ingredient(String name, String quantity, String measure) {
        this.measure = measure;
        this.quantity = quantity;
        this.name = name;
    }

    @Override
    public boolean equals (Object obj) {

        if (obj.getClass() == Ingredient.class ){
            Ingredient ing = (Ingredient) obj;
            return this.getName().equals(ing.getName());
        }
        else if (obj.getClass() == String.class){
            String ing = (String) obj;
            return this.getName().equals(ing);
        }
        else
            return false;


    }
}



