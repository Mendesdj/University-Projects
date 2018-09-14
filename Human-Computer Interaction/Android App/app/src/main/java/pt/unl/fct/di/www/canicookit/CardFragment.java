package pt.unl.fct.di.www.canicookit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MendesPC on 24/11/2017.
 */

public class CardFragment extends Fragment {

    private CardView cardView;

    public static Fragment getInstance(int position, String step) {
        CardFragment f = new CardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("step", step);
        f.setArguments(args);

        return f;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_on_recipe, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(String.format("Step %d", getArguments().getInt("position")+1));

        TextView step = (TextView) view.findViewById(R.id.step);
        step.setText(String.format("%s", getArguments().getString("step")));


        return view;
    }

    public CardView getCardView() {
        return cardView;
    }
}
