package com.example.myapplication.UI.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "excursions",
        foreignKeys = @ForeignKey(
                entity = Vacation.class, // The referenced entity
                parentColumns = "vacationId",
                childColumns = "vacationId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index(value = "vacationId") // index for vacationId
)
public class Excursion {

    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionTitle;
    private String excursionDate;
    private int vacationId; // The foreign key column referencing Vacation table

    // Constructors
    public Excursion(int excursionID, String excursionTitle, String excursionDate, int vacationId) {
        this.excursionID = excursionID;
        this.excursionTitle = excursionTitle;
        this.excursionDate = excursionDate;
        this.vacationId = vacationId;
    }

    // Getters and Setters
    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionTitle() {
        return excursionTitle;
    }

    public void setExcursionTitle(String excursionTitle) {
        this.excursionTitle = excursionTitle;
    }

    public String getExcursionDate() {
        return excursionDate;
    }

    public void setExcursionDate(String excursionDate) {
        this.excursionDate = excursionDate;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }
}

