package ro.brite.android.nehe07;

final class SceneState {
	
	float xRot = 0.0f;
	float yRot = 0.0f;
	
	float xSpeed = 0.0f;
	float ySpeed = 0.0f;
	
	boolean lighting = true;
	int filter = 2;
	
	public void toggleLighting() {
		lighting = !lighting;
	}

	public void switchToNextFilter() {
		filter = (filter + 1) % 3;
	}

}
