package ro.brite.android.nehe09;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;


public class GlApp extends Activity {
	
	GLSurfaceView surface;
	GlRenderer renderer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        surface = new GLSurfaceView(this);
        renderer = new GlRenderer(this);
        surface.setRenderer(renderer);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_T:
			renderer.toggleTwinkle();
			break;
		case KeyEvent.KEYCODE_A:
			renderer.zoom -= 2;
			break;
		case KeyEvent.KEYCODE_Z:
			renderer.zoom += 2;
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			renderer.tilt -= 5;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			renderer.tilt += 5;
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}