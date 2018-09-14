package pt.unl.fct.di.www.canicookit.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pt.unl.fct.di.www.canicookit.R;
import pt.unl.fct.di.www.canicookit.dataModels.SampleModel;

/**
 * Created by MendesPC on 25/11/2017.
 */

public class MultiSelectAdapter extends RecyclerView.Adapter<MultiSelectAdapter.MyViewHolder> {

    public ArrayList<SampleModel> usersList=new ArrayList<>();
    public ArrayList<SampleModel> selected_usersList=new ArrayList<>();
    Context mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public RelativeLayout ll_listitem;
        public FrameLayout buttonFrame;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.tv_user_name);
            name = (TextView) view.findViewById(R.id.tv_posting);
            ll_listitem=(RelativeLayout)view.findViewById(R.id.ll_listitem);

            buttonFrame = (FrameLayout) view.findViewById(R.id.clickIngredientFrame);

        }
    }


    public MultiSelectAdapter(Context context,ArrayList<SampleModel> userList,ArrayList<SampleModel> selectedList) {
        this.mContext=context;
        this.usersList = userList;
        this.selected_usersList = selectedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_on_inventory, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SampleModel movie = usersList.get(position);
        holder.name.setText(movie.getName());

        Glide.with(mContext).load(movie.getUrl()).fitCenter().into(holder.image);

        if(selected_usersList.contains(usersList.get(position)))
            LayoutInflater.from(mContext).inflate(R.layout.checkmark, holder.buttonFrame);
        else
           holder.buttonFrame.removeAllViews();


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}

