package io.github.tiagofrbarbosa.fleekard.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.github.tiagofrbarbosa.fleekard.R;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class TextItemViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;


    public TextItemViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.list_item);
    }

    public void bind(String text) {
        textView.setText(text);
    }

}