package com.example.notes;

import androidx.cardview.widget.CardView;

import com.example.notes.Models.Notes;

public interface notesClicker {
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);

}
