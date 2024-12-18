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
import com.example.myapplication.UI.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> mVacations;
    private final Context context;
    private final LayoutInflater mInflater;

    public VacationAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    // ViewHolder that holds views for each item in the RecyclerView
    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Vacation current = mVacations.get(position);
                    Intent intent = new Intent(context, VacationDetails.class);
                    // Pass vacation details as extras to the VacationDetails activity
                    intent.putExtra("id", current.getVacationId());
                    intent.putExtra("title", current.getVacationTitle());
                    intent.putExtra("hotel", current.getVacationHotel());
                    intent.putExtra("startDate", current.getStartDate());
                    intent.putExtra("endDate", current.getEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the vacation_list_item layout for each item
        View itemView = mInflater.inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        if (mVacations != null) {
            // Set the vacation title in the TextView
            Vacation current = mVacations.get(position);
            String title = current.getVacationTitle();
            holder.vacationItemView.setText(title);
        } else {
            holder.vacationItemView.setText("No vacation title");
        }
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the list
        if (mVacations != null) {
            return mVacations.size();
        } else {
            return 0;
        }
    }

    // Update the list of vacations and notify the adapter of the changes
    public void setVacations(List<Vacation> vacations) {
        mVacations = vacations;
        notifyDataSetChanged();
    }
}
