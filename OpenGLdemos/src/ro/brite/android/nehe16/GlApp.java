package ro.brite.android.nehe16;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;


public class GlApp extends Activity {
	
	private GLSurfaceView surface;
	private GlRenderer renderer;
	
	private GestureDetector gestureDetector;
	
	private static boolean toasted;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        gestureDetector = new GestureDetector(this, new GlAppGestureListener());
        
        surface = new GLSurfaceView(this);
        renderer = new GlRenderer(this);
        surface.setRenderer(renderer);
        setContentView(surface);
        
        if (!toasted) {
        	Toast.makeText(this, "touch and drag to rotate object", Toast.LENGTH_LONG).show();
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
		case KeyEvent.KEYCODE_L:
			GlRenderer.sceneState.toggleLighting();
			break;
		case KeyEvent.KEYCODE_F:
			GlRenderer.sceneState.switchToNextFilter();
			break;
		case KeyEvent.KEYCODE_G:
			GlRenderer.sceneState.switchToNextFogMode();
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			GlRenderer.sceneState.saveRotation();
			GlRenderer.sceneState.dxSpeed = 0.0f;
			GlRenderer.sceneState.dySpeed = 0.0f;
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			GlRenderer.sceneState.saveRotation();
			GlRenderer.sceneState.dxSpeed -= 0.1f;
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			GlRenderer.sceneState.saveRotation();
			GlRenderer.sceneState.dxSpeed += 0.1f;
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			GlRenderer.sceneState.saveRotation();
			GlRenderer.sceneState.dySpeed -= 0.1f;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			GlRenderer.sceneState.saveRotation();
			GlRenderer.sceneState.dySpeed += 0.1f;
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private float startX, startY;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			return true;
		}
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			GlRenderer.sceneState.dxSpeed = 0.0f;
			GlRenderer.sceneState.dySpeed = 0.0f;
			GlRenderer.sceneState.saveRotation();
			startX = event.getX();
			startY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			GlRenderer.sceneState.dx = event.getX() - startX;
			GlRenderer.sceneState.dy = event.getY() - startY;
			break;
		}
		
		return super.onTouchEvent(event);
	}

	private class GlAppGestureListener extends GestureDetector.SimpleOnGestureListener
    {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			// measure speed in milliseconds
			GlRenderer.sceneState.dxSpeed = velocityX / 1000;
			GlRenderer.sceneState.dySpeed = velocityY / 1000;
			return super.onFling(e1, e2, velocityX, velocityY);
		}
    }
	
}