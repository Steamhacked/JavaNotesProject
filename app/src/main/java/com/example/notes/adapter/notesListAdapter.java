package com.example.notes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Models.Notes;
import com.example.notes.R;
import com.example.notes.notesClicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class notesListAdapter extends RecyclerView.Adapter <notesViewHolder>{

    Context context;
    List<Notes> list;
    notesClicker clicker;

    public notesListAdapter(Context context, List<Notes> list, notesClicker clicker) {
        this.context = context;
        this.list = list;
        this.clicker = clicker;
    }

    @NonNull
    @Override
    public notesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new notesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull notesViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_title.setSelected(true);

        holder.textView_notes.setText(list.get(position).getNotes());

        holder.textView_date.setText(list.get(position).getDate());
        holder.textView_date.setSelected(true);


        if(list.get(position).isPinned()){
            holder.imageView_pin.setImageResource(R.drawable.pin_icon);
        }
        else{
            holder.imageView_pin.setImageResource(0);
        }

        int color_code = getRandomColor();
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code, null));

        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicker.onClick(list.get(holder.getAdapterPosition()));
            }
        });
        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clicker.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_container);
                return true;
            }
        });
    }

    private int getRandomColor(){
        List <Integer> colors = new ArrayList<>();
        colors.add(R.color.blue);
        colors.add(R.color.red);
        colors.add(R.color.purple);

        Random rnd = new Random();
        int random_color = rnd.nextInt(colors.size());
        return colors.get(random_color);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList (List<Notes> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }
}

class notesViewHolder extends RecyclerView.ViewHolder{

    CardView notes_container;
    TextView textView_title;
    TextView textView_notes;
    TextView textView_date;
    ImageView imageView_pin;

    public notesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_container = itemView.findViewById(R.id.notes_container);
        textView_date = itemView.findViewById(R.id.textView_date);
        textView_notes = itemView.findViewById(R.id.textView_notes);
        textView_title = itemView.findViewById(R.id.textView_title);
        imageView_pin = itemView.findViewById(R.id.imageView_pin);
    }
}