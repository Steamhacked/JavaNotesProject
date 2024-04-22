package com.example.notes.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notes.Models.Notes;

@Database(entities = {Notes.class}, version = 1, exportSchema = false)
public abstract class roomDB extends RoomDatabase {
    private static roomDB database;
    private static String DB_NAME = "NotesApp";
    public synchronized static roomDB getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(), roomDB.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    public abstract mainDB mainDAO();
}
