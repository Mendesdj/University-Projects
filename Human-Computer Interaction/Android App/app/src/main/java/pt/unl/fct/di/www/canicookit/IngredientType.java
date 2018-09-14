package pt.unl.fct.di.www.canicookit;

//FALTA UM TIPO O NUTS AND CENAS
enum IngredientType {
    MEATS, FISH, PASTA, FRUITS, DAIRY, DRINKS, CONDIMENTS, HERBS, VEGETABLES, OILS, SAUCE, CEREALS, FLOUR, EGGS, SWEET;

    IngredientType() {
    }

    public static IngredientType fromValue(String value) {
        return valueOf(value);
    }

    @Override
    public String toString() {
        String ret = name ().toLowerCase ();
        return ret.substring(0, 1).toUpperCase() + ret.substring(1);
    }
}

