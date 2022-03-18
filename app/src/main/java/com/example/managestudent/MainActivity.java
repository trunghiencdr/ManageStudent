package com.example.managestudent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.managestudent.activities.ManageClass;
import com.example.managestudent.activities.ManageStudent;
import com.example.managestudent.activities.ManageSubject;
import com.example.managestudent.activities.RegisterSubject;
import com.example.managestudent.database.Database;

public class MainActivity extends AppCompatActivity {

    CardView cvClass, cvSubject, cvStudent, cvMark;

    Database database;
    String dbName="ManageStudent.sqlite";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initDB();
        addControls();
        addEvents();
    }

    private void initDB() {
        // create db
        database = new Database(this);
        database.initAllTable();
//        database.clearAllTable();
//        database.clearData("Diem");
//


    }

    private void addControls(){
        cvClass = findViewById(R.id.cvClass);
        cvSubject = findViewById(R.id.cvSubject);
        cvStudent = findViewById(R.id.cvStudent);
        cvMark = findViewById(R.id.cvMark);
    }

    private void addEvents(){
        cvClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ManageClass.class);
                startActivity(intent);
            }
        });
        cvSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ManageSubject.class);
                startActivity(intent);
            }
        });
        cvStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ManageStudent.class);
                startActivity(intent);
            }
        });
        cvMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterSubject.class);
                startActivity(intent);
            }
        });
    }
}