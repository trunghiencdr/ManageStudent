package com.example.managestudent.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.managestudent.R;
import com.example.managestudent.activities.RegisterSubject;
import com.example.managestudent.activities.StudentMark;
import com.example.managestudent.database.Database;
import com.example.managestudent.models.Mark;
import com.example.managestudent.models.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

public class StudentAdapter extends ArrayAdapter<Student> {

    Context context;
    int resource;
    List<Student> objects;
    private String loai = null;
    Database database;
    String maMH;

    public StudentAdapter(@NonNull Context context, int resource, @NonNull List<Student> objects, String loai) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.loai = loai;
    }

    public StudentAdapter(@NonNull Context context, int resource, @NonNull List<Student> objects, String loai, String maMH) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.loai = loai;
        this.maMH = maMH;
        database = new Database(context);
    }

    private class ViewHolder {
        TextView lbMaHocSinh, lbHoTen, lbPhai, lbDiem, lbNgaySinh, lbSTT;
        ImageView btnDeleteStudent, btnRegister;
        EditText txtDiem;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);

            viewHolder.lbMaHocSinh = convertView.findViewById(R.id.lbMaHocSinh);
            viewHolder.lbHoTen = convertView.findViewById(R.id.lbHoTen);
            viewHolder.lbPhai = convertView.findViewById(R.id.lbPhai);
            viewHolder.btnDeleteStudent = convertView.findViewById(R.id.btnDeleteStudent);
            viewHolder.btnRegister = convertView.findViewById(R.id.btnRegister);
            viewHolder.txtDiem = convertView.findViewById(R.id.txtDiem);
            viewHolder.txtDiem.setSelection(viewHolder.txtDiem.getText().length());
            viewHolder.lbDiem = convertView.findViewById(R.id.lbDiem);
            viewHolder.lbNgaySinh = convertView.findViewById(R.id.lbNgaySinh);
            viewHolder.lbSTT = convertView.findViewById(R.id.lbSTTStudent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Student student = objects.get(position);
        viewHolder.lbSTT.setText((position + 1) + "");
        viewHolder.lbMaHocSinh.setText(student.getMaHocSinh());
        viewHolder.lbHoTen.setText("Họ tên: " + student.getHoTen());
        viewHolder.lbPhai.setText("Phái: " + (student.isPhai() ? "Nam" : "Nữ"));
        viewHolder.lbNgaySinh.setText("Ngày sinh: " + new SimpleDateFormat("dd/MM/yyyy").format(student.getNgaySinh()));
        if (context.getClass() == RegisterSubject.class) {

            viewHolder.btnRegister.setVisibility(View.VISIBLE);
            viewHolder.btnDeleteStudent.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.txtDiem.setVisibility(View.INVISIBLE);
            viewHolder.lbDiem.setVisibility(View.INVISIBLE);
            viewHolder.btnDeleteStudent.setVisibility(View.VISIBLE);
            viewHolder.btnRegister.setVisibility(View.INVISIBLE);
        }
        // case register and deregister
        if (!loai.equalsIgnoreCase("normal")) {
            /// case register student
            if (loai.equalsIgnoreCase("register")) {

                viewHolder.txtDiem.setVisibility(View.VISIBLE);
                viewHolder.lbDiem.setVisibility(View.VISIBLE);
                viewHolder.txtDiem.setText(database.getMark(student.getMaHocSinh(), maMH));
                viewHolder.txtDiem.setSelection(viewHolder.txtDiem.getText().length());
                viewHolder.txtDiem.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        Toast.makeText(context, "ontextchange:"+charSequence, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {


                        String mark = editable.toString();

                        boolean checkUpdate;
                        if (mark.equals("")) {// nếu nhập điểm rỗng thì update điểm null của học sinh vào bảng
                            checkUpdate = database.updateMark(student.getMaHocSinh(), maMH, mark);
                        } else {// nếu khác rỗng thì check xem phải là double không
                            try {
                                double markDouble = Double.parseDouble(mark);
                                // nếu nhập đúng định dạng thì update
                                checkUpdate = database.updateMark(student.getMaHocSinh(), maMH, mark);

                            } catch (Exception ex) {
                                return;
                            }
                        }
                        if (checkUpdate) {

                        }


                    }
                });
                // button deregister
                viewHolder.btnRegister.setImageDrawable(this.context.getResources().getDrawable(R.drawable.leftarrow));
                viewHolder.btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean success = database.deregisterSubject(student.getMaHocSinh(), maMH);
                        if (success) {
                            objects.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Deregister successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Deregister failed because points entered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // case deregister student
            } else if (loai.equalsIgnoreCase("noregister")) {
                viewHolder.txtDiem.setVisibility(View.INVISIBLE);
                viewHolder.lbDiem.setVisibility(View.INVISIBLE);
                viewHolder.btnRegister.setImageDrawable(this.context.getResources().getDrawable(R.drawable.rightarrow));

                // register subject if student has registered yet
                viewHolder.btnRegister.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        boolean success = database.registerSubject(student.getMaHocSinh(), maMH);
                        if (success) {
                            objects.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Register success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Register failed because database.registerSubject", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else { // case list student of class
            viewHolder.btnRegister.setVisibility(View.VISIBLE);
            viewHolder.btnRegister.setImageResource(R.drawable.exam);
            viewHolder.btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StudentMark.class);
                    intent.putExtra("student", student);
                    getContext().startActivity(intent);
                }
            });
        }

        // delete student
        viewHolder.btnDeleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database database = new Database(context);
                boolean result = database.deleteStudent(student.getMaHocSinh());
                if (result) {
                    Toast.makeText(context,
                            "Delete student successfully", Toast.LENGTH_SHORT).show();
                    objects.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Student have already registered for some courses ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }
}
