package ro.brite.android.nehe08;

final class SceneState {
	
	float xRot = 0.0f;
	float yRot = 0.0f;
	
	float xSpeed = 0.0f;
	float ySpeed = 0.0f;
	
	boolean lighting = false;
	int filter = 2;
	boolean blending = true;
	
	public void toggleLighting() {
		lighting = !lighting;
	}

	public void switchToNextFilter() {
		filter = (filter + 1) % 3;
	}

	public void toggleBlending() {
		blending = !blending;
	}

}
