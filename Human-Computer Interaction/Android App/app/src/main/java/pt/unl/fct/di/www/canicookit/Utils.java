package pt.unl.fct.di.www.canicookit;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import pt.unl.fct.di.www.canicookit.dataModels.SampleModel;
import pt.unl.fct.di.www.canicookit.dataModels.ShoppingListModel;

/**
 * Created by ricardoesteves on 09/11/17.
 */

public class Utils {

    private static final String TAG = "Utils";

    public static List<InfiniteFeedInfo> loadInfiniteFeeds(Context context) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            JSONArray array = new JSONArray(loadJSONFromAsset(context, "infinite_news.json"));

            List<InfiniteFeedInfo> feedList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                InfiniteFeedInfo feed = gson.fromJson(array.getString(i), InfiniteFeedInfo.class);
                feedList.add(feed);
            }
            return feedList;
        } catch (Exception e) {
            Log.d(TAG, "seedGames parseException " + e);
            e.printStackTrace();
            return null;
        }
    }

    public static List<InfiniteFeedInfo> loadInfiniteFavorites(Context context) {
        List<SimpleRecipe> originList = loadFavorites(context);
        List<InfiniteFeedInfo> returnList = new ArrayList<>();

        for (SimpleRecipe s: originList){
            InfiniteFeedInfo foundRecipe = loadRecipe(context, s.Title);
            if(foundRecipe != null)
                returnList.add(foundRecipe);
        }

        return returnList;
    }

    public static InfiniteFeedInfo loadRecipe(Context context, String RecipeName) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            JSONArray array = new JSONArray(loadJSONFromAsset(context, "infinite_news.json"));

            for (int i = 0; i < array.length(); i++) {
                InfiniteFeedInfo feed = gson.fromJson(array.getString(i), InfiniteFeedInfo.class);
                if (feed.getTitle().toLowerCase().equals(RecipeName.toLowerCase()))
                   return feed;
            }
            return null;

        } catch (Exception e) {
            Log.d(TAG, "seedGames parseException " + e);
            e.printStackTrace();
            return null;
        }
    }

    public static List<InfiniteFeedInfo> loadInfiniteFeeds(Context context, String query) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            JSONArray array = new JSONArray(loadJSONFromAsset(context, "infinite_news.json"));
            List<InfiniteFeedInfo> feedList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                InfiniteFeedInfo feed = gson.fromJson(array.getString(i), InfiniteFeedInfo.class);
                if (feed.getTitle().toLowerCase().contains(query.toLowerCase()))
                    feedList.add(feed);
            }
            return feedList;
        } catch (Exception e) {
            Log.d(TAG, "seedGames parseException " + e);
            e.printStackTrace();
            return null;
        }
    }

    public static List<InfiniteFeedInfo> loadRecipeWithIngredientsList(Context context, String [] ingredientsToExclude, String [] ingredientsToInclude) {
        List<InfiniteFeedInfo> feedList = new ArrayList<>();

        try {

            List<InfiniteFeedInfo> originalList = loadInfiniteFeeds(context);
            if (originalList == null)
                return feedList;
            Log.d("OriginalListSize", String.valueOf(originalList.size()));

            List<String> ingredientsToIncludeList = Arrays.asList(ingredientsToInclude);
            List<String> ingredientsToExcludeList = Arrays.asList(ingredientsToExclude);

            if (ingredientsToIncludeList.size()==0){
                feedList = originalList;
                Log.d("EmptyIncludeList", String.valueOf(originalList.size()));
            }
            else {
                for (InfiniteFeedInfo recipe : originalList) {

                    boolean found = false;
                    Ingredient [] recipeIngredients = recipe.getIngredients();

                    for (Ingredient ingredient : recipeIngredients){

                        for (String s : ingredientsToIncludeList){
                            if (s.trim().toLowerCase().equals(ingredient.getName().trim().toLowerCase())){
                                feedList.add(recipe);
                                found = true;
                                break;
                            }

                        }
                        if (found)
                            break;
                    }
                }
            }

            Iterator<InfiniteFeedInfo> iterator = originalList.iterator();

            while (iterator.hasNext()){

                InfiniteFeedInfo recipe = iterator.next();
                Ingredient [] recipeIngredients = recipe.getIngredients();
                boolean found = false;
                for (Ingredient ingredient : recipeIngredients){

                    for (String s : ingredientsToExcludeList){
                        if (s.trim().toLowerCase().equals(ingredient.getName().trim().toLowerCase())){
                            found = true;
                        }
                    }
                }
                if (found) {
                    iterator.remove();
                }

            }

            return feedList;

        } catch (Exception e) {
            Log.d(TAG, "seedGames parseException " + e);
            e.printStackTrace();

            return feedList;
        }
    }

    public static List<InfiniteFeedInfo> loadInfiniteFavorites(Context context, String query) {

        List<InfiniteFeedInfo> returnList = new ArrayList<>();
        List<InfiniteFeedInfo> originList = loadInfiniteFavorites(context);

        for (InfiniteFeedInfo i : originList) {
            if (i.getTitle().toLowerCase().contains(query.toLowerCase())){
                returnList.add(i);
            }
        }

        return returnList;

    }


    private static String loadJSONFromAsset(Context context, String jsonFileName) {
        String json = null;
        InputStream is = null;
        try {
            AssetManager manager = context.getAssets();
            Log.d(TAG, "path " + jsonFileName);
            is = manager.open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static boolean writeToFavorites (Context context, InfiniteFeedInfo Recipe, boolean add){

        String path = context.getFilesDir().getPath() + "/" + "favorites.json";

        SimpleRecipe simple = new SimpleRecipe(Recipe.getTitle());

        try{

            File file = new File(path);
            if(!file.exists())
                file.createNewFile();

            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

            List<SimpleRecipe> list = new ArrayList<>();

            try {

                TypeReference<List<SimpleRecipe>> mapType = new TypeReference<List<SimpleRecipe>>() {};
                list = mapper.readValue(file, mapType);

            }
            catch (Exception o){

            }

            if (add){
                list.add(simple);
            }
            else {
                int indexToRemove = -1;
                for (int i = 0; i < list.size(); i++){
                    if (list.get(i).Title.equals(Recipe.getTitle())){
                        indexToRemove = i;
                    }
                }
                if (indexToRemove != -1 ){
                    list.remove(indexToRemove);
                }
            }

            writer.writeValue(file, list);
            return true;

        }

        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public static List<SimpleRecipe> loadFavorites(Context context) {

        String path = context.getFilesDir().getPath() + "/" + "favorites.json";
        List<SimpleRecipe> list = new ArrayList<>();

        try {

            File file = new File(path);
            if(!file.exists())
                file.createNewFile();

            ObjectMapper mapper = new ObjectMapper();

            try{
                TypeReference<List<SimpleRecipe>> mapType = new TypeReference<List<SimpleRecipe>>() {};
                list = mapper.readValue(file, mapType);
            }
            catch (Exception o){

            }

            return list;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean writeToInventory (Context context, List<String> ingredients){

        String path = context.getFilesDir().getPath() + "/" + "inventory.json";

        try{

            File file = new File(path);
            if(!file.exists())
                file.createNewFile();

            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

            writer.writeValue(file, ingredients);
            return true;

        }

        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> loadInventory(Context context) {

        String path = context.getFilesDir().getPath() + "/" + "inventory.json";
        List<String> list = new ArrayList<>();

        try {

            File file = new File(path);
            if(!file.exists())
                file.createNewFile();

            ObjectMapper mapper = new ObjectMapper();

            try{
                TypeReference<List<String>> mapType = new TypeReference<List<String>>() {};
                list = mapper.readValue(file, mapType);
            }
            catch (Exception o){

            }

            return list;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<IngredientMain> loadIngredientsClassFromInventory(Context context) {

        String path = context.getFilesDir().getPath() + "/" + "inventory.json";
        List<String> list = new ArrayList<>();
        List<IngredientMain> allIngredients = new ArrayList<>();
        List<IngredientMain> returnList = new ArrayList<>();

        try {

            File file = new File(path);
            if(!file.exists())
                file.createNewFile();

            ObjectMapper mapper = new ObjectMapper();

            try{
                TypeReference<List<String>> mapType = new TypeReference<List<String>>() {};
                list = mapper.readValue(file, mapType);
            }
            catch (Exception o){

            }

            allIngredients = loadIngredientsFromAsset(context);

            for (String s : list) {
                for (IngredientMain i : allIngredients) {
                    if (s.equals(i.getName())){
                        returnList.add(i);
                    }
                }
            }

            return returnList;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*  public static List<Diet> loadDiets(Context context) {
        try {
            GsonBuilder builder = new GsonBuilder ();
            Gson gson = builder.create ();
            JSONArray array = new JSONArray ( loadJSONFromAsset ( context, "diets.json" ) );
            List<Diet> dietList = new ArrayList<> ();
            for (int i = 0; i < array.length (); i++) {
                Diet d = gson.fromJson ( array.getString ( i ), Diet.class );
                dietList.add ( d );
            }
            return dietList;
        } catch (Exception e) {
            Log.d ( TAG, "seedGames parseException " + e );
            e.printStackTrace ();
            return null;
        }
    }*/

    public static List<Diet> loadDietsFromFile(Context context) {
        Gson gson = new Gson ();
        String jsonString = "";
        List<Diet> ret = new ArrayList<> ();

        try {
            InputStream inputStream = context.openFileInput ( "diets.json" );

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader ( inputStream );
                BufferedReader bufferedReader = new BufferedReader ( inputStreamReader );
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder ();

                while ((receiveString = bufferedReader.readLine ()) != null) {
                    stringBuilder.append ( receiveString );
                }
                inputStream.close ();
                jsonString = stringBuilder.toString ();
                JSONArray jsonArray = new JSONArray ( jsonString );

                Type listType = new TypeToken<ArrayList<Diet>> () {
                }.getType ();
                ret = new Gson ().fromJson ( jsonArray.toString (), listType );
                return ret;
            }

        } catch (FileNotFoundException e) {
            Log.e ( "login activity", "File not found: " + e.toString () );
        } catch (IOException e) {
            Log.e ( "login activity", "Can not read file: " + e.toString () );
        } catch (JSONException e) {
            Log.e ( "login activity", "Can not convert to JSON: " + e.toString () );
        }

        return ret;
    }

    public static void saveDietsToFile(Context context, List<Diet> diets) {
        Gson gson = new Gson ();
        String data = gson.toJson ( diets );
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter ( context.openFileOutput ( "diets.json", Context.MODE_PRIVATE ) );
            outputStreamWriter.write ( data );
            outputStreamWriter.close ();
        } catch (IOException e) {
            Log.e ( "Exception", "File write failed: " + e.toString () );
        }
    }

    public static void saveShoppingListToFile(Context context, List<ShoppingListModel> shoppingList) {
        Gson gson = new Gson ();
        String data = gson.toJson ( shoppingList );
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter ( context.openFileOutput ( "shoppingList.json", Context.MODE_PRIVATE ) );
            outputStreamWriter.write ( data );
            outputStreamWriter.close ();
        } catch (IOException e) {
            Log.e ( "Exception", "File write failed: " + e.toString () );
        }
    }

    public static List<ShoppingListModel> loadShoppingListFromFile(Context context) {
        Gson gson = new Gson ();
        String jsonString = "";
        List<ShoppingListModel> ret = new ArrayList<> ();

        try {
            InputStream inputStream = context.openFileInput ( "shoppingList.json" );

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader ( inputStream );
                BufferedReader bufferedReader = new BufferedReader ( inputStreamReader );
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder ();

                while ((receiveString = bufferedReader.readLine ()) != null) {
                    stringBuilder.append ( receiveString );
                }
                inputStream.close ();
                jsonString = stringBuilder.toString ();
                JSONArray jsonArray = new JSONArray ( jsonString );

                Type listType = new TypeToken<ArrayList<ShoppingListModel>> () {
                }.getType ();
                ret = new Gson ().fromJson ( jsonArray.toString (), listType );
                return ret;
            }

        } catch (FileNotFoundException e) {
            Log.e ( "login activity", "File not found: " + e.toString () );
        } catch (IOException e) {
            Log.e ( "login activity", "Can not read file: " + e.toString () );
        } catch (JSONException e) {
            Log.e ( "login activity", "Can not convert to JSON: " + e.toString () );
        }

        return ret;
    }

    public static boolean isInShoppingList (Context context, String ingredientName){
        List<ShoppingListModel> shop = loadShoppingListFromFile (context);
        for (ShoppingListModel s : shop){
            if(s.getIng().getName().trim().toLowerCase().equals(ingredientName.trim().toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static List<IngredientMain> loadIngredientsFromAsset(Context context) {
        String json = loadJSONFromAsset ( context, "ingredients.json" );
        List<IngredientMain> ret;

        Type listType = new TypeToken<ArrayList<IngredientMain>> () {
        }.getType ();
        ret = new Gson ().fromJson ( json, listType );
        return ret;
    }

    /*
    public static List<Ingredient> loadIngredientsInInventory(Context context) {

        String path = context.getFilesDir().getPath() + "/" + "inventory.json";
        List<Ingredient> list = new ArrayList<>();

        try {

            File file = new File(path);
            if(!file.exists())
                file.createNewFile();

            ObjectMapper mapper = new ObjectMapper();

            try{
                TypeReference<List<Ingredient>> mapType = new TypeReference<List<Ingredient>>() {};
                list = mapper.readValue(file, mapType);
            }
            catch (Exception o){

            }

            return list;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    */

    public static IngredientMain getIngFromAsset(String ing, Context context) {
        List<IngredientMain> ingList = Utils.loadIngredientsFromAsset ( context );
        for (IngredientMain i : ingList) {
            if (i.getName().trim().equalsIgnoreCase ( ing.trim() )) {

                Log.d("URL: ", i.getImgUrl());
                return new IngredientMain ( i.getName (), i.getType (), i.getImgUrl (), i.getUnitsMeasure () );

            }
        }
        return null;
    }


    public static List<Ingredient> getIngredients (Context context, Ingredient[] recipeIngredients, boolean Missing) {
        List<String> inventory = loadInventory(context);
        List<Ingredient> returnList = new ArrayList<>();

        if (Missing) {
            for (Ingredient ingRecipe : recipeIngredients){
                boolean found = false;
                for(String s : inventory){
                    if (s.trim().toLowerCase().equals(ingRecipe.getName().trim().toLowerCase())){
                        found = true;
                        break;
                    }

                }
                if (!found) returnList.add(ingRecipe);

            }
        }
        else {
            for (Ingredient ingRecipe : recipeIngredients){
                boolean found = false;
                for(String s : inventory){
                    if (s.trim().toLowerCase().equals(ingRecipe.getName().trim().toLowerCase())){
                        found = true;
                        break;
                    }

                }
                if (found) returnList.add(ingRecipe);

            }
        }

        return returnList;
    }

    public static List<SampleModel> createListFromIngredientsStrings (Context context, List<String> ingredientNames ){
        List<IngredientMain> original =  loadIngredientsFromAsset(context);
        List<SampleModel> returnList = new ArrayList<>();

        for (IngredientMain ing : original) {
            for (String s : ingredientNames){
                if(s.trim().toLowerCase().equals(ing.getName().trim().toLowerCase())){
                    SampleModel model = new SampleModel(ing.getName(), ing.getImgUrl());
                    returnList.add(model);
                    break;
                }
            }
        }

        return returnList;
    }


}