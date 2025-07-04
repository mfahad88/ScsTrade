package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.scstrade.R;
import com.example.scstrade.databinding.FileUploadCameraBinding;
import com.example.scstrade.helper.Utils;
import com.google.android.material.card.MaterialCardView;

/**
 * Custom view that lets the user pick / capture a single image and
 * shows the file name.  Launchers are injected from the host Fragment/Activity.
 */
public class FileUploadCamera extends MaterialCardView {

    /* ----------------------------- state -------------------------------- */

    public FileUploadCameraBinding binding;

    public void setFileName(@Nullable String fileName) {
        this.fileName = fileName;
    }

    private @Nullable String fileName;          // shown in UI
    private @Nullable String base64;            // encoded image

    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<Uri>    cameraLauncher;
    private Uri cameraUri;                      // where full‑res photo is written

    /* Optional public accessors */
    public @Nullable String getFileName() { return fileName; }
    public @Nullable String getBase64()   { return base64;   }

    /* Expose internal buttons if you still need them outside */
    public CardView cardUpload;                 // camera icon card
    public MaterialCardView materialSelect;     // blue pill

    /* --------------------------- constructors --------------------------- */

    public FileUploadCamera(Context c, AttributeSet a)           { super(c, a); init(c, a); }
    public FileUploadCamera(Context c, AttributeSet a, int def)  { super(c, a, def); init(c, a); }

    /* ----------------------------- setup -------------------------------- */

    private void init(Context context, AttributeSet attrs) {
        binding = FileUploadCameraBinding.inflate(LayoutInflater.from(context), this, /*attach*/true);

        /* ---- XML attributes (title + optional colour) ---- */
        if (attrs != null) {
            TypedArray ta = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.FileUploadCamera, 0, 0);
            try {
                String title = ta.getString(R.styleable.FileUploadCamera_titleFileUpload);
                int    color = ta.getColor (R.styleable.FileUploadCamera_colorFileUpload,
                        Color.parseColor("#1A73E8"));
                binding.nomineeNic.setText(title == null ? "" : title);
                binding.materialSelect.setCardBackgroundColor(color);
            } finally { ta.recycle(); }
        }

        /* ---- initial empty state ---- */
        updateVisuals(false);

        /* ---- internal click listeners ---- */
        binding.materialSelect.setOnClickListener(v -> {
            if (galleryLauncher != null) galleryLauncher.launch("image/*");
        });

        binding.cardUpload.setOnClickListener(v -> {
            if (cameraLauncher != null && cameraUri != null) cameraLauncher.launch(cameraUri);
        });

        /* expose sub‑views if caller still wants them */
        cardUpload     = binding.cardUpload;
        materialSelect = binding.materialSelect;
    }

    /* ---------------------- launcher injection API ---------------------- */

    /** Call from Fragment/Activity to enable gallery picking. */
    public void setGalleryLauncher(@NonNull ActivityResultLauncher<String> launcher) {
        this.galleryLauncher = launcher;
    }

    /**
     * Call from Fragment/Activity to enable camera capture.
     * @param launcher  ActivityResultContracts.TakePicture launcher
     * @param outputUri Destination Uri created via FileProvider
     */
    public void setCameraLauncher(@NonNull ActivityResultLauncher<Uri> launcher,
                                  @NonNull Uri outputUri) {
        this.cameraLauncher = launcher;
        this.cameraUri      = outputUri;
    }

    /**
     * Call from Fragment/Activity when an image has been obtained
     * (either gallery or camera).  Handles filename, Base64 and UI update.
     */
    public void bindImage(@NonNull Uri uri) {
        Context ctx  = getContext();
        fileName     = Utils.Companion.getFileNameFromUri(ctx, uri);
        base64       = Utils.Companion.convertImageUriToBase64(ctx, uri);
        updateVisuals(true);
    }

    /* --------------------------- ui helpers ----------------------------- */

    public void updateVisuals(boolean hasFile) {
        if (hasFile) {
            binding.fileName.setText(fileName);
            binding.fileName.setVisibility(View.VISIBLE);
            binding.uploadIcon.setImageDrawable(
                    ContextCompat.getDrawable(getContext(), R.drawable.icon_camera));

//            binding.uploadIcon.setImageDrawable(
//                    ContextCompat.getDrawable(getContext(), R.drawable.baseline_delete_24));

            binding.materialSelect.setCardBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color.md_theme_primary));

            binding.selectFile.setText("File Added");
        } else {
            binding.fileName.setVisibility(View.INVISIBLE);
            binding.uploadIcon.setImageDrawable(
                    ContextCompat.getDrawable(getContext(), R.drawable.icon_camera));

            binding.materialSelect.setCardBackgroundColor(Color.parseColor("#1A73E8"));
            binding.selectFile.setText("Select File");
        }
    }
}
