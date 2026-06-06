package com.example.myhospital;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private final AppointmentDao appointmentDao;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        appointmentDao = db.appointmentDao();
    }

    public LiveData<List<Appointment>> getUserHistory(int userId) {
        return appointmentDao.getAppointmentsForUser(userId);
    }
}