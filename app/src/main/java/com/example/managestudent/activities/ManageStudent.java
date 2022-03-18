package com.example.managestudent.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managestudent.R;
import com.example.managestudent.adapter.StudentAdapter;
import com.example.managestudent.database.Database;
import com.example.managestudent.models.Classroom;
import com.example.managestudent.models.Common;
import com.example.managestudent.models.Mark;
import com.example.managestudent.models.PdfDocumentAdapter;
import com.example.managestudent.models.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ManageStudent extends AppCompatActivity {

    Spinner spinnerClass;
    ArrayList<Classroom> listClassroom;
    ArrayAdapter<Classroom> adapterClassroom;
    String lop;
    ListView lvStudent;
    ArrayList<Student> listStudent;
    StudentAdapter studentAdapter;
    Database database;
    Calendar calendar;

    // dialog add
    Dialog dialog;
    TextView titleDialogAddStudent;
    EditText txtMaHocSinh;
    EditText txtHo;
    EditText txtTen;
    RadioButton rdbNam;
    RadioButton rdbNu;
    EditText txtNgaySinh;
    ImageView btnChooseDate;
    Button btnConfirmAddStudent;
    // export pdf

    ImageView btnExportPDFStudent, btnBackManageStudent;


    FloatingActionButton btnAddStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_student);


        addControls();
        initData();
        addEvents();

    }

    private void addControls() {
        spinnerClass = findViewById(R.id.spinnerClass);
        lvStudent = findViewById(R.id.lvStudent);
        btnAddStudent = findViewById(R.id.btnAddStudent);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_student);
        titleDialogAddStudent = dialog.findViewById(R.id.titleDialogAddStudent);
        txtMaHocSinh = dialog.findViewById(R.id.txtMaHocSinh);
        txtHo = dialog.findViewById(R.id.txtHo);
        txtTen = dialog.findViewById(R.id.txtTen);
        rdbNam = dialog.findViewById(R.id.rdbNam);
        rdbNu = dialog.findViewById(R.id.rdbNu);
        txtNgaySinh = dialog.findViewById(R.id.txtNgaySinh);
        btnChooseDate = dialog.findViewById(R.id.btnChooseDate);
        btnConfirmAddStudent = dialog.findViewById(R.id.btnConfirmAddStudent);

        // export pdf
        btnExportPDFStudent = findViewById(R.id.btnExportPDFStudent);
        btnBackManageStudent = findViewById(R.id.btnBackManageStudent);


    }

    private void addEvents() {

        btnBackManageStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManageStudent.this, "back", Toast.LENGTH_SHORT).show();
            }
        });
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        btnExportPDFStudent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(ManageStudent.this, "btnPDF click", Toast.LENGTH_SHORT).show();
                                createPDFFile(Common.getAppPath(ManageStudent.this) + "test_pdf.pdf");
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();


        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lop = listClassroom.get(i).getLOP();
                try {
                    listStudent = database.getAllStudents(lop);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                studentAdapter = new StudentAdapter(ManageStudent.this, R.layout.custom_listview_student, listStudent, "normal");
                lvStudent.setAdapter(studentAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//                if(listClassroom.size()>0){
//                    lop=listClassroom.get(0).getLOP();
//                    listStudent = database.getAllStudents(lop);
//                    studentAdapter = new StudentAdapter(ManageStudent.this, R.layout.custom_listview_student, listStudent);
//                    lvStudent.setAdapter(studentAdapter);
//                }else listStudent = new ArrayList<Student>();

            }
        });
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog("ADD", -1);
            }
        });

        lvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog("UPDATE", i);
            }
        });
    }

    private void createPDFFile(String path) {
        if (new File(path).exists()) {
            new File(path).delete();
        }

        try {
            Document document = new Document();
            // save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            document.setPageSize(PageSize.A4);
            document.setMargins(50f, 5f, 5f, 10f);
            document.addCreationDate();
            document.addAuthor("TNT_HIEN");
            document.addCreator("Hien Nguyen");

            // font setting
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 18);
            BaseColor colorAccent = new BaseColor(0, 153, 204, 255);
            float fontSize = 20.0f;
            float valueFontSize = 26.0f;

            // Custom font
            BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            Paragraph paragraph = new Paragraph("");
            //create title of document
            Font titleFont = new Font(fontName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "MANAGE STUDENT", Element.ALIGN_CENTER, titleFont);
            //specify column widths
            Classroom classroom = (Classroom) spinnerClass.getSelectedItem();
            spendingDataToPDF(classroom.getLOP());


            addNewItem(document, "CLASS:"  + classroom.getLOP() + "           "+ "TEACHER:"   +
                    classroom.getCHUNHIEM(), Element.ALIGN_CENTER, bf12);
            addNewItem(document, "                              ", Element.ALIGN_CENTER, bf12);


            //specify column widths

            for(Student student: listStudent){
                addNewItem(document, "STUDENT INFORMATION", Element.ALIGN_LEFT, bfBold12);
                addNewItem(document, String.format("%-30s %-30s", "ID:"+student.getMaHocSinh(),
                       "GENDER:"+ (student.isPhai()?"Nam":"Nu")), Element.ALIGN_LEFT, bf12);

                addNewItem(document, String.format("%-30s %-30s","NAME:"+ student.getHoTen(),
                       "BIRTHDAY:"+ new SimpleDateFormat("dd/MM/yyyy").format(student.getNgaySinh())),
                        Element.ALIGN_LEFT, bf12);

                createTable(document,bfBold12, bf12, student);
            }


//            //repeat the same as above to display another location
//            insertCell(table, "", Element.ALIGN_LEFT, 4, bfBold12);
//            insertCell(table, "California Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
//            orderTotal = 0;
//
//            for (int x = 1; x < 7; x++) {
//
//                insertCell(table, "20020" + x, Element.ALIGN_RIGHT, 1, bf12);
//                insertCell(table, "XYZ00" + x, Element.ALIGN_LEFT, 1, bf12);
//                insertCell(table, "This is Customer Number XYZ00" + x, Element.ALIGN_LEFT, 1, bf12);
//
//                orderTotal = Double.valueOf(df.format(Math.random() * 1000));
//                total = total + orderTotal;
//                insertCell(table, df.format(orderTotal), Element.ALIGN_RIGHT, 1, bf12);
//
//            }
//            insertCell(table, "California Total...", Element.ALIGN_RIGHT, 3, bfBold12);
//            insertCell(table, df.format(total), Element.ALIGN_RIGHT, 1, bfBold12);

            //add the PDF table to the paragraph

            // add the paragraph to the document
            document.add(paragraph);


            // add more


            //print
            document.close();
            printPDF();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void spendingDataToPDF(String lop) {

    }

    private void createTable(Document document, Font header, Font data, Student student){
        Paragraph paragraph = new Paragraph();
        ArrayList<Mark> markArrayList = database.getStudentMark(student.getMaHocSinh());
        DecimalFormat df = new DecimalFormat("0.00");
        float[] columnWidths = {1f, 3f, 2f, 2f};
        //create PDF table with the given widths
        PdfPTable table = new PdfPTable(columnWidths);
        // set table width a percentage of the page width
        table.setWidthPercentage(100f);

        //insert column headings
        insertCell(table, "STT", Element.ALIGN_RIGHT, 1, header);
        insertCell(table, "Subject name", Element.ALIGN_LEFT, 1, header);
        insertCell(table, "Factor", Element.ALIGN_LEFT, 1, header);
        insertCell(table, "Score", Element.ALIGN_LEFT, 1, header);
//            insertCell(table, "Order Total", Element.ALIGN_RIGHT, 1, bfBold12);
        table.setHeaderRows(1);

        //insert an empty row
//            insertCell(table, "", Element.ALIGN_LEFT, 4, bfBold12);
        //create section heading by cell merging
//            insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
        double orderTotal, total = 0;

        //just some random data to fill
        for (int i=0;i<markArrayList.size(); i++) {
            Mark mark = markArrayList.get(i);
            insertCell(table, "" + (i+1), Element.ALIGN_RIGHT, 1, data);
            insertCell(table, mark.getSubject().getTenMH(), Element.ALIGN_RIGHT, 1, data);
            insertCell(table, mark.getSubject().getHeSo()+"" , Element.ALIGN_LEFT, 1, data);
            insertCell(table, mark.getDiem()==null?"":mark.getDiem() , Element.ALIGN_LEFT, 1, data);

//                orderTotal = Double.valueOf(df.format(Math.random() * 1000));
//                total = total + orderTotal;
//                insertCell(table, df.format(orderTotal), Element.ALIGN_RIGHT, 1, bf12);

        }
        //merge the cells to create a footer for that section
        insertCell(table, "Average subject:", Element.ALIGN_RIGHT, 3, header);
        insertCell(table, df.format(total), Element.ALIGN_RIGHT, 1, header);

        paragraph.add(table);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(ManageStudent.this,
                    Common.getAppPath(ManageStudent.this) + "test_pdf.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        } catch (Exception e) {
            Log.d("TNT_HIEN:", "" + e.getMessage());
        }
    }

    private void addNewItem(Document document, String text, int align, Font font) {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

    private void processChooseDate() {
        DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                txtNgaySinh.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                dialog.getContext(),
                callBack,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private boolean checkStudentInput() {

        if (txtMaHocSinh.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(dialog.getContext(), "Student ID cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtHo.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(dialog.getContext(), "Last name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtTen.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(dialog.getContext(), "First name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void dialog(String loai, int index) {


        // add class
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");


        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processChooseDate();

            }
        });


        if (loai.equals("ADD")) {
            calendar = Calendar.getInstance();
            rdbNam.setChecked(true);
            txtNgaySinh.setText(simpleDateFormat.format(calendar.getTime()));
            titleDialogAddStudent.setText("ADD NEW STUDENT");
            btnConfirmAddStudent.setText("ADD");
            btnConfirmAddStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Student newStudent = null;
                    if (checkStudentInput()) {// check if condition input
                        try {
                            newStudent = new Student(txtMaHocSinh.getText().toString(), txtHo.getText().toString(), txtTen.getText().toString(),
                                    rdbNam.isChecked(), simpleDateFormat.parse(txtNgaySinh.getText().toString()), lop);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        boolean result = database.addStudent(newStudent);

                        if (result) {

                            listStudent.add(newStudent);
                            studentAdapter.notifyDataSetChanged();
                            txtMaHocSinh.setText("");
                            txtHo.setText("");
                            txtTen.setText("");
                            calendar = Calendar.getInstance();
                            txtNgaySinh.setText(simpleDateFormat.format(calendar.getTime()));
                            Toast.makeText(ManageStudent.this, "Add student successfully!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(ManageStudent.this, "Failed!Duplicate student code", Toast.LENGTH_SHORT).show();
                    } else return;
                }
            });


        } else { // CASE UPDATE STUDENT
            Student student = listStudent.get(index);
            txtMaHocSinh.setText(student.getMaHocSinh());
            txtHo.setText(student.getHo());
            txtTen.setText(student.getTen());
            calendar.setTime(student.getNgaySinh());
            txtNgaySinh.setText(simpleDateFormat.format(calendar.getTime()));
            if (student.isPhai()) {
                rdbNam.setChecked(true);
            } else {
                rdbNu.setChecked(true);
            }


            titleDialogAddStudent.setText("UPDATE CLASSROOM");
            btnConfirmAddStudent.setText("UPDATE");
            btnConfirmAddStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Student newStudent = null;
                    try {
                        newStudent = new Student(txtMaHocSinh.getText().toString(), txtHo.getText().toString(), txtTen.getText().toString(),
                                rdbNam.isChecked(), simpleDateFormat.parse(txtNgaySinh.getText().toString()), lop);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    boolean result = database.updateStudent(listStudent.get(index).getMaHocSinh(), newStudent);
                    if (result) {

                        listStudent.remove(index);
                        listStudent.add(index, newStudent);
                        studentAdapter.notifyDataSetChanged();
                        Toast.makeText(ManageStudent.this, "Succesful!", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(ManageStudent.this, "Failed!", Toast.LENGTH_SHORT).show();


                }
            });
        }
        dialog.show();
    }

    private void initData() {
        database = new Database(ManageStudent.this);
        listClassroom = database.getAllClassrooms();
        adapterClassroom = new ArrayAdapter<>(ManageStudent.this, android.R.layout.simple_spinner_dropdown_item, listClassroom);
        spinnerClass.setAdapter(adapterClassroom);


    }
}