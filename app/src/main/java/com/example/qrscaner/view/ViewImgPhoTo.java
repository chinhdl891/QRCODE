package com.example.qrscaner.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ViewImgPhoTo extends androidx.appcompat.widget.AppCompatImageView {
    Context mContext;
    public ViewImgPhoTo(@NonNull Context context) {
        super(context);
    }

    public ViewImgPhoTo(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewImgPhoTo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
