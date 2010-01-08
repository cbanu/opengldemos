package ro.brite.android.nehe23;

import javax.microedition.khronos.opengles.GL10;

import ro.brite.android.opengl.common.GlMatrix;

final class SceneState {
	
	static final float angleFactor = 0.35f;
	float dx, dy;
	float dxSpeed, dySpeed;
	GlMatrix baseMatrix = new GlMatrix();
	GlMatrix baseMatrixInv = new GlMatrix();
	
	boolean lighting = false;
	int filter = 2;
	int objectIdx = 1;

	// snapshot values
	private float _dx, _dy;
	
	public void toggleLighting() {
		lighting = !lighting;
	}

	public void switchToNextFilter() {
		filter = (filter + 1) % 3;
	}

	public void switchToNextObject() {
		objectIdx = (objectIdx + 1) % 6;
	}

	void takeDataSnapshot() {
		_dx = dx;
		_dy = dy;
	}
	
	void saveRotation() {
		float r = (float)Math.sqrt(_dx * _dx + _dy * _dy);
		if (r != 0) {
			GlMatrix rotation = new GlMatrix();
			rotation.rotate(r * angleFactor, _dy / r, _dx / r, 0);
			baseMatrix.premultiply(rotation);

			GlMatrix rotationInv = new GlMatrix();
			rotationInv.rotate(- r * angleFactor, _dy / r, _dx / r, 0);
			baseMatrixInv.multiply(rotationInv);
		}
		GlRenderer.sceneState.dx = 0.0f;
		GlRenderer.sceneState.dy = 0.0f;
	}
	
	void rotateModel(GL10 gl) {
		float r = (float)Math.sqrt(_dx * _dx + _dy * _dy);
		if (r != 0) {
			gl.glRotatef(r * angleFactor, _dy / r, _dx / r, 0);
		}
		gl.glMultMatrixf(baseMatrix.data);
	}

	GlMatrix getRotation() {
		GlMatrix matrix = new GlMatrix();
		float r = (float)Math.sqrt(_dx * _dx + _dy * _dy);
		if (r != 0) {
			matrix.rotate(r * angleFactor, _dy / r, _dx / r, 0);
		}
		matrix.multiply(baseMatrix);
		return matrix;
	}
	
	GlMatrix getInverseRotation() {
		GlMatrix matrix = new GlMatrix();
		matrix.assign(baseMatrixInv);
		float r = (float)Math.sqrt(_dx * _dx + _dy * _dy);
		if (r != 0) {
			matrix.rotate(- r * angleFactor, _dy / r, _dx / r, 0);
		}
		return matrix;
	}

	void dampenSpeed(long deltaMillis) {
		if (dxSpeed != 0.0f) {
			dxSpeed *= (1.0f - 0.001f * deltaMillis);
			if (Math.abs(dxSpeed) < 0.001f) dxSpeed = 0.0f;
		}
		
		if (dySpeed != 0.0f) {
			dySpeed *= (1.0f - 0.001f * deltaMillis);
			if (Math.abs(dySpeed) < 0.001f) dySpeed = 0.0f;
		}
	}
	
}
