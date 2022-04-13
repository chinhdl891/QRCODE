package com.example.qrscaner.view.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class TextViewPoppinRegular extends AppCompatTextView  {
    private Context mContext;
    public TextViewPoppinRegular(@NonNull Context context) {
        super(context);
        mContext = context;
        setTypeFace();
    }

    public TextViewPoppinRegular(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setTypeFace();
    }

    public TextViewPoppinRegular(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setTypeFace();
    }
    private void setTypeFace(){
        setTypeface(Typeface.createFromAsset(mContext.getAssets(),"fonts/Poppins-Medium.ttf" ));
    }
}
