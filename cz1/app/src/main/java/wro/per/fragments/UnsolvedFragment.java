package wro.per.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import wro.per.R;

public class UnsolvedFragment extends Fragment {

    SharedPreferences sharedPreferences;
    String userKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.unsolved_fragment, container, false);



        return view;
    }
}