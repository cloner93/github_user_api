package com.milad.githubmvvmtest.view.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.milad.githubmvvmtest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class jsonUtils {
    private Context context;

    public jsonUtils(Context context) {
        this.context = context;
    }

    private static Map<String, String> color = new HashMap<>();

    private String loadFromDisk(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.json);
        Writer writer = new StringWriter();

        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return writer.toString();

    }

    private void setColorLang(String jsonFile) {

        try {
            JSONObject object = new JSONObject(jsonFile);
            JSONArray keys = object.names();

            assert keys != null;
            for (int i = 0; i < keys.length(); ++i) {

                String key = keys.getString(i); // Here's your key
                String value = object.getString(key); // Here's your value
                color.put(key, value);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String findColor(String language) {

        for (Map.Entry<String, String> entry : color.entrySet()) {
            if (entry.getKey().equals(language)) {
                return entry.getValue();
            }
        }
        return "#db5855";
    }

    public String getColor(String language) {
        if (color.isEmpty()) {
            setColorLang(loadFromDisk(context));
            Log.v("data", "first load ");
            return findColor(language);
        } else {
            Log.v("data", "two load ");

            return findColor(language);
        }
    }
}
