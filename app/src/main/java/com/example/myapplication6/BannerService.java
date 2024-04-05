package com.example.myapplication6;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class BannerService extends Service {
    String inputIntent = null;
    WindowManager windowManager;
    View rootView;
    TextView serviceTextView;
    Button serviceButton;
    public BannerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        rootView = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.banner_service, null);
        serviceTextView = rootView.findViewById(R.id.service_text_view);
        serviceButton = rootView.findViewById(R.id.service_button);

        serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        inputIntent = intent.getStringExtra("value");

        final WindowManager.LayoutParams params = new
                WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        String text;
        if (inputIntent.length() > 0) {
            serviceTextView.setText("");
            text = "Блюдо: " + inputIntent + " ";
            serviceTextView.setText(text);
        } else {
            text = "Введите название блюда";
            serviceTextView.setText(text);
        }

        serviceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTextViewClick();
            }
        });

        windowManager.addView(rootView, params);
        params.gravity = Gravity.TOP | Gravity.CENTER;
        windowManager.updateViewLayout(rootView, params);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(rootView);
    }

    public void onTextViewClick() {
        Intent dialogIntent = new Intent(this, MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(dialogIntent);
        stopSelf();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}