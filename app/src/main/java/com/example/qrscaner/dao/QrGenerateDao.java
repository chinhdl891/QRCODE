package com.example.qrscaner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.qrscaner.models.QrGenerate;

import java.util.List;

@Dao
public interface QrGenerateDao {
    @Query("select * from qr_generate order by date desc")
    List<QrGenerate> getListQrGenerate();
    @Insert
    void insertQrGenerate(QrGenerate qrGenerate);
    @Delete
    void deleteQrGenerate(QrGenerate qrGenerate);


}
