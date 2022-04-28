package com.example.qrscaner.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.qrscaner.models.QrScan;
import com.example.qrscaner.dao.QrDao;

@Database(entities = {QrScan.class}, version = 2)
public abstract class QrHistoryDatabase extends RoomDatabase {


    private static final String DB_NAME = "qr.db";
    private static QrHistoryDatabase instance;

    public static synchronized QrHistoryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, QrHistoryDatabase.class, DB_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract QrDao qrDao();
}
