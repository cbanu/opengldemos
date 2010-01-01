package ro.brite.android.nehe09;

final class SceneState {
	
	final int nrStars = 30;
	Star[] stars;
	
	float zoom = -20.0f;
	float tilt = 90.0f;
	float spin;
	boolean twinkle = false;
	
	public SceneState() {
		// setup stars
		stars = new Star[nrStars];
		for (int i = 0; i < nrStars; i++) {
			stars[i] = new Star();
			stars[i].angle = 0;
			stars[i].dist = (((float)i)/nrStars)*5.0f;
			stars[i].r = (float)Math.random();
			stars[i].g = (float)Math.random();
			stars[i].b = (float)Math.random();
		}
	}
	
	public void toggleTwinkle() {
		twinkle = !twinkle;
	}

	public void updateNextFrame() {
		for (int i = 0; i < nrStars; i++) {
			spin += 0.01f;									// Used To Spin The Stars
			stars[i].angle += ((float)i)/nrStars;			// Changes The Angle Of A Star
			stars[i].dist -= 0.01f;							// Changes The Distance Of A Star

			if (stars[i].dist < 0.0f)						// Is The Star In The Middle Yet
			{
				stars[i].dist += 5.0f;						// Move The Star 5 Units From The Center
				stars[i].r = (float)Math.random();			// Give It A New Red Value
				stars[i].g = (float)Math.random();			// Give It A New Green Value
				stars[i].b = (float)Math.random();			// Give It A New Blue Value
			}
		}
	}
}
