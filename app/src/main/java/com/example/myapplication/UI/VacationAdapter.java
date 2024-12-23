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

// Adapter class for handling vacation items in a RecyclerView
public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
    private List<Vacation> vacations;
    private final Context context;
    private final LayoutInflater inflater;

    // Constructor initializing context, vacations list, and inflater
    public VacationAdapter(Context context, List<Vacation> vacations) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.vacations = vacations;
    }

    // ViewHolder class
    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;

        // Constructor initializing the view and setting up click listener
        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView = itemView.findViewById(R.id.vacationTextView);

            // Set up click listener to open vacation details
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Vacation current = vacations.get(position);
                Intent intent = new Intent(context, VacationDetails.class); // Create intent to open vacation details
                intent.putExtra("vacationId", current.getVacationId());
                intent.putExtra("vacationTitle", current.getVacationTitle());
                intent.putExtra("vacationHotel", current.getVacationHotel());
                intent.putExtra("startDate", current.getStartDate());
                intent.putExtra("endDate", current.getEndDate());
                context.startActivity(intent);
            });
        }
    }

    // Creates new ViewHolder and inflates the vacation list item layout
    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    // Binds the data vacations to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        if (vacations != null) {
            Vacation current = vacations.get(position);
            holder.vacationItemView.setText(current.getVacationTitle());
        } else {
            holder.vacationItemView.setText("No Vacation Title");
        }
    }

    // Returns the total number of items in the list
    @Override
    public int getItemCount() {
        if (vacations != null) {
            return vacations.size();
        } else {
            return 0;
        }
    }

    // Method to update the vacation list and notify the adapter
    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
        notifyDataSetChanged();
    }
}
