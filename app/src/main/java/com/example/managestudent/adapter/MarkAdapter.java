package com.example.managestudent.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.managestudent.R;
import com.example.managestudent.database.Database;
import com.example.managestudent.models.Mark;
import com.example.managestudent.models.Subject;

import java.util.List;

public class MarkAdapter extends ArrayAdapter<Mark> {

    private Context context;
    private int resource;
    private List<Mark> objects;
    private Database db;
    public MarkAdapter(@NonNull Context context, int resource, @NonNull List<Mark> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        db = new Database(this.context);
    }

    private class ViewHolder{
        TextView lbSubjectName, lbMark,lbHeSo, lbSubjectID;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);

            viewHolder.lbSubjectName = convertView.findViewById(R.id.lbSubjectName);
            viewHolder.lbMark = convertView.findViewById(R.id.lbMark);
            viewHolder.lbHeSo = convertView.findViewById(R.id.lbHeSoDiem);
            viewHolder.lbSubjectID = convertView.findViewById(R.id.lbSubjectID);

            convertView.setTag(viewHolder);
        }else{
           viewHolder = (ViewHolder) convertView.getTag();
        }
        Mark mark = objects.get(position);
        viewHolder.lbSubjectID.setText(mark.getMaMH());
        viewHolder.lbHeSo.setText(mark.getSubject().getHeSo()+"");
        viewHolder.lbSubjectName.setText(db.getSubjectName(mark.getMaMH()));
        viewHolder.lbMark.setText(mark.getDiem()==null?"": mark.getDiem()+"");

        return convertView;
    }
}
