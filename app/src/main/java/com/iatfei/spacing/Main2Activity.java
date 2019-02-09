package com.iatfei.spacing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {

    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

    private Button button2;
    private Button button4;
    private Button button7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"apps@iatfei.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "有 關 隔 格 app 事 宜 。");
        button2 = findViewById(R.id.button2);
        button4 = findViewById(R.id.button4);
        button7 = findViewById(R.id.button7);


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/fei0316/spacing-android/"));
                startActivity(browserIntent);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://iatfei.com/"));
                startActivity(browserIntent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(emailIntent, "發 送 電 郵"));
            }
        });
    }
}
