package com.classy.survivegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
    private void makeServerCall() {
        Thread thread = new Thread(() -> {
            String url = Activity_Menu.this.getString(R.string.url);
            String data = Activity_Menu.getJSON(url);
            if (data != null) {
                data = data.replace("\n", "").trim();
            }

            Log.d("pttt", data);
            if (data != null) {
                // לקרוא ל־startGame על ה־main thread
                String finalData = data;
                runOnUiThread(() -> {
                    String id = menu_EDT_id.getText().toString();
                    if (id.length() != 9) {
                        Toast.makeText(Activity_Menu.this, "ID must be 9 numbers", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startGame(id, finalData);
                });
            }
        });
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startGame(String id, String data) {
        String[] splits = data.split(",");
        int index = Character.getNumericValue(id.charAt(7));
        if (index < 0 || index >= splits.length) {
            Toast.makeText(this, "No country found", Toast.LENGTH_SHORT).show();
            return;
        }
        String state = splits[index];
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
