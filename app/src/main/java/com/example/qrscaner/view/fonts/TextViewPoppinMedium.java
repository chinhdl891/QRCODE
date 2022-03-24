package com.example.qrscaner.view.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class TextViewPoppinMedium extends AppCompatTextView  {
    private Context mContext;
    public TextViewPoppinMedium(@NonNull Context context) {
        super(context);
        setTypeFace();
    }

    public TextViewPoppinMedium(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeFace();
    }

    public TextViewPoppinMedium(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeFace();
    }
    private void setTypeFace(){
        setTypeface(Typeface.createFromAsset(mContext.getAssets(),"fonts/Poppins-Medium.ttf" ));
    }
}
