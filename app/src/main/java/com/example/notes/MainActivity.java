package com.example.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notes.DataBase.roomDB;
import com.example.notes.adapter.notesListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.notes.Models.Notes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    notesListAdapter notelist;
    roomDB database;
    List<Notes> notes = new ArrayList<>();
    SearchView searchView_home;
    Notes selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_home);
        fab = findViewById(R.id.fab_add);

        searchView_home = findViewById(R.id.searchView_home);

        database = roomDB.getInstance(this);
        notes = database.mainDAO().getAll();

        updateRecycler(notes);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteTakingActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter (newText);
                return false;
            }
        });
    }

    private void filter(String newText) {
        List<Notes> filteredList = new ArrayList<>();
        boolean isFilterApplied = !newText.isEmpty();

        for (Notes singleNote : notes) {
            if (isFilterApplied && (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase()))) {
                filteredList.add(singleNote);
            }
        }

        if (isFilterApplied) {
            notelist.filterList(filteredList);
        } else {
            notelist.filterList(notes);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if(resultCode == Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("notes");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notelist.notifyDataSetChanged();
            }
        }

        if(requestCode == 102){
            if(resultCode == Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("notes");
                database.mainDAO().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notelist.notifyDataSetChanged();
            }
        }
    }



    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notelist = new notesListAdapter(MainActivity.this, notes, clicker);
        recyclerView.setAdapter(notelist);

    }

    private final notesClicker clicker = new notesClicker() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NoteTakingActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {

            selectedNote = new Notes();
            selectedNote = notes;
            showPopup(cardView);

        }
    };
    private void showPopup(CardView cardView){
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()==R.id.pin) {
            if (selectedNote.isPinned()) {
                database.mainDAO().pin(selectedNote.getID(), false);
                Toast.makeText(MainActivity.this, "unpinned", Toast.LENGTH_SHORT).show();
            } else {
                database.mainDAO().pin(selectedNote.getID(), true);
                Toast.makeText(MainActivity.this, "pinned", Toast.LENGTH_SHORT).show();
            }
            notes.clear();
            notes.addAll(database.mainDAO().getAll());
            notelist.notifyDataSetChanged();
            return true;
        }
        if(item.getItemId()==R.id.delete) {
            database.mainDAO().delete(selectedNote);
            notes.remove(selectedNote);
            notelist.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "removed", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}