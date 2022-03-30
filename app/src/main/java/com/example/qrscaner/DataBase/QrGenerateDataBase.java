package com.example.qrscaner.DataBase;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.qrscaner.Model.QrGenerate;
import com.example.qrscaner.dao.QrDao;
import com.example.qrscaner.dao.QrGenerateDao;

@Database(entities = {QrGenerate.class}, version = 1)
public abstract class QrGenerateDataBase extends RoomDatabase {
    private static final String DB_NAME = "qr_gen.db";
    private static QrGenerateDataBase instance;
    public static synchronized QrGenerateDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, QrGenerateDataBase.class, DB_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract QrGenerateDao qrGenerateDao();
}
