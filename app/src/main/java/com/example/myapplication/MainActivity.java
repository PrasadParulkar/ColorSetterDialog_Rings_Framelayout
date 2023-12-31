package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.flag.BubbleFlag;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class MainActivity extends AppCompatActivity {


    private View mColorPreview;

    private int mDefaultColor;

    FrameLayout mFramelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.button_background);


        Button mPickColorButton = findViewById(R.id.pick_color_button);
        Button mSetColorButton = findViewById(R.id.set_color_button);


        mColorPreview = findViewById(R.id.preview_selected_color);
        mFramelayout = findViewById(R.id.softTriggerIcon);


        mDefaultColor = 0;


        mPickColorButton.setOnClickListener(
                v -> dialog());

        mSetColorButton.setOnClickListener(
                v -> {
                    for (int i = 2; i >= 0; i--) {
                        assert layerDrawable != null;
                        Drawable layer = layerDrawable.getDrawable(i);
                        if (layer instanceof GradientDrawable) {
                            GradientDrawable gradientDrawable = (GradientDrawable) layer;
                            int alpha = calculateAlphaValueForIndex(i);
                            gradientDrawable.setColor(ColorUtils.setAlphaComponent(mDefaultColor, alpha));
                        }
                        layerDrawable.setDrawableByLayerId(layerDrawable.getId(i), layer);
                    }
                    mFramelayout.setBackgroundDrawable(layerDrawable);
                });
    }

    private int calculateAlphaValueForIndex(int i) {
        int minAlpha = 80;
        return minAlpha + (i * 50);
    }

    private void dialog() {
        ColorPickerDialog.Builder builder =
                new ColorPickerDialog.Builder(this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("Test")
                        .setPositiveButton(
                                getString(R.string.confirm),
                                (ColorEnvelopeListener) (envelope, fromUser) -> setLayoutColor(envelope))
                        .setNegativeButton(
                                getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.getColorPickerView().setFlagView(new BubbleFlag(this));
        builder.show();
    }

    @SuppressLint("SetTextI18n")
    private void setLayoutColor(ColorEnvelope envelope) {
        mDefaultColor = envelope.getColor();
        mColorPreview.setBackgroundColor(envelope.getColor());
    }
}
