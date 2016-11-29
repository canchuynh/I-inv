package edu.team6.inventory.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.team6.inventory.activities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFirstFragment extends Fragment {


    public IntroFirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_view_items, container, false);
    //linearLayout = (LinearLayout) rootView.findViewById(R.id.linearlayout);
        return rootView;
    }

}
