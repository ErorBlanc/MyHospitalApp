package com.example.myhospital.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myhospital.model.Doctor;

import java.util.List;

@Dao
public interface DoctorDao {
    @Insert
    void insertAll(List<Doctor> doctors);

    @Query("SELECT * FROM doctors")
    List<Doctor> getAllDoctors();

    @Query("SELECT * FROM doctors WHERE specialization = :spec")
    List<Doctor> getDoctorsBySpecialization(String spec);
}