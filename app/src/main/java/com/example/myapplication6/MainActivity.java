package com.example.myapplication6;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private final Context thisContext = this;

    Button mainButton;
    EditText mainEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainButton = (Button) findViewById(R.id.main_button);
        mainEditText = (EditText) findViewById(R.id.main_edit_text);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodName = mainEditText.getText().toString();

                if (foodName.length() > 0) {
                    String savedValue = mainEditText.getText().toString();
                    stopService(new Intent(thisContext, BannerService.class));
                    Intent serviceIntent = new Intent(thisContext, BannerService.class);
                    serviceIntent.putExtra("value", savedValue);
                    startService(serviceIntent);

                    mainEditText.setHintTextColor(Color.GREEN);
                    mainEditText.setEnabled(false);
                } else {
                    mainEditText.setText("");
                    mainEditText.setHint(getResources().getString(R.string.enter_food));
                    mainEditText.setHintTextColor(Color.RED);
                }
            }
        });


        if (!Settings.canDrawOverlays(this)) {
            requestPermissions();
        }

        while (!Settings.canDrawOverlays(this)) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        Intent serviceIntent = new Intent(this, BannerService.class);
        serviceIntent.putExtra("value", "");
        startService(serviceIntent);
    }

    public void requestPermissions() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 100);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, BannerService.class));
    }
}