package com.example.wordwrapgame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class WordlistFragment extends Fragment {

    public WordlistFragment() {
        // Required empty public constructor
    }

    public static WordlistFragment newInstance(String param1, String param2) {
        WordlistFragment fragment = new WordlistFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.d("WordlistFragment", "onCreate" + " arguments not null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wordlist, container, false);

        // Init UI components
        // Init Gameboard


        return view;
    }
}