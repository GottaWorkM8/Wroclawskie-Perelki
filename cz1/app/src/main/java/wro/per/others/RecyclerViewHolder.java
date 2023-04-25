package wro.per.others;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import wro.per.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {


    TextView name;
    TextView objectCountName;
    TextView objectCount;
    View quest;
    ConstraintLayout object;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        objectCountName = itemView.findViewById(R.id.objectCountName);
        objectCount = itemView.findViewById(R.id.objectCount);
        quest = itemView.findViewById(R.id.quest);
        object = itemView.findViewById(R.id.object);
    }


}
