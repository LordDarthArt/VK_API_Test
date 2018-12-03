package com.example.lorddarthart.testvk;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    View view;
    ViewHolder viewHolder;
    List<NoteText> noteText;

    public RecyclerViewAdapter(Context context, List<NoteText> noteText) {
        this.context = context;
        this.noteText = noteText;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.singleitem, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.txtNote.setText(noteText.get(position).getNoteText());
    }

    @Override
    public int getItemCount() {
        return noteText.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNote;
        CardView rvCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNote = itemView.findViewById(R.id.txtNotes);
            rvCardView = itemView.findViewById(R.id.rvCardView);
        }
    }
}
