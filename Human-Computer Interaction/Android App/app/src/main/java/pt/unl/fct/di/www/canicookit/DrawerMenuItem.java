package pt.unl.fct.di.www.canicookit;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

/**
 * Created by ricardoesteves on 06/11/17.
 */

@Layout(R.layout.drawer_item)
public class DrawerMenuItem {

    public static final int DRAWER_MENU_ITEM_HOME = 1;
    public static final int DRAWER_MENU_ITEM_ADVANCED_SEARCH = 2;
    public static final int DRAWER_MENU_ITEM_FAVOURITES = 3;
    public static final int DRAWER_MENU_ITEM_SHOPPING_LIST = 4;
    public static final int DRAWER_MENU_ITEM_INVENTORY = 5;
    public static final int DRAWER_MENU_ITEM_DIETS = 6;

    private int mMenuPosition;
    private Context mContext;
    private DrawerCallBack mCallBack;
    private DrawerLayout mDrawer;

    @View(R.id.itemNameTxt)
    private TextView itemNameTxt;

    @View(R.id.itemIcon)
    private ImageView itemIcon;

    private String activity;

    public DrawerMenuItem(Context context, int menuPosition, DrawerLayout mDrawer, String activity) {
        mContext = context;
        mMenuPosition = menuPosition;
        this.mDrawer = mDrawer;
        this.activity = activity;

    }

    @Resolve
    private void onResolved() {
        switch (mMenuPosition){
            case DRAWER_MENU_ITEM_HOME:
                itemNameTxt.setText("Home");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_home_black_18dp));
                break;
            case DRAWER_MENU_ITEM_ADVANCED_SEARCH:
                itemNameTxt.setText("Advanced Search");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_search_black_18dp));
                break;
            case DRAWER_MENU_ITEM_FAVOURITES:
                itemNameTxt.setText("Favourites");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star_black_18dp));
                break;
            case DRAWER_MENU_ITEM_SHOPPING_LIST:
                itemNameTxt.setText("Shopping List");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopping_cart_black_18dp));
                break;
            case DRAWER_MENU_ITEM_INVENTORY:
                itemNameTxt.setText("Inventory");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopping_basket_black_18dp));
                break;
            case DRAWER_MENU_ITEM_DIETS:
                itemNameTxt.setText("Diets");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_restaurant_black_18dp));
                break;
        }
    }

    Intent intent;

    @Click(R.id.mainView)
    public void onMenuItemClick(){

        switch (mMenuPosition){
            case DRAWER_MENU_ITEM_HOME:

                if (activity != null && activity.equals("Home")){
                    mDrawer.closeDrawers();
                }
                else {

                    intent = new Intent(mContext, MainActivity.class );
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }

                if(mCallBack != null)mCallBack.onProfileMenuSelected();
                break;

            case DRAWER_MENU_ITEM_ADVANCED_SEARCH:

                if (activity != null && activity.equals("Advanced_Search")){
                    mDrawer.closeDrawers();
                }

                else {
                    intent = new Intent(mContext, AdvancedSearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }

                if(mCallBack != null)mCallBack.onRequestMenuSelected();
                break;

            case DRAWER_MENU_ITEM_FAVOURITES:

                if (activity != null && activity.equals("Favourites")){
                    mDrawer.closeDrawers();
                }

                else {
                    intent = new Intent(mContext, FavoritesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }

                if(mCallBack != null)mCallBack.onGroupsMenuSelected();
                break;
            case DRAWER_MENU_ITEM_SHOPPING_LIST:

                if (activity != null && activity.equals("Shopping_List")){
                    mDrawer.closeDrawers();
                }

                else {
                    intent = new Intent(mContext, ShoppingListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }

                if(mCallBack != null)mCallBack.onSettingsMenuSelected();

                break;
            case DRAWER_MENU_ITEM_INVENTORY:

                if (activity != null && activity.equals("Inventory")){
                    mDrawer.closeDrawers();
                }

                else {
                    intent = new Intent(mContext, MyInventoryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }

                if(mCallBack != null)mCallBack.onNotificationsMenuSelected();
                break;
            case DRAWER_MENU_ITEM_DIETS:

                if (activity != null && activity.equals("Diets")){
                    mDrawer.closeDrawers();
                }

                else {
                    intent = new Intent(mContext, DietsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }

                if(mCallBack != null)mCallBack.onSettingsMenuSelected();

                break;
        }
    }

    public void setDrawerCallBack(DrawerCallBack callBack) {
        mCallBack = callBack;
    }

    public interface DrawerCallBack{
        void onProfileMenuSelected();
        void onRequestMenuSelected();
        void onGroupsMenuSelected();
        void onMessagesMenuSelected();
        void onNotificationsMenuSelected();
        void onSettingsMenuSelected();
    }
}