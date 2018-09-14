package pt.unl.fct.di.www.canicookit;

/**
 * Created by tomas on 16/11/2017.
 */

public class ActiveFilterModel {
    String name;
    Boolean isIncluded;

    public ActiveFilterModel(String name, boolean isIncluded) {
        this.name = name;
        this.isIncluded = isIncluded;

        this.name = this.name;
    }

    public String getName() {
        return name;
    }

    public Boolean getIncluded() {
        return isIncluded;
    }
}
