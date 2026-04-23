package com.calculator.pro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.media.SoundPool;
import android.media.AudioManager;

import com.calculator.pro.button.ArithmeticButtons;
import com.calculator.pro.ui.MainUi;

public class MainActivity extends Activity {

    TextView display;
    ArithmeticButtons calc;
    MainUi ui;

    // SOUND SYSTEM
    private SoundPool soundPool;
    private int clickSound;
    private boolean soundEnabled = true;
    private boolean soundLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        display = (TextView) findViewById(R.id.display);

        calc = new ArithmeticButtons();
        ui = new MainUi(display);

        // ✅ OLD SOUNDPOOL (AIDE SAFE)
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        // load sound
        clickSound = soundPool.load(this, R.raw.click, 1);

        // ✅ NO LAMBDA (AIDE SAFE)
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool sp, int sampleId, int status) {
                if (status == 0) {
                    soundLoaded = true;
                }
            }
        });

        ui.update(calc.getDisplay());
    }

    public void onSoundToggle(View v) {
        soundEnabled = !soundEnabled;
    }

    private void playClick() {
        if (soundEnabled && soundLoaded) {
            soundPool.play(clickSound, 1, 1, 0, 0, 1);
        }
    }

    public void onNumberClick(View v) {
        playClick();
        Button b = (Button) v;
        ui.update(calc.inputNumber(b.getText().toString()));
    }

    public void onOperatorClick(View v) {
        playClick();
        Button b = (Button) v;
        String op = b.getText().toString();

        if (op.equals("÷")) op = "/";
        else if (op.equals("×")) op = "*";
        else if (op.equals("−")) op = "-";

        ui.update(calc.inputNumber(op));
    }

    public void onEqualClick(View v) {
        playClick();
        String result = calc.calculate();
        ui.update(result);
    }

    public void onClearClick(View v) {
        playClick();
        ui.update(calc.clear());
    }

    public void onBackspaceClick(View v) {
        playClick();
        ui.update(calc.backspace());
    }

    public void onOpenBracket(View v) {
        playClick();
        ui.update(calc.inputNumber("("));
    }

    public void onCloseBracket(View v) {
        playClick();
        ui.update(calc.inputNumber(")"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
