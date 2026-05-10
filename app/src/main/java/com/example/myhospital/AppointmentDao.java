package com.example.myhospital;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AppointmentDao {
    @Insert
    void insert(Appointment appointment);

    @Query("SELECT * FROM appointments WHERE userId = :userId ORDER BY date DESC")
    List<Appointment> getAppointmentsForUser(int userId);

    @Query("SELECT * FROM appointments WHERE userId = :userId AND status = 'upcoming' ORDER BY date ASC")
    List<Appointment> getUpcomingAppointments(int userId);

    @Delete
    void delete(Appointment appointment);
}