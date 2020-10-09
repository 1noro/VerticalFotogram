package net.a3do.app.verticalfotogram;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class GameUtils {

    public static String readJsonFile(Context context, int id) throws Exception {
        InputStream is = context.getResources().openRawResource(id);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        return writer.toString();
    }

    public static boolean checkTitle(JSONArray titleArray, String titleToCheck) {
        boolean out = false;

        try {
            String realTitle = titleArray.getJSONObject(0).getString("value").toLowerCase();
            titleToCheck = titleToCheck.toLowerCase();
            if (realTitle.equals(titleToCheck)) {
                out = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static String readLevelStatusFile(Context context, String fileDir) {

        String ret = "";

        while (true) {
            try {
                InputStream inputStream = context.openFileInput(fileDir);

                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((receiveString = bufferedReader.readLine()) != null) {
                        stringBuilder.append("\n").append(receiveString);
                    }

                    inputStream.close();
                    ret = stringBuilder.toString();
                } else {
                    ret = "[]";
                }
                break;
            } catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
                writeEmptyLevelStatusFile(context, fileDir);
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
                ret = "[]";
            }
        }

        return ret;
    }

    public static void writeEmptyLevelStatusFile(Context context, String fileDir) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileDir, Context.MODE_PRIVATE));
            outputStreamWriter.write("[]");
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
