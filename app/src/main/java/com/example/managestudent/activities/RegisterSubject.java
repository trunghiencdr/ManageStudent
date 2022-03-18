package com.example.managestudent.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.UiAutomation;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.managestudent.R;
import com.example.managestudent.adapter.StudentAdapter;
import com.example.managestudent.database.Database;
import com.example.managestudent.models.Classroom;
import com.example.managestudent.models.Student;
import com.example.managestudent.models.Subject;

import java.util.ArrayList;

public class RegisterSubject extends AppCompatActivity {

    Spinner spinnerClass, spinnerSubject;
    ListView lvNoRegister, lvRegister;

    ArrayList<Classroom> listClass;
    ArrayList<Subject> listSubject;
    ArrayAdapter<Classroom> classAdapter;
    ArrayAdapter<Subject> subjectAdapter;
    ArrayList<Student> listNoRegister, listRegister;
    StudentAdapter noRegisterAdapter, registerAdapter;
    EditText editTextTest;

    Database database;

    TabHost tabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_subject);

        addControls();
        initData();
        addEvents();

    }

    private void addControls() {
        spinnerClass = findViewById(R.id.spinnerClassForMark);
        spinnerSubject = findViewById(R.id.spinnerSubjectForMark);
        lvNoRegister = findViewById(R.id.lvNoRegister);
        lvRegister = findViewById(R.id.lvRegister);





        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabNoRegister = tabHost.newTabSpec("noRegister");
        tabNoRegister.setIndicator("NO REGISTER");
        tabNoRegister.setContent(R.id.tabNoRegister);
        tabHost.addTab(tabNoRegister);

        TabHost.TabSpec tabResigter = tabHost.newTabSpec("register");
        tabResigter.setIndicator("REGISTER");
        tabResigter.setContent(R.id.tabRegister);
        tabHost.addTab(tabResigter);
    }

    private void initData() {
        database = new Database(RegisterSubject.this);
        listSubject = database.getAllSubjects();
        listClass = database.getAllClassrooms();

        classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listClass);
        subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listSubject);

        spinnerClass.setAdapter(classAdapter);
        spinnerSubject.setAdapter(subjectAdapter);

        listClass = database.getAllClassrooms();
        listSubject = database.getAllSubjects();


    }

    private void addEvents() {
        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (listClass.size() > 0 && listSubject.size() > 0) {
                    Subject subject = (Subject) spinnerSubject.getSelectedItem();
                    Classroom classroom = listClass.get(i);
                    listNoRegister = database.getStudentsNoRegister(classroom.getLOP(), subject.getMaMH());
                    listRegister = database.getStudentsRegister(classroom.getLOP(), subject.getMaMH());


                    noRegisterAdapter = new StudentAdapter(RegisterSubject.this, R.layout.custom_listview_student, listNoRegister
                            , "noregister", subject.getMaMH());
                    lvNoRegister.setAdapter(noRegisterAdapter);


                    registerAdapter = new StudentAdapter(RegisterSubject.this, R.layout.custom_listview_student, listRegister
                            , "register", subject.getMaMH());
                    lvRegister.setAdapter(registerAdapter);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (listClass.size() > 0 && listSubject.size() > 0) {
                    Classroom classroom = (Classroom) spinnerClass.getSelectedItem();
                    Subject subject = listSubject.get(i);

                    listNoRegister = database.getStudentsNoRegister(classroom.getLOP(), subject.getMaMH());
                    listRegister = database.getStudentsRegister(classroom.getLOP(), subject.getMaMH());

                    noRegisterAdapter = new StudentAdapter(RegisterSubject.this, R.layout.custom_listview_student, listNoRegister
                            , "noregister", subject.getMaMH());
                    lvNoRegister.setAdapter(noRegisterAdapter);


                    registerAdapter = new StudentAdapter(RegisterSubject.this, R.layout.custom_listview_student, listRegister
                            , "register", subject.getMaMH());
                    lvRegister.setAdapter(registerAdapter);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                Classroom classroom = (Classroom) spinnerClass.getSelectedItem();
                Subject subject = (Subject) spinnerSubject.getSelectedItem();
                if (s.equals("noRegister")) {
                    listNoRegister = database.getStudentsNoRegister(classroom.getLOP(), subject.getMaMH());

                    noRegisterAdapter = new StudentAdapter(RegisterSubject.this, R.layout.custom_listview_student, listNoRegister
                            , "noregister", subject.getMaMH());
                    lvNoRegister.setAdapter(noRegisterAdapter);

//                    Toast.makeText(RegisterSubject.this, "noRegister", Toast.LENGTH_SHORT).show();
                } else {
                    listRegister = database.getStudentsRegister(classroom.getLOP(), subject.getMaMH());

                    registerAdapter = new StudentAdapter(RegisterSubject.this, R.layout.custom_listview_student, listRegister
                            , "register", subject.getMaMH());
                    lvRegister.setAdapter(registerAdapter);

//                    Toast.makeText(RegisterSubject.this, "Register", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}