package ro.brite.android.nehe02;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;


public class GlApp extends Activity {

    private GLSurfaceView surface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        surface = new GLSurfaceView(this);
        surface.setRenderer(new GlRenderer());
        setContentView(surface);
    }

    @Override
    protected void onPause() {
        super.onPause();
        surface.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surface.onResume();
    }

}