package com.example.akvandroidapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import com.yandex.runtime.image.ImageProvider;

import static com.example.akvandroidapp.util.Constants.FONT_SIZE;
import static com.example.akvandroidapp.util.Constants.MARGIN_SIZE;
import static com.example.akvandroidapp.util.Constants.STROKE_SIZE;


public class TextImageProvider extends ImageProvider {
    @Override
    public String getId() {
        return "text_" + text;
    }

    private final String text;
    private Context context;
    @Override
    public Bitmap getImage() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        Paint textPaint = new Paint();
        textPaint.setTextSize(FONT_SIZE * metrics.density);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);

        float widthF = textPaint.measureText(text);
        Paint.FontMetrics textMetrics = textPaint.getFontMetrics();
        float heightF = Math.abs(textMetrics.bottom) + Math.abs(textMetrics.top);
        float textRadius = (float)Math.sqrt(widthF * widthF + heightF * heightF) / 2;
        float internalRadius = textRadius + MARGIN_SIZE * metrics.density;
        float externalRadius = internalRadius + STROKE_SIZE * metrics.density;

        int width = (int) (2 * externalRadius + 0.5);

        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(Color.RED);
        canvas.drawCircle(width / 2, width / 2, externalRadius, backgroundPaint);

        backgroundPaint.setColor(Color.WHITE);
        canvas.drawCircle(width / 2, width / 2, internalRadius, backgroundPaint);

        canvas.drawText(
                text,
                width / 2,
                width / 2 - (textMetrics.ascent + textMetrics.descent) / 2,
                textPaint);

        return bitmap;
    }

    public TextImageProvider(Context context, String text) {
        this.text = text;
        this.context = context;
    }
}
