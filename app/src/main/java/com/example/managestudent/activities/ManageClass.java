package com.example.managestudent.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
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
import com.example.managestudent.adapter.ClassroomAdapter;
import com.example.managestudent.database.Database;
import com.example.managestudent.models.Classroom;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ManageClass extends AppCompatActivity {

    FloatingActionButton btnAddClass;
    ListView lvClass;
    ArrayList<Classroom> listClassroom;
    ClassroomAdapter classroomAdapter;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_class);


        addControls();
        addEvents();
    }

    private void addControls(){
        btnAddClass = findViewById(R.id.btnAddClass);
        lvClass = findViewById(R.id.lvClass);

        db = new Database(ManageClass.this);
        listClassroom = db.getAllClassrooms();
        classroomAdapter = new ClassroomAdapter(ManageClass.this, R.layout.custom_list_classroom, listClassroom);
        lvClass.setAdapter(classroomAdapter);
    }


    private void dialog(String loai, int index){

            Dialog dialog = new Dialog(ManageClass.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_add_class);
            // add class
            TextView lbDialogClass = dialog.findViewById(R.id.lbDialogClass);
            EditText txtLop = dialog.findViewById(R.id.txtLop);
            EditText txtChuNhiem = dialog.findViewById(R.id.txtChuNhiem);
            Button btnConfirmAddClass = dialog.findViewById(R.id.btnConfirmAddClass);

        if(loai.equals("ADD")){
            lbDialogClass.setText("ADD NEW CLASSROOM");
            btnConfirmAddClass.setText("ADD");
            btnConfirmAddClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Classroom newClass = new Classroom(txtLop.getText().toString(), txtChuNhiem.getText().toString());


                    boolean result = db.addClass(newClass);

                    if(result){

                        listClassroom.add(newClass);
                        classroomAdapter.notifyDataSetChanged();
                        Toast.makeText(ManageClass.this, "Succesful!", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(ManageClass.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            });


        }else{
            Classroom classroom = listClassroom.get(index);
            txtLop.setText(classroom.getLOP());
            txtChuNhiem.setText(classroom.getCHUNHIEM());
            lbDialogClass.setText("UPDATE CLASSROOM");
            btnConfirmAddClass.setText("UPDATE");
            btnConfirmAddClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Classroom newClass = new Classroom(txtLop.getText().toString(), txtChuNhiem.getText().toString());


                    boolean result=db.updateClassroom(listClassroom.get(index).getLOP(), newClass);
                    if(result){

                        listClassroom.remove(index);
                        listClassroom.add(index, newClass);
                        classroomAdapter.notifyDataSetChanged();
                        Toast.makeText(ManageClass.this, "Succesful!", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(ManageClass.this, "Failed!", Toast.LENGTH_SHORT).show();



                }
            });
        }
        dialog.show();
    }
    private void addEvents(){


        lvClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog("UPDATE", i);

            }
        });
        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog("ADD", -1);
            }
        });
    }


}