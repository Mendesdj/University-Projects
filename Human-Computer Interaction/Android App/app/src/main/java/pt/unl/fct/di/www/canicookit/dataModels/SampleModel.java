package pt.unl.fct.di.www.canicookit.dataModels;

import pt.unl.fct.di.www.canicookit.Ingredient;
import pt.unl.fct.di.www.canicookit.IngredientMain;

/**
 * Created by MendesPC on 25/11/2017.
 */

public class SampleModel {
    private String name;
    private String url;

    public SampleModel(IngredientMain ing) {
        this.name = ing.getName();
        this.url = ing.getImgUrl();
    }

    public SampleModel(String name , String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals (Object obj) {

        if (obj.getClass() != SampleModel.class ){
            return false;
        }

        SampleModel sam = (SampleModel) obj;
        if (sam.getName().trim().toLowerCase().equals(this.getName().trim().toLowerCase())){
            return true;
        }
        else
            return false;



    }
}
