package com.classy.survivegame;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class Activity_Game extends AppCompatActivity {
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_STATE = "EXTRA_STATE";
    private ImageButton[] arrows;
    int currentLevel = 0;
    int[] steps = {1, 1, 1, 2, 2, 2, 3, 3, 3};
    private boolean goodToGo = true;

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        String id = getIntent().getStringExtra(EXTRA_ID);
        if (id.length() == this.steps.length) {
            int i = 0;
            while (true) {
                int[] iArr = this.steps;
                if (i >= iArr.length) {
                    break;
                }
                iArr[i] = Integer.valueOf(String.valueOf(id.charAt(i))).intValue() % 4;
                i++;
            }
        }
        findViews();
        initViews();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void arrowClicked(int direction) {
        if (this.goodToGo && direction != this.steps[this.currentLevel]) {
            this.goodToGo = false;
        }
        int i = this.currentLevel + 1;
        this.currentLevel = i;
        if (i >= this.steps.length) {
            finishGame();
        }
    }

    private void finishGame() {
        String state = getIntent().getStringExtra(EXTRA_STATE);
        if (this.goodToGo) {
            Toast.makeText(this, "Survived in " + state, 1).show();
        } else {
            Toast.makeText(this, "You Failed ", 1).show();
        }
        finish();
    }

    private void initViews() {
        int i = 0;
        while (true) {
            ImageButton[] imageButtonArr = this.arrows;
            if (i < imageButtonArr.length) {
                final int finalI = i;
                imageButtonArr[i].setOnClickListener(new View.OnClickListener() { // from class: com.classy.survivegame.Activity_Game.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        Activity_Game.this.arrowClicked(finalI);
                    }
                });
                i++;
            } else {
                return;
            }
        }
    }

    private void findViews() {
        this.arrows = new ImageButton[]{(ImageButton) findViewById(R.id.game_BTN_left), (ImageButton) findViewById(R.id.game_BTN_right), (ImageButton) findViewById(R.id.game_BTN_up), (ImageButton) findViewById(R.id.game_BTN_down)};
    }
}
