package com.example.myhospital.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "doctors")
public class Doctor {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String specialization;
    public String cabinet;
}