package ro.brite.android.nehe09;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;


public class GlApp extends Activity {
	
	private GLSurfaceView surface;
	private GlRenderer renderer;

	private static boolean toasted;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        surface = new GLSurfaceView(this);
        renderer = new GlRenderer(this);
        surface.setRenderer(renderer);
        setContentView(surface);
        
        if (!toasted) {
        	Toast.makeText(this, "use trackball to tilt spiral", Toast.LENGTH_LONG).show();
        	toasted = true;
        }
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
			GlRenderer.sceneState.toggleTwinkle();
			break;
		case KeyEvent.KEYCODE_A:
			GlRenderer.sceneState.zoom -= 2;
			break;
		case KeyEvent.KEYCODE_Z:
			GlRenderer.sceneState.zoom += 2;
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			GlRenderer.sceneState.tilt -= 5;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			GlRenderer.sceneState.tilt += 5;
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}