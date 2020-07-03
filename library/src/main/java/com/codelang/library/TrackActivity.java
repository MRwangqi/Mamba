package com.codelang.library;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codelang.api.annotation.Track;
import com.codelang.library.bean.Student;

/**
 * @author codelang
 */
public class TrackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
    }

    boolean isOpen = true;


    public void track(View view) {
        if (isOpen) {
            open("hello", 1.0f, 2.0, 3L);
        } else {
            short a = 1;
            byte b = 2;
            char c = 'C';
            close(new Student("xiaoming"), a, b, c, false);
        }
        isOpen = !isOpen;
    }

    @Track
    private void open(String t, float a, double b, long c) {
        Toast.makeText(this, "What can I say？Mamba out", Toast.LENGTH_SHORT).show();
    }


    @Track
    private void close(Student stu, short a, byte b, char c, boolean d) {
        Toast.makeText(this, "Mamba is back", Toast.LENGTH_SHORT).show();
    }


}

