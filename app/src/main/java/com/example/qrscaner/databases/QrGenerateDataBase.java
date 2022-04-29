package com.example.qrscaner.databases;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.qrscaner.models.QrGenerate;
import com.example.qrscaner.dao.QrGenerateDao;

@Database(entities = {QrGenerate.class}, version = 2)
public abstract class QrGenerateDataBase extends RoomDatabase {
    static Migration migration = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table qr_generate add column color integer  ");
        }
    };
    private static final String DB_NAME = "qr_gen.db";
    private static QrGenerateDataBase instance;
    public static synchronized QrGenerateDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, QrGenerateDataBase.class, DB_NAME).allowMainThreadQueries().addMigrations(migration).build();
        }
        return instance;
    }
    public abstract QrGenerateDao qrGenerateDao();
}
