package com.example.qrscaner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscaner.Model.OptionGenerate;
import com.example.qrscaner.R;

import java.util.List;

public class CategoryQCAdapter extends RecyclerView.Adapter<CategoryQCAdapter.ViewHolderCategoryOption> {
    List<OptionGenerate> optionGenerates;
    Context context;

    public CategoryQCAdapter(List<OptionGenerate> optionGenerates, Context context) {
        this.optionGenerates = optionGenerates;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderCategoryOption onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.category_cr_qr,parent,false);
      return new ViewHolderCategoryOption(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCategoryOption holder, int position) {
        OptionGenerate optionGenerate = optionGenerates.get(position);
        holder.txtCategory.setText(optionGenerate.getName());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,3, LinearLayoutManager.VERTICAL,false);
        OptionGenerateAdapter optionGenerateAdapter = new OptionGenerateAdapter();
        optionGenerateAdapter.setGenerateItems(optionGenerate.getGenerateItems());
        holder.recyclerViewCategory.setAdapter(optionGenerateAdapter);
        holder.recyclerViewCategory.setLayoutManager(layoutManager);

    }

    @Override
    public int getItemCount() {
        return optionGenerates.size();
    }

    class ViewHolderCategoryOption extends RecyclerView.ViewHolder{
        RecyclerView recyclerViewCategory;
        TextView txtCategory;
        public ViewHolderCategoryOption(@NonNull View itemView) {
            super(itemView);
            recyclerViewCategory = itemView.findViewById(R.id.recyclerCategory);
            txtCategory = itemView.findViewById(R.id.txtCategoryName);

        }
    }
}
