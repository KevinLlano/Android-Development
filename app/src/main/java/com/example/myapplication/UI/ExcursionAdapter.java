package com.example.myapplication.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.UI.entities.Excursion;


import java.util.ArrayList;
import java.util.List;

// Sets up the RecyclerView list
public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;

    // ViewHolder for the RecyclerView item
    class ExcursionViewHolder extends RecyclerView.ViewHolder {

        private final TextView excursionItemView;

        private ExcursionViewHolder(View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView3); // TextView for the excursion title
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        final Excursion current = mExcursions.get(position);
                        Intent intent = new Intent(context, ExcursionDetails.class);
                        intent.putExtra("id", current.getExcursionID());
                        intent.putExtra("title", current.getExcursionTitle());
                        intent.putExtra("vacationID", current.getVacationId());
                        intent.putExtra("excursionDate", current.getExcursionDate());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    // Constructor for the adapter
    public ExcursionAdapter(Context context, List<Excursion> excursions) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mExcursions = excursions != null ? excursions : new ArrayList<>();
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if (mExcursions != null && !mExcursions.isEmpty()) {
            Excursion current = mExcursions.get(position);
            holder.excursionItemView.setText(current.getExcursionTitle());
        } else {
            holder.excursionItemView.setText(R.string.no_excursion_title);
        }
    }

    @Override
    public int getItemCount() {
        return mExcursions != null ? mExcursions.size() : 0;
    }

    // Sets the list of excursions
    public void setExcursion(List<Excursion> excursions) {
        mExcursions = excursions != null ? excursions : new ArrayList<>();
        notifyDataSetChanged();
    }
}
