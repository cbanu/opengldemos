package ro.brite.android.nehe23;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import ro.brite.android.opengl.common.GlMatrix;
import ro.brite.android.opengl.common.GlVertex;
import ro.brite.android.opengl.common.Utils;


class GlCylinder extends GlObject {
	
	float base;
	float top;
	float height;
	int nrSlices;
	int nrStacks;
	
	private FloatBuffer coordsBuffer;
	private FloatBuffer normalsBuffer;
	private FloatBuffer texCoordsBuffer;
	
	public GlCylinder(float base, float top, float height, int nrSlices, int nrStacks) {
		this.base = base;
		this.top = top;
		this.height = height;
		this.nrSlices = nrSlices;
		this.nrStacks = nrStacks;
		generateData();
	}
	
	private void generateData() {
		
		float[] vertexCoords = new float[3 * nrSlices * 2 * (nrStacks + 1)];
		float[] normalCoords = new float[3 * nrSlices * 2 * (nrStacks + 1)];
		float[] textureCoords = new float[2 * nrSlices * 2 * (nrStacks + 1)];
		
		for (int i = 0; i < nrSlices; i++) {
			
			double alpha0 = (i + 0) * (2 * Math.PI) / nrSlices;
			double alpha1 = (i + 1) * (2 * Math.PI) / nrSlices;

			float cosAlpha0 = (float) Math.cos(alpha0);
			float sinAlpha0 = (float) Math.sin(alpha0);
			float cosAlpha1 = (float) Math.cos(alpha1);
			float sinAlpha1 = (float) Math.sin(alpha1);

			int elemIdx = i * 2 * (nrStacks + 1);
			
			for (int j = 0; j <= nrStacks; j++) {

				float z = height * (0.5f - ((float)j) / nrStacks);
				float r = top + (base - top) * j / nrStacks;
				
				Utils.setXYZ(vertexCoords, 3 * elemIdx + 3 * 2 * j,
						r * cosAlpha1, r * sinAlpha1, z);

				Utils.setXYZ(vertexCoords, 3 * elemIdx + 3 * 2 * j + 3,
						r * cosAlpha0, r * sinAlpha0, z);
				
				Utils.setXYZn(normalCoords, 3 * elemIdx + 3 * 2 * j,
						height * cosAlpha1,
						height * sinAlpha1,
						base - top);
				Utils.setXYZn(normalCoords, 3 * elemIdx + 3 * 2 * j + 3,
						height * cosAlpha0,
						height * sinAlpha0,
						base - top);

				textureCoords[2 * elemIdx + 2 * 2 * j + 0] = ((float) (i + 1)) / nrSlices;
				textureCoords[2 * elemIdx + 2 * 2 * j + 1] = ((float) (j + 0)) / nrStacks;
				
				textureCoords[2 * elemIdx + 2 * 2 * j + 2] = ((float) (i + 0)) / nrSlices;
				textureCoords[2 * elemIdx + 2 * 2 * j + 3] = ((float) (j + 0)) / nrStacks;
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
