package pt.unl.fct.di.www.canicookit.dataModels;


public class DietListItemModel implements Comparable<DietListItemModel> {
    private String name;

    public DietListItemModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(DietListItemModel f) {
        return this.name.compareTo ( f.getName () );

    }
}
