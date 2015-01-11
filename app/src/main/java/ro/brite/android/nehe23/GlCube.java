package ro.brite.android.nehe23;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import ro.brite.android.opengl.common.GlMatrix;
import ro.brite.android.opengl.common.GlVertex;
import ro.brite.android.opengl.common.Utils;


class GlCube extends GlObject {
	
	private final static float[][] cubeVertexCoordsTemplate = new float[][] {
		new float[] { // top
			 1, 1,-1,
			-1, 1,-1,
			-1, 1, 1,
			 1, 1, 1
		},
		new float[] { // bottom
			 1,-1, 1,
			-1,-1, 1,
			-1,-1,-1,
			 1,-1,-1
		},
		new float[] { // front
			 1, 1, 1,
			-1, 1, 1,
			-1,-1, 1,
			 1,-1, 1
		},
		new float[] { // back
			 1,-1,-1,
			-1,-1,-1,
			-1, 1,-1,
			 1, 1,-1
		},
		new float[] { // left
			-1, 1, 1,
			-1, 1,-1,
			-1,-1,-1,
			-1,-1, 1
		},
		new float[] { // right
			 1, 1,-1,
			 1, 1, 1,
			 1,-1, 1,
			 1,-1,-1
		},
	};

	private final static float[][] cubeNormalCoords = new float[][] {
		new float[] { // top
			 0, 1, 0,
			 0, 1, 0,
			 0, 1, 0,
			 0, 1, 0
		},
		new float[] { // bottom
			 0,-1, 0,
			 0,-1, 0,
			 0,-1, 0,
			 0,-1, 0
		},
		new float[] { // front
			 0, 0, 1,
			 0, 0, 1,
			 0, 0, 1,
			 0, 0, 1
		},
		new float[] { // back
			 0, 0,-1,
			 0, 0,-1,
			 0, 0,-1,
			 0, 0,-1
		},
		new float[] { // left
			-1, 0, 0,
			-1, 0, 0,
			-1, 0, 0,
			-1, 0, 0
		},
		new float[] { // right
			 1, 0, 0,
			 1, 0, 0,
			 1, 0, 0,
			 1, 0, 0
		},
	};
	
	private final static float[][] cubeTextureCoords = new float[][] {
		new float[] { // top
			1, 0,
			1, 1,
			0, 1,
			0, 0
		},
		new float[] { // bottom
			0, 0,
			1, 0,
			1, 1,
			0, 1
		},
		new float[] { // front
			1, 1,
			0, 1,
			0, 0,
			1, 0
		},
		new float[] { // back
			0, 1,
			0, 0,
			1, 0,
			1, 1
		},
		new float[] { // left
			1, 1,
			0, 1,
			0, 0,
			1, 0
		},
		new float[] { // right
			0, 1,
			0, 0,
			1, 0,
			1, 1
		},
	};

	private final static FloatBuffer normalsBfr;
	
	static {
		normalsBfr = Utils.wrapDirect(cubeNormalCoords);
	}
	
	private float size;
	
	private FloatBuffer coordsBfr;
	private FloatBuffer texCoordsBfr;
	
	public GlCube(float size) {
		this.size = size;
		generateData();
	}
	
	private void generateData() {
		coordsBfr = Utils.wrapDirect(cubeVertexCoordsTemplate);
		for (int i = 0; i < coordsBfr.capacity(); i++)
		{
			coordsBfr.put(i, size * coordsBfr.get(i));
		}

		texCoordsBfr = Utils.wrapDirect(cubeTextureCoords);
	}

	@Override
	public void draw(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, coordsBfr);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalsBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordsBfr);

		for (int i = 0; i < 6; i++) {
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 4 * i, 4);
		}
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
	
	@Override
	public void calculateReflectionTexCoords(GlVertex vEye, GlMatrix mInvRot) {
		Utils.computeSphereEnvTexCoords(vEye.data, mInvRot.data,
				coordsBfr, normalsBfr, texCoordsBfr, 24);
	}
	
}
