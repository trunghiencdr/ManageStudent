package com.example.managestudent.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.managestudent.R;
import com.example.managestudent.activities.ManageClass;
import com.example.managestudent.database.Database;
import com.example.managestudent.models.Classroom;

import java.util.List;


public class ClassroomAdapter extends ArrayAdapter<Classroom> {

    ManageClass context;
    int resource;
    List<Classroom> objects;

    public ClassroomAdapter(@NonNull ManageClass context, int resource, @NonNull List<Classroom> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    private class ViewHolder{
        TextView lbLop, lbChuNhiem, lbSTT;
        ImageView btnDeleteClass;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);

            viewHolder = new ViewHolder();
            viewHolder.lbLop = convertView.findViewById(R.id.lbLop);
            viewHolder.lbChuNhiem = convertView.findViewById(R.id.lbChuNhiem);
            viewHolder.btnDeleteClass = convertView.findViewById(R.id.btnDeleteClass);
            viewHolder.lbSTT = convertView.findViewById(R.id.lbSTTClass);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Classroom classroom = objects.get(position);
        viewHolder.lbSTT.setText((position+1)+ "");
        viewHolder.lbLop.setText(classroom.getLOP());
        viewHolder.lbChuNhiem.setText(classroom.getCHUNHIEM());
        viewHolder.btnDeleteClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Database db = new Database(context);
                boolean result = db.deleteClassroom(classroom.getLOP());
                if(result){
                    Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
                    objects.remove(position);
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(context, "Classroom have more student", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return convertView;
    }
}
