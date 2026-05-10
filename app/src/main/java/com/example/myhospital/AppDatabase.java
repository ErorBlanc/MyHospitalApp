package com.example.myhospital;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Doctor.class, Appointment.class,}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract UserDao userDao();
    public abstract DoctorDao doctorDao();
    public abstract AppointmentDao appointmentDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "myhospital_db"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}