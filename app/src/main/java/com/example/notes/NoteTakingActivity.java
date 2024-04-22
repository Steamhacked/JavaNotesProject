package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notes.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteTakingActivity extends AppCompatActivity {

    Notes notes;
    EditText editText_title;
    EditText editText_notes;
    ImageView imageView_save;
    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_taking);

        editText_title = findViewById(R.id.editText_title);
        editText_notes = findViewById(R.id.editText_notes);
        imageView_save = findViewById(R.id.imageView_save);

        notes = new Notes();


        try {
            notes = (Notes) getIntent().getSerializableExtra("old_note");
            editText_title.setText(notes.getTitle());
            editText_notes.setText(notes.getNotes());
            isOldNote = true;

        } catch (Exception e){
            e.printStackTrace();
        }

        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText_title.getText().toString();
                String contains = editText_notes.getText().toString();

                if(contains.isEmpty()){
                    Toast.makeText(NoteTakingActivity.this, "Enter the note", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat template = new SimpleDateFormat("d MMM yyyy");
                Date date = new Date();


                if(!isOldNote){
                    notes = new Notes();
                }

                notes.setTitle(title);
                notes.setNotes(contains);
                notes.setDate(template.format(date));

                Intent intent = new Intent();
                intent.putExtra("notes", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}