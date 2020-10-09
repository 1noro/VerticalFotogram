package net.a3do.app.verticalfotogram;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;

public class Level {

    private Context context;
    private int levelId;
    private int levelItemJsonId;
    private JSONArray levelArray;

    public int[] getFrameList() {
        return frameList;
    }

    private int[] frameList;

    public Level(Context context, int levelId, int levelItemJsonId) {
        this.context = context;
        this.levelId = levelId;
        this.levelItemJsonId = levelItemJsonId;

        try {
            this.levelArray = new JSONArray(GameUtils.readJsonFile(this.context, levelItemJsonId));
        } catch (Exception e) {
            Log.d("##### EXCPETION","readJsonFile || new JSONArray(levelData)");
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

    public boolean checkTitle(ViewPager mViewPager, String titleToCheck) {
        boolean out = false;
        try {
            if (GameUtils.checkTitle(levelArray.getJSONObject(mViewPager.getCurrentItem()).getJSONArray("title"), titleToCheck)) {
                Toast.makeText(context, "CORRECTO", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "FALLO", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }

}
