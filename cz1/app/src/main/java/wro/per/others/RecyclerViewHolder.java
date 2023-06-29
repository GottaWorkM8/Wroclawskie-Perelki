package wro.per.others;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import wro.per.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {


    TextView nameTextView;
    TextView objectCountNameTextView;
    TextView objectCountTextView;
    View questView;
    ConstraintLayout objectConstraintLayout;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.name);
        objectCountNameTextView = itemView.findViewById(R.id.objectsCountName);
        objectCountTextView = itemView.findViewById(R.id.objectsCount);
        questView = itemView.findViewById(R.id.quest);
    }


}
