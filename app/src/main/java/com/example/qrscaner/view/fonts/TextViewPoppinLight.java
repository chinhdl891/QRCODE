package com.example.qrscaner.view.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class TextViewPoppinLight extends AppCompatTextView  {
    private Context mContext;
    public TextViewPoppinLight(@NonNull Context context) {
        super(context);
        setTypeFace();
    }

    public TextViewPoppinLight(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeFace();
    }

    public TextViewPoppinLight(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeFace();
    }
    private void setTypeFace(){
        setTypeface(Typeface.createFromAsset(mContext.getAssets(),"fonts/Poppins-Light.ttf" ));
    }
}
