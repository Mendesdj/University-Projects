package pt.unl.fct.di.www.canicookit.dataModels;

public class ActiveFilterModel implements Comparable<ActiveFilterModel> {
    private String name;
    private Boolean isIncluded;
    private String url;

    public ActiveFilterModel(String name, boolean isIncluded) {
        this.name = name;
        this.isIncluded = isIncluded;
        this.url = "";
    }

    public ActiveFilterModel(String name, boolean isIncluded, String url) {
        this.name = name;
        this.isIncluded = isIncluded;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public Boolean getIncluded() {
        return isIncluded;
    }

    public void setIsIncluded(Boolean inc) {
        this.isIncluded = inc;
    }

    @Override
    public int compareTo(ActiveFilterModel f) {
        return this.name.compareTo ( f.getName () );

    }
}
