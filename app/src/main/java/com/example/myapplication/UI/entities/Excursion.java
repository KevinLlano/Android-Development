package com.example.myapplication.UI.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursions")
public class Excursion {

    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionTitle;
    private String excursionDate;
    private int vacationID;

    // Constructors
    public Excursion(int excursionID, String excursionTitle, String excursionDate, int vacationID) {
        this.excursionID = excursionID;
        this.excursionTitle = excursionTitle;
        this.excursionDate = excursionDate;
        this.vacationID = vacationID;
    }


    // Getter and Setters
    public int getExcursionID() {
        return excursionID;
    }


    public String getExcursionTitle() {
        return excursionTitle;
    }

    public String getExcursionDate() {
        return excursionDate;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setExcursionTitle(String excursionTitle) {
        this.excursionTitle = excursionTitle;
    }


    public void setExcursionDate(String excursionDate) {
        this.excursionDate = excursionDate;
    }


}
