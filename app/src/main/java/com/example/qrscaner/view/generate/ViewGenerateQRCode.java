package com.example.qrscaner.view.generate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.qrscaner.R;

public class ViewGenerateQRCode extends ConstraintLayout {
    Context mContext;
    View mRootView;

    public ViewGenerateQRCode(@NonNull Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public ViewGenerateQRCode(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();

    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = layoutInflater.inflate(R.layout.text_renerate_view, this, true);

    }

    public void setUpData(int id) {
        if (id==0){

        }
    }

}
