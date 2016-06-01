package coolthings.joey.grant.bulldozerxtreme;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PlayActivity extends AppCompatActivity {
    private PlayVm playVm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playVm = new PlayVm();
        setContentView(R.layout.activity_play);
        playVm.onCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playVm.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        playVm.onPause(this);
    }
}
