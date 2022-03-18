package com.example.managestudent.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.managestudent.R;
import com.example.managestudent.database.Database;
import com.example.managestudent.models.Subject;

import java.util.List;

public class SubjectAdapter extends ArrayAdapter<Subject> {

    Context context;
    int resource;
    List<Subject> objects;
    public SubjectAdapter(@NonNull Context context, int resource, @NonNull List<Subject> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    private class ViewHolder{
        TextView txtMaMH, txtTenMH, txtHeSo, lbSTT;
        ImageView btnDelete;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);

            viewHolder = new ViewHolder();
            viewHolder.txtMaMH =convertView.findViewById(R.id.lbMaMH);
            viewHolder.txtTenMH =convertView.findViewById(R.id.lbTenMH);
            viewHolder.txtHeSo =convertView.findViewById(R.id.lbHeSo);
            viewHolder.btnDelete =convertView.findViewById(R.id.btnDelete);
            viewHolder.lbSTT = convertView.findViewById(R.id.lbSTTSubject);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Subject subject = objects.get(position);
        viewHolder.txtMaMH.setText( subject.getMaMH());
        viewHolder.txtTenMH.setText( subject.getTenMH());
        viewHolder.txtHeSo.setText( subject.getHeSo()+"");
        viewHolder.lbSTT.setText((position+1)+"");
        // delete subject
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database database = new Database(context);

                boolean result = database.deleteSubject(subject.getMaMH());
                if(result){
                    objects.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Delete subject successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "The course has already been registered by some students", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}
