package pt.unl.fct.di.www.canicookit;

public class IngredientMain implements Comparable<IngredientMain> {
    private String name;
    private String type;
    private String url;
    private String unitsMeasure;

    public IngredientMain(String name, IngredientType type, String url) {
        this.name = name;
        this.type = type.toString ();
        this.url = url;
    }

    public IngredientMain(String name, String type, String url, String unitsMeasure) {
        this.name = name;
        this.type = type;
        this.url = url;
        this.unitsMeasure = unitsMeasure;
    }

    public IngredientMain(String name, String type, String url, UnitMeasures unitsMeasure) {
        this.name = name;
        this.type = type;
        this.url = url;
        this.unitsMeasure = unitsMeasure.toString ();
    }

    public IngredientMain(String name, String type, String url) {
        this.name = name;
        this.type = type;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getImgUrl() {
        return url;
    }

    public String getUnitsMeasure() {
        return unitsMeasure;
    }

    public void setUnitsMeasure(String unitsMeasure) {
        this.unitsMeasure = unitsMeasure;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(IngredientMain f) {
        return this.name.compareTo ( f.getName () );

    }
}
