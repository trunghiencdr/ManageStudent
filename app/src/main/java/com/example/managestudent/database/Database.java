package com.example.managestudent.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.managestudent.models.Classroom;
import com.example.managestudent.models.Mark;
import com.example.managestudent.models.Student;
import com.example.managestudent.models.Subject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Database extends SQLiteOpenHelper {

    public static Context context;
    public static String DB_NAME = "ManageStudent.sqlite";
    public static int DB_VERSION = 1;
    public Database(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public void select(String sql, Object... args){
        SQLiteDatabase database = getWritableDatabase();
        for(int i=0; i< args.length; i++){

        }

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Class(LOP NVARCHAR(200) PRIMARY KEY, CHUNHIEM NVARCHAR(200))");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Subject(MAMH NCHAR(20) PRIMARY KEY, TENMH NVARCHAR(200), HESO FLOAT)");



        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Student(MAHOCSINH NCHAR(20) PRIMARY KEY," +
                "HO NVARCHAR(100), TEN NVARCHAR(100), PHAI BOOLEAN, NGAYSINH date, LOP NVARCHAR(200), FOREIGN KEY(LOP) REFERENCES Class(LOP))");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS DIEM(MAHOCSINH NCHAR(20), MAMH NCHAR(20),DIEM FLOAT, " +
                "FOREIGN KEY(MAHOCSINH) REFERENCES Student(MAHOCSINH)," +
                "FOREIGN KEY(MAMH) REFERENCES Subject(MAMH)," +
                "PRIMARY KEY(MAHOCSINH, MAMH))");
    }

    public void initAllTable(){
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS Class(LOP NVARCHAR(200) PRIMARY KEY, CHUNHIEM NVARCHAR(200))");
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS Subject(MAMH NCHAR(20) PRIMARY KEY, TENMH NVARCHAR(200), HESO INT)");
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS Student(MAHOCSINH NCHAR(20) PRIMARY KEY," +
                "HO NVARCHAR(100), TEN NVARCHAR(100), PHAI BOOLEAN, NGAYSINH VARCHAR(100), LOP NVARCHAR(200) REFERENCES Class(LOP) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE)");

        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS DIEM(MAHOCSINH NCHAR(20), MAMH NCHAR(20),DIEM FLOAT, " +
                "FOREIGN KEY(MAHOCSINH) REFERENCES Student(MAHOCSINH) ON DELETE RESTRICT ON UPDATE CASCADE," +
                "FOREIGN KEY(MAMH) REFERENCES Subject(MAMH) ON DELETE RESTRICT ON UPDATE CASCADE," +
                "PRIMARY KEY(MAHOCSINH, MAMH))");

    }

    public void clearAllTable(){
        this.queryData("DROP TABLE DIEM");
        this.queryData("DROP TABLE Subject");
        this.queryData("DROP TABLE Student");
        this.queryData("DROP TABLE Class");


    }
    public void clearData(String tableName){
        int result = this.getWritableDatabase().delete( tableName,null, null);
        if(result==-1){
            System.out.println("delete " + tableName + " khong thanh cong");
        }else{
            System.out.println("delete " + tableName + " thanh cong" );
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Student");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Mark");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Subject");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Class");
        onCreate(sqLiteDatabase);
    }

    public boolean addClass(Classroom newClass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("LOP", newClass.getLOP());
        contentValues.put("CHUNHIEM", newClass.getCHUNHIEM());

        long result= db.insert("Class", null, contentValues);
        if(result==-1){
            return false;
        }
        return true;
    }

    public ArrayList<Classroom> getAllClassrooms(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Class",null);
        ArrayList<Classroom> list = new ArrayList<>();
        while(cursor.moveToNext()){
            list.add(new Classroom(cursor.getString(0), cursor.getString(1)));
        }
        cursor.close();
        return list;
    }

    public boolean deleteClassroom(String name)
    {

        try{
            this.queryData("PRAGMA foreign_keys=ON");
            this.getWritableDatabase().delete("Class", "LOP=?" , new String[]{name});
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public boolean updateClassroom(String id, Classroom newClassroom){

        ContentValues cv = new ContentValues();
        cv.put("LOP", newClassroom.getLOP());
        cv.put("CHUNHIEM", newClassroom.getCHUNHIEM());
        this.queryData("PRAGMA foreign_keys=ON");
        return this.getWritableDatabase().update("Class", cv,"LOP=?", new String[]{id}) >0;
    }

    public boolean addSubject(Subject newSubject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("MAMH", newSubject.getMaMH());
        contentValues.put("TENMH", newSubject.getTenMH());
        contentValues.put("HESO", newSubject.getHeSo());

        long result= db.insert("Subject", null, contentValues);
        if(result==-1){
            return false;
        }
        return true;
    }

    public ArrayList<Subject> getAllSubjects(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Subject",null);
        ArrayList<Subject> list = new ArrayList<>();
        while(cursor.moveToNext()){
            list.add(new Subject(cursor.getString(0), cursor.getString(1), cursor.getInt(2)));
        }
        cursor.close();
        return list;
    }

    public boolean deleteSubject(String name)
    {
        try{
            this.queryData("PRAGMA foreign_keys=ON");
            this.getWritableDatabase().delete("Subject", "MAMH=?" , new String[]{name});
            return true;
        }catch (Exception ex){

        }
        return false;
    }

    public boolean updateSubject(String id, Subject newSubject){

        ContentValues cv = new ContentValues();
        cv.put("MAMH", newSubject.getMaMH());
        cv.put("TENMH", newSubject.getTenMH());
        cv.put("HESO", newSubject.getHeSo());
        this.queryData("PRAGMA foreign_keys=ON");
        return this.getWritableDatabase().update("Subject", cv,"MAMH=?", new String[]{id}) >0;
    }

    //=======end subject
//==================================student===============================================
    public boolean addStudent(Student newStudent){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("MAHOCSINH", newStudent.getMaHocSinh());
        contentValues.put("HO", newStudent.getHo());
        contentValues.put("TEN", newStudent.getTen());
        contentValues.put("PHAI", newStudent.isPhai());
        contentValues.put("NGAYSINH", new SimpleDateFormat("dd/MM/yyyy").format(newStudent.getNgaySinh()));
        contentValues.put("LOP", newStudent.getLop());

        long result= db.insert("Student", null, contentValues);
        if(result==-1){
            return false;
        }
        return true;
    }

    public ArrayList<Student> getAllStudents(String lop) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Student where LOP='" + lop  +"'",null);
        ArrayList<Student> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        while(cursor.moveToNext()){
            Date date=new Date();
            try{
               date = simpleDateFormat.parse(cursor.getString(4));
            }catch (Exception ex){
                ex.printStackTrace();
                Log.d("bug:", "lỗi get date từ bảng student");
            }

            list.add(new Student(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3)>0, date, lop));
        }
        cursor.close();
        return list;
    }

    public boolean deleteStudent(String name)
    {
        try{
            this.queryData("PRAGMA foreign_keys=ON");
            this.getWritableDatabase().delete("Student", "MAHOCSINH=?" , new String[]{name});
            return true;
        }catch (Exception e){

        }
        return false;
    }

    public boolean updateStudent(String id, Student newStudent){

        ContentValues cv = new ContentValues();
        cv.put("MAHOCSINH", newStudent.getMaHocSinh());
        cv.put("HO", newStudent.getHo());
        cv.put("TEN", newStudent.getTen());
        cv.put("PHAI", newStudent.isPhai());
        cv.put("LOP", newStudent.getLop());
        this.queryData("PRAGMA foreign_keys=ON");
        return this.getWritableDatabase().update("Student", cv,"MAHOCSINH=?", new String[]{id}) >0;
    }
    //======== end student
    //======== start register
    public ArrayList<Student> getStudentsRegister(String lop, String maMH){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM (SELECT * FROM Student WHERE LOP='" + lop +"') as st," +
                "(SELECT * FROM DIEM WHERE MAMH='"+ maMH+"') as d " +
                "WHERE st.MAHOCSINH = d.MAHOCSINH";
        Cursor cursor = db.rawQuery(sql,null);
        ArrayList<Student> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        while(cursor.moveToNext()){
            Date date=new Date();
            try{
                date = simpleDateFormat.parse(cursor.getString(4));
            }catch (Exception ex){
                ex.printStackTrace();
                Log.d("bug:", "lỗi get date từ bảng student");
            }

            list.add(new Student(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3)>0, date, lop));
        }
        for(Student st: list){
            System.out.println(st);
        }
        cursor.close();
        return list;
    }

    public ArrayList<Student> getStudentsNoRegister(String lop, String maMH){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM (SELECT * FROM Student WHERE LOP='" + lop +"') as st " +
                "WHERE NOT EXISTS" +
                "(SELECT null " +
                    "FROM(SELECT * FROM DIEM WHERE MAMH='"+ maMH+"') as d " +
                     "WHERE st.MAHOCSINH = d.MAHOCSINH)";

        Cursor cursor = db.rawQuery(sql,null);
        ArrayList<Student> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        while(cursor.moveToNext()){

            Date date=new Date();
            try{
                date = simpleDateFormat.parse(cursor.getString(4));
            }catch (Exception ex){
                ex.printStackTrace();
                Log.d("bug:", "lỗi get date từ bảng student");
            }

            list.add(new Student(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3)>0, date, lop));
        }
        for(Student st: list){
            System.out.println(st);
        }
        cursor.close();
        return list;
    }

    public boolean registerSubject(String maHS, String maMH){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("MAHOCSINH", maHS);
        contentValues.put("MAMH", maMH);
        contentValues.putNull("DIEM");
        long result = database.insert("DIEM", null, contentValues);
        if(result==-1) return false;
        else return true;


    }

    public boolean deregisterSubject(String maHS, String maMH){
        boolean checkMark = getMark(maHS, maMH)==null?false:true;// nếu chưa nhập điểm thì false, nhập rồi thì true
        if(!checkMark) { // chưa nhập điểm
            SQLiteDatabase database = this.getWritableDatabase();
            boolean checkDelete =database.delete("DIEM", "MAHOCSINH=? AND MAMH=?", new String[]{maHS, maMH}) > 0;
            if(checkDelete) return true;
            else return false;
        }else return false; // nhập điểm rồi
    }

    public boolean updateMark(String maHS, String maMH, String diem){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(diem.equals(""))
        cv.putNull("DIEM");
        else cv.put("DIEM", diem);
        return database.update("DIEM",cv, "MAHOCSINH=? AND MAMH=?", new String[]{maHS,maMH})>0;
    }

    public String getMark(String maHS, String maMH){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT DIEM FROM DIEM WHERE MAHOCSINH='" + maHS + "' AND MAMH='" + maMH + "'", null);

        if(cursor.moveToNext()){
//            Toast.makeText(context, "get Mark:"+cursor.getString(0), Toast.LENGTH_SHORT).show();
            return cursor.getString(0);
        }else {
            return null;
        }
    }
    public ArrayList<Mark> getAllMark(){
        ArrayList<Mark> list = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT MAHOCSINH, DIEM.MAMH, TENMH, HESO, DIEM FROM DIEM, Subject WHERE DIEM.MAMH=Subject.MAMH";
        Cursor cursor = database.rawQuery(sql, null);
        while(cursor.moveToNext()){
            Subject subject = new Subject(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            list.add(new Mark(cursor.getString(0), cursor.getString(1), cursor.getString(4), subject));
        }

        return list;
    }

    public float calAverageScore(String maHS){
        ArrayList<Mark> markArrayList = this.getStudentMark(maHS);
        float sumScore = 0f;
        int sumFactor =0;
        if(markArrayList.size()>=2){// begin calculator
            for(Mark mark : markArrayList){
                int factor = mark.getSubject().getHeSo();
                if(mark.getDiem()==null){
                    sumScore +=0;
                }else{
                    sumScore +=(Integer.parseInt(mark.getDiem())*factor);
                }
                sumFactor+=factor;
            }
        }else return 0;

        return sumScore/sumFactor;
    }
    public ArrayList<Mark> getStudentMark(String maHS){
        ArrayList<Mark> list = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT MAHOCSINH, DIEM.MAMH, TENMH, HESO, DIEM FROM DIEM, Subject WHERE DIEM.MAMH=Subject.MAMH" +
                " AND MAHOCSINH='" + maHS+"'";
        Cursor cursor = database.rawQuery(sql, null);
        while(cursor.moveToNext()){
            Subject subject = new Subject(cursor.getString(1), cursor.getString(2), cursor.getInt(3));

            list.add(new Mark(cursor.getString(0), cursor.getString(1), cursor.getString(4), subject));
        }
        return list;
    }

    public String getSubjectName(String maMH){
        Cursor cursor = this.getData("SELECT TENMH FROM Subject WHERE MAMH='" + maMH + "'");
        if(cursor.moveToNext()){
            return cursor.getString(0);
        }else return null;
    }

    public Subject getSubjectByID(String maMH){
        Cursor cursor= this.getData("SELECT * FROM Subject WHERE MAMH='" + maMH + "'");
        if(cursor.moveToNext()){
            return new Subject(cursor.getString(0), cursor.getString(1), cursor.getInt(2));
        }else return null;

    }

}
