package com.example.myhospital;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "appointments")
public class Appointment {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public int doctorId;
    public String doctorName;
    public String specialization;
    public String date;
    public String time;
    public String status;
}