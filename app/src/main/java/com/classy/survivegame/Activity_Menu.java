package com.classy.survivegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/* loaded from: classes2.dex */
public class Activity_Menu extends AppCompatActivity {
    private MaterialButton menu_BTN_start;
    private TextInputEditText menu_EDT_id;

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        initViews();
    }

    private void initViews() {
        this.menu_BTN_start.setOnClickListener(new View.OnClickListener() { // from class: com.classy.survivegame.Activity_Menu.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Activity_Menu.this.makeServerCall();
            }
        });
    }

    private void findViews() {
        this.menu_BTN_start = (MaterialButton) findViewById(R.id.menu_BTN_start);
        this.menu_EDT_id = (TextInputEditText) findViewById(R.id.menu_EDT_id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void makeServerCall() {
        Thread thread = new Thread() { // from class: com.classy.survivegame.Activity_Menu.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                String url = Activity_Menu.this.getString(R.string.url);
                String data = Activity_Menu.getJSON(url);
                Log.d("pttt", data);
                if (data != null) {
                    Activity_Menu activity_Menu = Activity_Menu.this;
                    activity_Menu.startGame(activity_Menu.menu_EDT_id.getText().toString(), data);
                }
            }
        };
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startGame(String id, String data) {
        String[] splits = data.split(",");
        String state = splits[Integer.valueOf(String.valueOf(id.charAt(7))).intValue()];
        Intent intent = new Intent(getBaseContext(), (Class<?>) Activity_Game.class);
        intent.putExtra(Activity_Game.EXTRA_ID, id);
        intent.putExtra(Activity_Game.EXTRA_STATE, state);
        startActivity(intent);
    }

    public static String getJSON(String url) {
        String data = "";
        HttpsURLConnection con = null;
        try {
            try {
                try {
                    URL u = new URL(url);
                    con = (HttpsURLConnection) u.openConnection();
                    con.connect();
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        sb.append(line + "\n");
                    }
                    br.close();
                    data = sb.toString();
                } catch (Throwable th) {
                    if (con != null) {
                        try {
                            con.disconnect();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (MalformedURLException ex2) {
                ex2.printStackTrace();
                if (con != null) {
                    con.disconnect();
                }
            } catch (IOException ex3) {
                ex3.printStackTrace();
                if (con != null) {
                    con.disconnect();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        } catch (Exception ex4) {
            ex4.printStackTrace();
        }
        return data;
    }
}
