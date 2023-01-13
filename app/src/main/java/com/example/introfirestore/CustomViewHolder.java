package com.example.introfirestore;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public CardView cardView;
    public ImageView imageView;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.list_item_text);
        cardView = itemView.findViewById(R.id.main_container);
        imageView = itemView.findViewById(R.id.image_delete);
    }

}
