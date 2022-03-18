package com.example.managestudent.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.managestudent.R;
import com.example.managestudent.adapter.MarkAdapter;
import com.example.managestudent.database.Database;
import com.example.managestudent.models.Mark;
import com.example.managestudent.models.Student;

import java.util.ArrayList;

public class StudentMark extends AppCompatActivity {

    ListView lvMark;
    TextView lbAverageScore;
    MarkAdapter markAdapter;
    ArrayList<Mark> listMark;
    Database db ;
    Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_mark);

        setControls();
        initDatas();
        setEvents();
    }

    private void setControls(){
        lvMark = findViewById(R.id.lvStudentMark);
        lbAverageScore = findViewById(R.id.lbDTB);

    }

    private void initDatas(){
        db = new Database(StudentMark.this);
        Intent intent = getIntent();
        this.student = (Student) intent.getSerializableExtra("student");
        listMark = db.getStudentMark(student.getMaHocSinh());
        lbAverageScore.setText("Average score: " +db.calAverageScore(student.getMaHocSinh())+"");
        markAdapter = new MarkAdapter(this, R.layout.listview_student_mark, listMark);
        lvMark.setAdapter(markAdapter);
    }


    private void setEvents(){

    }
}