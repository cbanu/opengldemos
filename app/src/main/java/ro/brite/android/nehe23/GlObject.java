package ro.brite.android.nehe23;

import ro.brite.android.opengl.common.GlVertex;
import ro.brite.android.opengl.common.GlMatrix;

import javax.microedition.khronos.opengles.GL10;


abstract class GlObject {
	public abstract void draw(GL10 gl);
	public abstract void calculateReflectionTexCoords(GlVertex vEye, GlMatrix mInvRot);
}
