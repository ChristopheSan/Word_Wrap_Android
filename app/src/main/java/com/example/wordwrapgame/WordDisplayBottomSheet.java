package com.example.wordwrapgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class WordDisplayBottomSheet extends BottomSheetDialogFragment {

    private ArrayList<String> foundWords;
    private Boolean showFoundWords;

    // Constructor to pass the words list
    public WordDisplayBottomSheet(ArrayList<String> words, Boolean showFoundWords) {
        this.foundWords = words;
        this.showFoundWords = showFoundWords;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_words_bottom_sheet, container, false);

        // Determine if to show found words or remaining words
        TextView title = view.findViewById(R.id.words_title);
        if (showFoundWords) {
            title.setText("Found Words");
        } else {
            title.setText("Remaining Words: " + foundWords.size());
        }

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.words_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new WordsAdapter(foundWords));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View parent = (View) view.getParent();
        BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(parent);
        behavior.setPeekHeight((int) (getResources().getDisplayMetrics().heightPixels * 0.9));
    }

    // RecyclerView Adapter to display words
    private static class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordViewHolder> {
        private final ArrayList<String> words;

        WordsAdapter(ArrayList<String> words) {
            this.words = words;
        }

        @NonNull
        @Override
        public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.word_view_row, parent, false);
            return new WordViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
            holder.wordTextView.setText(words.get(position));
        }

        @Override
        public int getItemCount() {
            return words.size();
        }

        static class WordViewHolder extends RecyclerView.ViewHolder {
            TextView wordTextView;

            WordViewHolder(@NonNull View itemView) {
                super(itemView);
                wordTextView = itemView.findViewById(R.id.word_text);
            }
        }
    }
}
