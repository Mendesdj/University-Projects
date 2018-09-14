package pt.unl.fct.di.www.canicookit;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.unl.fct.di.www.canicookit.dataModels.ShoppingListModel;
import static pt.unl.fct.di.www.canicookit.Utils.getIngFromAsset;
import static pt.unl.fct.di.www.canicookit.Utils.isInShoppingList;
import static pt.unl.fct.di.www.canicookit.Utils.loadIngredientsFromAsset;

import static pt.unl.fct.di.www.canicookit.Utils.loadShoppingListFromFile;
import static pt.unl.fct.di.www.canicookit.Utils.saveShoppingListToFile;

/**
 * Created by MendesPC && ricardoesteves on 12/11/2017.
 */

public class RecipeShow extends BaseActivity implements NumberPicker.OnValueChangeListener {

    private InfiniteFeedInfo Recipe;
    private List<MaterialFavoriteButton> ingredientButtons;
    private int numberPickerValue;

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources ().getDisplayMetrics ().density);
    }

    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ingredientButtons = new ArrayList<>();
        numberPickerValue = -1;

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.recipe_layout, contentFrameLayout);

        String value = getIntent().getStringExtra(getString(R.string.RecipeId));
        List<InfiniteFeedInfo> feedList = Utils.loadInfiniteFeeds(this.getApplicationContext(), value);
        Recipe = feedList.get(0);

        TextView title = (TextView) findViewById(R.id.cardTitle);
        title.setText(Recipe.getTitle());

        TextView time = (TextView) findViewById(R.id.cardTime);
        time.setText(Recipe.getFormattedTime());

        TextView difficulty = (TextView) findViewById(R.id.cardDifficulty);
        difficulty.setText(Recipe.getDifficulty());

        final LinearLayout ingredientLayout = (LinearLayout) findViewById(R.id.ingredientsPlaceHolderView);
        Ingredient [] ingredients = Recipe.getIngredients();

        List<Ingredient> haveIngredients = Utils.getIngredients(getApplicationContext(), ingredients, false);

        if (haveIngredients.size() != 0){
            setHeader(ingredientLayout,"Ingredients you have:", 25 , 15);
            for (final Ingredient ingredient : haveIngredients){

                View child = getLayoutInflater().inflate(R.layout.ingredient_have_recipe, null);
                TextView textView = (TextView) child.findViewById(R.id.IngredientText);
                textView.setText(getFullIngredientText(ingredient,1));

                ingredientLayout.addView(child);

            }
        }

        List<Ingredient> missingIngredients = Utils.getIngredients(getApplicationContext(), ingredients, true);

        if (missingIngredients.size() != 0){
            setHeader(ingredientLayout,"Missing Ingredients:", 30 , 15);
            for (final Ingredient ingredient : missingIngredients){

                View child = getLayoutInflater().inflate(R.layout.ingredient_on_recipe, null);
                TextView textView = (TextView) child.findViewById(R.id.IngredientText);
                textView.setText(getFullIngredientText(ingredient,1));
                MaterialFavoriteButton addtoShop = (MaterialFavoriteButton) child.findViewById(R.id.IngredientButton);

                if (isInShoppingList(getApplicationContext(),ingredient.getName())){
                    addtoShop.setFavorite(true);
                }

                setClickListenerOnButton(addtoShop, ingredient);

                ingredientLayout.addView(child);
                ingredientButtons.add(addtoShop);

            }
        }
        else {
            findViewById(R.id.addAll).setVisibility(View.GONE);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.stepsLayout);
        String [] steps = Recipe.getSteps();

        CardFragmentPagerAdapter pagerAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), dpToPixels(2, this),steps );
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);


        LinearLayout stepsLayout = (LinearLayout) findViewById(R.id.layoutSteps);
        setHeader(stepsLayout, "Steps", 20 ,0);

        ImageView image = (ImageView) findViewById(R.id.cardImage);
        Glide.with(getApplicationContext()).load(Recipe.getImageUrl()).into(image);

        TextView protein = (TextView) findViewById(R.id.proteinValue);
        protein.setText("Protein: \n" + Recipe.getProtein());

        TextView fat = (TextView) findViewById(R.id.fatValue);
        fat.setText("Fat: \n" + Recipe.getFat());

        TextView calories = (TextView) findViewById(R.id.caloriesValue);
        calories.setText("Calories: \n" + Recipe.getCalories());

        FrameLayout filterButtonLayout  = (FrameLayout) findViewById(R.id.extraButtonFrame);
        getLayoutInflater().inflate(R.layout.favorite, filterButtonLayout);

        MaterialFavoriteButton favorite = (MaterialFavoriteButton) findViewById(R.id.favoriteButton);

        List<SimpleRecipe> list = Utils.loadFavorites(getApplicationContext());
        for (SimpleRecipe recipe : list) {
            if (recipe.Title.equals(Recipe.getTitle())){
                favorite.setFavorite(true);
            }
        }

        favorite.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            if( Utils.writeToFavorites(getApplicationContext(), Recipe,true)){
                                Toast.makeText(getApplicationContext(),"Recipe added to Favorites", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            if( Utils.writeToFavorites(getApplicationContext(), Recipe,false)){
                                Toast.makeText(getApplicationContext(), "Recipe removed from Favorites", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });

        updateButtonText(Recipe.getServes());

        findViewById(R.id.addAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (MaterialFavoriteButton button : ingredientButtons){
                    button.setFavorite(true);
                }
            }
        });

    }

    private void setHeader (LinearLayout layout, String headerText, int topPadding, int bottomPadding){

        TextView header = new TextView(RecipeShow.this);
        header.setLayoutParams(new TableLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        header.setTextSize(15);
        header.setPadding(0, topPadding, 0, bottomPadding);
        header.setTypeface(Typeface.DEFAULT_BOLD);
        header.setGravity(Gravity.CENTER);
        header.setText(headerText);

        layout.addView(header);
    }

    private void updateButtonText (final String text){

        FrameLayout NumberPickerLayout  = (FrameLayout) findViewById(R.id.numberPickerFrame);
        NumberPickerLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.numberpickerlayout, NumberPickerLayout);
        TextView NumberPicker = (TextView) findViewById(R.id.buttonforNumberPicker);
        NumberPicker.setText(text);
        numberPickerValue= Integer.valueOf(text);
    }

    private String getFullIngredientText (Ingredient ingredient, float multiplier){

        int quantity;
        try {
            quantity = Integer.valueOf(ingredient.getQuantity());
            quantity *= multiplier;
            return quantity + " " + ingredient.getMeasure() + " " + ingredient.getName();
        }
        catch (Exception e) {
            return ingredient.getQuantity() + " " + ingredient.getMeasure() + " " + ingredient.getName();
        }

    }

    @Override
    public void onResume() {

        super.onResume ();
        closeDrawer();

    }

    private void setClickListenerOnButton (MaterialFavoriteButton addtoShop, final Ingredient ingredient) {
        addtoShop.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean added) {
                        if (added) {

                            List<ShoppingListModel> shop = loadShoppingListFromFile(getApplicationContext());

                            IngredientMain ing = getIngFromAsset(ingredient.getName(), getApplicationContext());
                            if (ing != null) {
                                ShoppingListModel newModel = new ShoppingListModel ( ingredient.getName().toString ().trim (), Integer.valueOf ( ingredient.getQuantity () ), getApplicationContext () );

                                shop.add(newModel);
                                saveShoppingListToFile(getApplicationContext(), shop);
                            }

                        }
                        else {
                            List<ShoppingListModel> shop = loadShoppingListFromFile(getApplicationContext());
                           Iterator<ShoppingListModel> iterator = shop.iterator();

                           while (iterator.hasNext()){
                                if (iterator.next().getIng().getName().trim().toLowerCase().equals(ingredient.getName().trim().toLowerCase())){
                                    iterator.remove();
                                }
                            }
                            saveShoppingListToFile(getApplicationContext(), shop);

                        }


                    }
                });
    }

    public void adjustIngredients (int i) {

        final LinearLayout ingredientLayout = (LinearLayout) findViewById(R.id.ingredientsPlaceHolderView);
        ingredientLayout.removeAllViews();
        ingredientButtons.clear();

        float multiplier = (float) i/ Integer.valueOf(Recipe.getServes());
        Ingredient [] ingredients = Recipe.getIngredients();

        List<Ingredient> haveIngredients = Utils.getIngredients(getApplicationContext(), ingredients, false);

        if (haveIngredients.size() != 0){
            setHeader(ingredientLayout,"Ingredients you have:", 25 , 20);
            for (final Ingredient ingredient : haveIngredients){

                View child = getLayoutInflater().inflate(R.layout.ingredient_have_recipe, null);
                TextView textView = (TextView) child.findViewById(R.id.IngredientText);
                textView.setText(getFullIngredientText(ingredient,multiplier));

                ingredientLayout.addView(child);

            }
        }

        List<Ingredient> missingIngredients = Utils.getIngredients(getApplicationContext(), ingredients, true);

        if (missingIngredients.size() != 0){
            setHeader(ingredientLayout,"Missing Ingredients:", 35 , 15);
            for (final Ingredient ingredient : missingIngredients){

                View child = getLayoutInflater().inflate(R.layout.ingredient_on_recipe, null);
                TextView textView = (TextView) child.findViewById(R.id.IngredientText);
                textView.setText(getFullIngredientText(ingredient,multiplier));
                MaterialFavoriteButton addtoShop = (MaterialFavoriteButton) child.findViewById(R.id.IngredientButton);

                if (isInShoppingList(getApplicationContext(),ingredient.getName())){
                    addtoShop.setFavorite(true);
                }

                setClickListenerOnButton(addtoShop, ingredient);

                ingredientLayout.addView(child);
                ingredientButtons.add(addtoShop);

            }
        }
        else {
            findViewById(R.id.addAll).setVisibility(View.GONE);
        }


    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        Toast.makeText(this,
                "Ingredients adjusted to " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();
        updateButtonText(String.valueOf( numberPicker.getValue()));
        adjustIngredients(numberPicker.getValue());

    }

    public void showNumberPicker(View view){

        Bundle args = new Bundle();
        args.putInt("serves",numberPickerValue);

        NumberPickerDialog newFragment = new NumberPickerDialog();
        newFragment.setArguments(args);
        newFragment.setValueChangeListener(this);
        newFragment.show(getSupportFragmentManager(), "Servings picker");
    }


}