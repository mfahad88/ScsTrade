package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.scstrade.R;
import com.example.scstrade.databinding.FileUploadCameraBinding;
import com.google.android.material.card.MaterialCardView;

public class FileUploadCamera extends MaterialCardView {
    FileUploadCameraBinding binding;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        if(fileName!=null) {
            binding.fileName.setText(fileName);
            binding.fileName.setVisibility(View.VISIBLE);
            binding.imageView32.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.baseline_delete_24));
            binding.materialCardView8.setCardBackgroundColor(ContextCompat.getColor(getContext(),R.color.md_theme_primary));
        }
    }

    public FileUploadCamera(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding=FileUploadCameraBinding.inflate(LayoutInflater.from(context),this,true);
        if(attrs!=null){
            TypedArray a=context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.FileUploadCamera,
                    0,0
            );
            try{
                String title=a.getString(R.styleable.FileUploadCamera_titleFileUpload);
                int color=a.getColor(R.styleable.FileUploadCamera_colorFileUpload, Color.parseColor("#000000"));
                binding.nomineeNic.setText(title);
                binding.materialCardView8.setCardBackgroundColor(color);

            }finally {
                a.recycle();
            }
        }
    }


}
