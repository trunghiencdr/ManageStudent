package com.example.managestudent.models;

import android.content.Context;
import android.util.Log;

import com.example.managestudent.R;

import java.io.File;

public class Common {
    public static String getAppPath(Context context) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory()
                + File.separator
                + context.getResources().getString(R.string.app_name)
                + File.separator);
        Log.d("bug: getAppPath:", dir.getPath());

        if(!dir.exists()){
            dir.mkdirs();
        }

        return dir.getPath() + File.separator;
    }
}
