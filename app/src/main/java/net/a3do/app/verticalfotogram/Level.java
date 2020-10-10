package net.a3do.app.verticalfotogram;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Level {

    private Context context;
    private int levelId;
    private int levelItemJsonId;
    private JSONArray levelArray;
    private JSONArray levelStatusArray;
    private int[] frameList;

    public Level(Context context, int levelId, int levelItemJsonId, String levelStatusJSON) {
        this.context = context;
        this.levelId = levelId;
        this.levelItemJsonId = levelItemJsonId;

        try {
            this.levelArray = new JSONArray(GameUtils.readJsonFile(this.context, levelItemJsonId));
            this.levelStatusArray = new JSONArray(levelStatusJSON);
        } catch (Exception e) {
            Log.d("##### EXCPETION","readJsonFile || new JSONArray(data)");
            e.printStackTrace();
        }

        assert this.levelArray != null;
        this.frameList = new int[this.levelArray.length()];
        for (int i = 0; i < this.frameList.length; i++) {
            try {
                this.frameList[i] = context.getResources().getIdentifier(this.levelArray.getJSONObject(i).getString("frame"), "drawable", context.getPackageName());
            } catch (JSONException e) {
                Log.d("##### EXCPETION", "ALGUNA COSA EN EL TRATAMIENTO DEL JSON, EN EL CONSTRUCTOR DE LEVEL");
                e.printStackTrace();
            }
        }
    }

    public int[] getFrameList() {
        return frameList;
    }

    public boolean checkTitle(ViewPager mViewPager, String titleToCheck) {
        boolean out = false;
        try {
            if (GameUtils.checkTitle(levelArray.getJSONObject(mViewPager.getCurrentItem()).getJSONArray("title"), titleToCheck)) {
//                Toast.makeText(context, "CORRECTO", Toast.LENGTH_SHORT).show();
                levelStatusArray.put(mViewPager.getCurrentItem());
                out = true;
            } /*else {
                Toast.makeText(context, "FALLO", Toast.LENGTH_SHORT).show();
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }

    public boolean checkFrameAnswered(int frameId) {
        boolean out = false;
        if (GameUtils.findIntInJSONArray(levelStatusArray, frameId)) out = true;
        return out;
    }

    public String getFrameTitleByLang(int frameId, String langId) {
        String out = "Null.";
        try {
            JSONArray frameTitles = levelArray.getJSONObject(frameId).getJSONArray("title");
            for (int i = 0; i < frameTitles.length(); i++) {
                JSONObject frameTitleObject = frameTitles.getJSONObject(0);
                if (frameTitleObject.getString("lang").equals(langId)) {
                    out = frameTitleObject.getString("value");
                }
            }
            if (out.equals("Null.")) out = frameTitles.getJSONObject(0).getString("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }

}
