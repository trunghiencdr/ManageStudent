package com.example.managestudent.response;

import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.example.managestudent.MainActivity;
import com.example.managestudent.database.Database;
import com.example.managestudent.models.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectResponse {

    Database database;
    private String dbName = "ManageStudent.sqlite";

    public SubjectResponse(Database database){
       this.database = database;
    }

    public  ArrayList<Subject> getAllSubjects(){
        ArrayList<Subject> list = new ArrayList<>();

        Cursor cursor = database.getData("SELECT * FROM Subject");
        while(cursor.moveToNext()){
            list.add(new Subject(cursor.getString(0), cursor.getString(1), cursor.getInt(2)));
        }
        cursor.close();
        return list;
    }

    public void insert(Subject newSubject){
        Log.d("test:" , newSubject.getMaMH()+"," + newSubject.getTenMH()+","
                + newSubject.getHeSo()+"");
        database.queryData("INSERT INTO Subject VALUES('"+newSubject.getMaMH()+"','" + newSubject.getTenMH()+"',"
                + newSubject.getHeSo()+")");
    }

    public void closs(){
        database.close();
    }
}
