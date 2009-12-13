package ro.brite.android.nehe07;

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
		case KeyEvent.KEYCODE_L:
			renderer.toggleLighting();
			break;
		case KeyEvent.KEYCODE_F:
			renderer.switchToNextFilter();
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			renderer.xSpeed = renderer.ySpeed = 0;
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			renderer.ySpeed -= 0.1f;
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			renderer.ySpeed += 0.1f;
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			renderer.xSpeed -= 0.1f;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			renderer.xSpeed += 0.1f;
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}