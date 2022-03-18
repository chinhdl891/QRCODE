package com.example.qrscaner.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrscaner.Adapter.CategoryQCAdapter;
import com.example.qrscaner.Model.GenerateItem;
import com.example.qrscaner.Model.OptionGenerate;
import com.example.qrscaner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerateFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GenerateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenderateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenerateFragment newInstance(String param1, String param2) {
        GenerateFragment fragment = new GenerateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerViewGenerate;
        View view = inflater.inflate(R.layout.fragment_generate, container, false);
        recyclerViewGenerate = view.findViewById(R.id.recyclerOption);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewGenerate.setLayoutManager(layoutManager);
        CategoryQCAdapter categoryQCAdapter = new CategoryQCAdapter(getOpGen(), getActivity());
        recyclerViewGenerate.setAdapter(categoryQCAdapter);
        return view;
    }

    private List<OptionGenerate> getOpGen() {
        List<OptionGenerate> optionGenerates = new ArrayList<>();
        optionGenerates.add(new OptionGenerate(0, getListQR(), "QR CODE"));
        optionGenerates.add(new OptionGenerate(1, getListBar(), "QR CODE"));

        return optionGenerates;
    }

    private List<GenerateItem> getListBar() {
        List<GenerateItem> generateItems = new ArrayList<>();

        generateItems.add(new GenerateItem(0, R.drawable.add_call, "Phone"));
        generateItems.add(new GenerateItem(0, getResources().getIdentifier("add_call", "drawable", "com.example.qrscaner"), "Phone"));

        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));

        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));
        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));
        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));
        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));
        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));
        return generateItems;
    }

    private List<GenerateItem> getListQR() {
        List<GenerateItem> generateItems = new ArrayList<>();

        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));
        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));

        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));

        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));
        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));
        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));
        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));
        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/add_call", null, null), "Phone"));
        return generateItems;
    }
}