package net.a3do.app.verticalfotogram;

import android.view.View;

import org.json.JSONObject;

public class MyOnClickListener implements View.OnClickListener {
    final JSONObject parameters;

    public MyOnClickListener(JSONObject parameters) {
        this.parameters = parameters;
    }

    @Override
    public void onClick(View v){}
}
