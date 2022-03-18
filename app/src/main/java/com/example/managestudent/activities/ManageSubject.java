package com.example.managestudent.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managestudent.R;
import com.example.managestudent.adapter.SubjectAdapter;
import com.example.managestudent.database.Database;
import com.example.managestudent.models.Classroom;
import com.example.managestudent.models.Subject;
import com.example.managestudent.response.SubjectResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ManageSubject extends AppCompatActivity {

    ListView lvSubject;
    ArrayList<Subject> listSubjects;
    SubjectAdapter subjectAdapter;

    FloatingActionButton btnAddSubject;
    SubjectResponse subjectResponse;
    Database database;
    String dbName = "ManageStudent.sqlite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_subject);


        addEvents();
        addControls();
    }

    private void addEvents(){

        lvSubject = findViewById(R.id.lvSubject);
        btnAddSubject = findViewById(R.id.btnAddSubject);

        database = new Database(ManageSubject.this);
        listSubjects = new ArrayList<>();
        listSubjects=database.getAllSubjects();
        subjectAdapter = new SubjectAdapter(this, R.layout.custom_subject, listSubjects);
        lvSubject.setAdapter(subjectAdapter);



    }

    private void addControls(){
        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dialog("ADD", -1);
            }
        });
        lvSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog("UPDATE", i);
            }
        });
    }

    private void dialog(String loai, int index){

        Dialog dialog = new Dialog(ManageSubject.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_subject);
        // add class
        TextView titleDialogAddSubject = dialog.findViewById(R.id.titleDialogAddSubject);
        EditText txtMaMH = dialog.findViewById(R.id.txtMaMH);
        EditText txtTenMH = dialog.findViewById(R.id.txtTenMH);
        EditText txtHeSo = dialog.findViewById(R.id.txtHeSo);
        Button btnConfirmAddSubject = dialog.findViewById(R.id.btnConfirmAddSubject);

        if(loai.equals("ADD")){
            titleDialogAddSubject.setText("ADD NEW SUBJECT");
            btnConfirmAddSubject.setText("ADD");
            btnConfirmAddSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Subject newSubject = new Subject(txtMaMH.getText().toString(), txtTenMH.getText().toString(), Integer.parseInt(txtHeSo.getText().toString()));


                    boolean result = database.addSubject(newSubject);

                    if(result){

                        listSubjects.add(newSubject);
                        subjectAdapter.notifyDataSetChanged();
                        Toast.makeText(ManageSubject.this, "Succesful!", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(ManageSubject.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            });


        }else{
            Subject subject = listSubjects.get(index);
            txtMaMH.setText(subject.getMaMH());
            txtTenMH.setText(subject.getTenMH());
            txtHeSo.setText(subject.getHeSo()+"");
            titleDialogAddSubject.setText("UPDATE SUBJECT");
            btnConfirmAddSubject.setText("UPDATE");
            btnConfirmAddSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Subject newSubject = new Subject(txtMaMH.getText().toString(), txtTenMH.getText().toString(), Integer.parseInt(txtHeSo.getText().toString()));


                    boolean result=database.updateSubject(listSubjects.get(index).getMaMH(), newSubject);
                    if(result){

                        listSubjects.remove(index);
                        listSubjects.add(index, newSubject);
                        subjectAdapter.notifyDataSetChanged();
                        Toast.makeText(ManageSubject.this, "Succesful!", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(ManageSubject.this, "Failed!", Toast.LENGTH_SHORT).show();



                }
            });
        }
        dialog.show();
    }
}