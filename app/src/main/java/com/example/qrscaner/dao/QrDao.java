package com.example.qrscaner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.qrscaner.Model.QrScan;

import java.util.List;

@Dao
public interface QrDao {
    @Query("select * from qr_history order by date desc")
    List<QrScan> getListQrHistory();

    @Insert
    void insertQr(QrScan qrScan);
    @Delete
    void deleteQr(QrScan qrScan);
}
