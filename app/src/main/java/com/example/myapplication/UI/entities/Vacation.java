package com.example.myapplication.UI.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entity, basically a table to be stored in the Room database
@Entity(tableName = "vacations")
public class Vacation {

    @PrimaryKey(autoGenerate = true)
    public int vacationId;
    private String vacationTitle;
    private String vacationHotel;
    private String startDate;
    private String endDate;

    // Constructors
    public Vacation(int vacationId, String vacationTitle, String vacationHotel, String startDate, String endDate) {
        this.vacationId = vacationId;
        this.vacationTitle = vacationTitle;
        this.vacationHotel = vacationHotel;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getter and Setter methods
    public String toString() {
        return vacationTitle;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    public String getVacationTitle() {
        return vacationTitle;
    }

    public void setVacationTitle(String vacationTitle) {
        this.vacationTitle = vacationTitle;
    }

    public String getVacationHotel() {
        return vacationHotel;
    }

    public void setVacationHotel(String vacationHotel) {
        this.vacationHotel = vacationHotel;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
