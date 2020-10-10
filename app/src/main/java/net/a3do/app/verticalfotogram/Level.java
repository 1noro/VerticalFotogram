package net.a3do.app.verticalfotogram;

import android.content.Context;
import android.util.Log;

import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Level {

    private Context context;
    private JSONArray levelArray;
    private JSONArray levelStatusArray;
    private int[] frameList;
    private String fileStatusDir;
    private String[] lastFailedAnswersArray;

    public Level(Context context, int levelId, int levelItemJsonId) {
        this.context = context;
        this.fileStatusDir = "levelStatus" + levelId + ".json";

        try {
            this.levelArray = new JSONArray(GameUtils.readJsonFile(this.context, levelItemJsonId));
            this.levelStatusArray = new JSONArray(GameUtils.readLevelStatusFile(context, this.fileStatusDir));
        } catch (Exception e) {
            Log.d("##### EXCPETION","readJsonFile || new JSONArray(data)");
            e.printStackTrace();
        }

        assert this.levelArray != null;
        this.frameList = new int[this.levelArray.length()];
        this.lastFailedAnswersArray = new String[this.levelArray.length()];
        for (int i = 0; i < this.frameList.length; i++) {
            this.lastFailedAnswersArray[i] = "";
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

    public boolean checkTitle(@NotNull ViewPager mViewPager, String titleToCheck) {
        boolean out = false;
        try {
            if (GameUtils.checkTitle(levelArray.getJSONObject(mViewPager.getCurrentItem()).getJSONArray("title"), titleToCheck)) {
                levelStatusArray.put(mViewPager.getCurrentItem());
                GameUtils.writeToFile(context, this.fileStatusDir, levelStatusArray.toString());
                out = true;
            }
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
                    break;
                }
            }
            if (out.equals("Null.")) out = frameTitles.getJSONObject(0).getString("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }

    public void setLastFailedAnswer(int frameId, String failedAnswer) {
        this.lastFailedAnswersArray[frameId] = failedAnswer;
    }

    public String getLastFailedAnswer(int frameId) {
        return this.lastFailedAnswersArray[frameId];
    }

}
