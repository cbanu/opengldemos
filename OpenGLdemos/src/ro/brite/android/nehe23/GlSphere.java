package ro.brite.android.nehe23;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import ro.brite.android.opengl.common.GlMatrix;
import ro.brite.android.opengl.common.GlVertex;
import ro.brite.android.opengl.common.Utils;


class GlSphere extends GlObject {
	
	private float radius;
	private int nrSlices;
	private int nrStacks;
	
	private FloatBuffer coordsBuffer;
	private FloatBuffer normalsBuffer;
	private FloatBuffer texCoordsBuffer;
	
	public GlSphere(float radius, int nrSlices, int nrStacks) {
		this.radius = radius;
		this.nrStacks = nrStacks;
		this.nrSlices = nrSlices;
		generateData();
	}
	
	private void generateData() {
		
		float[] vertexCoords = new float[3 * nrSlices * 2 * (nrStacks + 1)];
		float[] normalCoords = new float[3 * nrSlices * 2 * (nrStacks + 1)];
		float[] textureCoords = new float[2 * nrSlices * 2 * (nrStacks + 1)];
		
		for (int i = 0; i < nrSlices; i++) {
			
			double alpha0 = i * (2 * Math.PI) / nrSlices;
			double alpha1 = (i + 1) * (2 * Math.PI) / nrSlices;
			
			float cosAlpha0 = (float) Math.cos(alpha0);
			float sinAlpha0 = (float) Math.sin(alpha0);
			float cosAlpha1 = (float) Math.cos(alpha1);
			float sinAlpha1 = (float) Math.sin(alpha1);

			int elemIdx = i * 2 * (nrStacks + 1);
			
			for (int j = 0; j <= nrStacks; j++) {
				
				double beta = j * Math.PI / nrStacks - Math.PI / 2;
				
				float cosBeta = (float) Math.cos(beta);
				float sinBeta = (float) Math.sin(beta);
				
				Utils.setXYZ(vertexCoords, 3 * elemIdx + 3 * 2 * j,
						radius * cosBeta * cosAlpha1,
						radius * sinBeta,
						radius * cosBeta * sinAlpha1);
				Utils.setXYZ(vertexCoords, 3 * elemIdx + 3 * 2 * j + 3,
						radius * cosBeta * cosAlpha0,
						radius * sinBeta,
						radius * cosBeta * sinAlpha0);
				
				Utils.setXYZ(normalCoords, 3 * elemIdx + 3 * 2 * j,
						cosBeta * cosAlpha1,
						sinBeta,
						cosBeta * sinAlpha1);
				Utils.setXYZ(normalCoords, 3 * elemIdx + 3 * 2 * j + 3,
						cosBeta * cosAlpha0,
						sinBeta,
						cosBeta * sinAlpha0);

				Utils.setXY(textureCoords, 2 * elemIdx + 2 * 2 * j,
						((float) (i + 1)) / nrSlices,
						((float) j) / nrStacks);
				Utils.setXY(textureCoords, 2 * elemIdx + 2 * 2 * j + 2,
						((float) i) / nrSlices,
						((float) j) / nrStacks);
			}
		}

		coordsBuffer = Utils.wrapDirect(vertexCoords);
		normalsBuffer = Utils.wrapDirect(normalCoords);
		texCoordsBuffer = Utils.wrapDirect(textureCoords);
	}
	
	@Override
	public void draw(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, coordsBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalsBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordsBuffer);
		
		for (int i = 0; i < nrSlices; i++) {
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i * 2 * (nrStacks + 1), 2 * (nrStacks + 1));
		}
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
	
	@Override
	public void calculateReflectionTexCoords(GlVertex vEye, GlMatrix mInvRot) {
		Utils.computeSphereEnvTexCoords(
				vEye.data, mInvRot.data,
				coordsBuffer, normalsBuffer, texCoordsBuffer,
				nrSlices * 2 * (nrStacks + 1));
	}
	
}
