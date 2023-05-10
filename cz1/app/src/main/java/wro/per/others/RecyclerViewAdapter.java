package wro.per.others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import wro.per.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    Context context;
    List<Riddles> riddleList;

    public RecyclerViewAdapter(List<Riddles> riddleList, Context context) {
        this.riddleList = riddleList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.riddle_tile_fragment,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.nameTextView.setText(riddleList.get(position).getName());
        holder.objectCountTextView.setText(String.valueOf(riddleList.get(position).getObjectCount()));
    }

    @Override
    public int getItemCount() {
        return riddleList.size();
    }



}

