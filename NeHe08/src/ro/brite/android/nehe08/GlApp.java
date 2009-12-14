package ro.brite.android.nehe08;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;


public class GlApp extends Activity {
	
	private GLSurfaceView surface;
	private GlRenderer renderer;
	
	private GestureDetector gestureDetector;
	private static boolean fullscreen;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (fullscreen) {
        	requestWindowFeature(Window.FEATURE_NO_TITLE);  
        	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        	WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        
        gestureDetector = new GestureDetector(this, new GlAppGestureListener(this));
        
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
		case KeyEvent.KEYCODE_B:
			renderer.toggleBlending();
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	private class GlAppGestureListener extends GestureDetector.SimpleOnGestureListener
    {
    	private GlApp glApp;
    	
    	public GlAppGestureListener(GlApp glApp) {
    		this.glApp = glApp;
    	}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// toggle fullscreen flag
			GlApp.fullscreen = !GlApp.fullscreen;
			
			// start a new one
			Intent intent = new Intent(glApp, GlApp.class);
			startActivity(intent);

			// close current activity
			glApp.finish();
			
			return true;
		}
    }
	
}