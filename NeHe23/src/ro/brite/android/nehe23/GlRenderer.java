package ro.brite.android.nehe23;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ro.brite.android.nehe23.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;


public class GlRenderer implements Renderer {

	private Context context;
	
	public GlRenderer(Context context) {
		this.context = context;
	}
	
	private final static float lightAmb[]= { 0.5f, 0.5f, 0.5f, 1.0f };
	private final static float lightDif[]= { 1.0f, 1.0f, 1.0f, 1.0f };
	private final static float lightPos[]= { 0.0f, 0.0f, 2.0f, 1.0f };
	
	private final static FloatBuffer lightAmbBfr;
	private final static FloatBuffer lightDifBfr;
	private final static FloatBuffer lightPosBfr;
	
	private IntBuffer texturesBuffer;
	
	private static float xRot;
	private static float yRot;
	static float xSpeed;
	static float ySpeed;
	
	private static boolean lighting = false;
	private static int filter = 0;

	private static int object;
	private static GlCube cube;
	private static GlCylinder cylinder;
	private static GlDisk disk;
	private static GlSphere sphere;
	private static GlCylinder cone;
	private static GlDisk partialDisk;
	
	private static GlPlane background;
	
	static {
		lightAmbBfr = FloatBuffer.wrap(lightAmb);
		lightDifBfr = FloatBuffer.wrap(lightDif);
		lightPosBfr = FloatBuffer.wrap(lightPos);

		cube = new GlCube(1.0f, true, true);
		cylinder = new GlCylinder(1.0f, 1.0f, 3.0f, 32, 4, true, true);
		disk = new GlDisk(0.5f, 1.5f, 32, 4, true, true);
		sphere = new GlSphere(1.3f, 24, 12, true, true);
		cone = new GlCylinder(1.0f, 0.0f, 3.0f, 32, 4, true, true);
		partialDisk = new GlDisk(0.5f, 1.5f, 32, 4, (float) (Math.PI / 4), (float) (7 * Math.PI / 4), true, true);
		
		background = new GlPlane(16, 12, true, true);
	}

	private void LoadTextures(GL10 gl) {
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		int[] textureIDs = new int[] { R.drawable.bg, R.drawable.reflect };
		
		// create textures
		texturesBuffer = IntBuffer.allocate(3 * textureIDs.length);
		gl.glGenTextures(3, texturesBuffer);
		
		for (int i = 0; i < textureIDs.length; i++) {
			// load bitmap
			Bitmap texture = Utils.getTextureFromBitmapResource(context, textureIDs[i]);
			
			// setup texture 0
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesBuffer.get(3 * i));
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, texture, 0);
			
			// setup texture 1
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesBuffer.get(3 * i + 1));
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, texture, 0);
			
			// setup texture 2
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesBuffer.get(3 * i + 2));
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
			Utils.generateMipmapsForBoundTexture(texture);
	
			// free bitmap
			texture.recycle();
		}
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(0, 0, 0, 0);

		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		gl.glCullFace(GL10.GL_BACK);
		
		// lighting
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbBfr);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDifBfr);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosBfr);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		// update lighting
		if (lighting) {
			gl.glEnable(GL10.GL_LIGHTING);
		} else {
			gl.glDisable(GL10.GL_LIGHTING);
		}
		
		// draw background
		gl.glPushMatrix();
		gl.glTranslatef(0, 0, -10);
		background.Draw(gl);
		gl.glPopMatrix();
		
		// position object
		gl.glTranslatef(0, 0, -6);
		gl.glRotatef(xRot, 1, 0, 0);
		gl.glRotatef(yRot, 0, 1, 0);

		// compute reflection variables
		
		GlVertex vEye = new GlVertex(0, 0, 1);	// eye-vector in world space (eye is in the scene at Znear = 1.0f)
		GlMatrix mInv = new GlMatrix();			// build the current inverse matrix
		mInv.rotate(-yRot, 0, 1, 0);
		mInv.rotate(-xRot, 1, 0, 0);
		mInv.translate(0, 0, 6);
		mInv.transform(vEye);					// transform the eye-vector in model space

		GlMatrix mInvRot = new GlMatrix();		// rotation matrix, used for transforming the reflection vector
		mInvRot.rotate(xRot, 1, 0, 0);
		mInvRot.rotate(yRot, 0, 1, 0);
		
		// draw object
		gl.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesBuffer.get(3 + filter));
		switch (object) {
		case 0:
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glLightModelx(GL10.GL_LIGHT_MODEL_TWO_SIDE, GL10.GL_FALSE);
			cube.calculateReflectionTexCoords(vEye, mInvRot);
			cube.Draw(gl);
			break;
		case 1:
			gl.glDisable(GL10.GL_CULL_FACE);
			gl.glLightModelx(GL10.GL_LIGHT_MODEL_TWO_SIDE, GL10.GL_TRUE);
			cylinder.calculateReflectionTexCoords(vEye, mInvRot);
			cylinder.Draw(gl);
			break;
		case 2:
			gl.glDisable(GL10.GL_CULL_FACE);
			gl.glLightModelx(GL10.GL_LIGHT_MODEL_TWO_SIDE, GL10.GL_TRUE);
			disk.calculateReflectionTexCoords(vEye, mInvRot);
			disk.Draw(gl);
			break;
		case 3:
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glLightModelx(GL10.GL_LIGHT_MODEL_TWO_SIDE, GL10.GL_FALSE);
			sphere.calculateReflectionTexCoords(vEye, mInvRot);
			sphere.Draw(gl);
			break;
		case 4:
			gl.glDisable(GL10.GL_CULL_FACE);
			gl.glLightModelx(GL10.GL_LIGHT_MODEL_TWO_SIDE, GL10.GL_TRUE);
			cone.calculateReflectionTexCoords(vEye, mInvRot);
			cone.Draw(gl);
			break;
		case 5:
			gl.glDisable(GL10.GL_CULL_FACE);
			gl.glLightModelx(GL10.GL_LIGHT_MODEL_TWO_SIDE, GL10.GL_TRUE);
			partialDisk.calculateReflectionTexCoords(vEye, mInvRot);
			partialDisk.Draw(gl);
			break;
		}
		
		// update rotations
		xRot += xSpeed;
		yRot += ySpeed;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// reload textures
		LoadTextures(gl);
		// avoid division by zero
		if (height == 0) height = 1;
		// draw on the entire screen
		gl.glViewport(0, 0, width, height);
		// setup projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 1.0f, 100.0f);
	}
	
	public static void toggleLighting() {
		lighting = !lighting;
	}

	public static void switchToNextFilter() {
		filter = (filter + 1) % 3;
	}

	public static void switchToNextObject() {
		object = (object + 1) % 6;
	}
	
}