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
    List<Zagadki> zagadki;

    public RecyclerViewAdapter(List<Zagadki> zagadki, Context context) {
        this.zagadki = zagadki;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.kafelek_zagadki_layout,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.name.setText(zagadki.get(position).getName());
        //holder.objectCount.setText(zagadki.get(position).getObjectCount());
    }

    @Override
    public int getItemCount() {
        return zagadki.size();
    }



}

