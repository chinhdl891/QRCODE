package com.example.qrscaner.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.qrscaner.models.Language;
import com.example.qrscaner.R;

public class DialogFragment extends AppCompatDialogFragment {
    private NumberPicker nbpDialogLang;
    private String[] strLanguages;
    private SelectLanguage iSelectLanguage;
    private Button btnDialogSelectedLang;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iSelectLanguage = (SelectLanguage) context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_language, container, false);
        strLanguages = Language.listStrCountry();
        init(view);
        if (getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return view;

    }

    private void init(View view) {
        btnDialogSelectedLang = view.findViewById(R.id.btn_dialog_lang_select_lang);
        nbpDialogLang = view.findViewById(R.id.nbp_dialog_lang_selectLang);
        nbpDialogLang.setMinValue(0);
        nbpDialogLang.setMaxValue(strLanguages.length - 1);
        nbpDialogLang.setDisplayedValues(strLanguages);
        btnDialogSelectedLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             iSelectLanguage.onSelectListener(nbpDialogLang.getValue());
             dismiss();
            }
        });


    }

    public interface SelectLanguage {
        void onSelectListener(int i);

    }
}
