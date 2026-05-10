package com.example.myhospital;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String fullName;
    public String email;
    public String password;
    public String phone;
}