package ro.brite.android.nehe23;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import ro.brite.android.opengl.common.GlMatrix;
import ro.brite.android.opengl.common.GlVertex;
import ro.brite.android.opengl.common.Utils;


class GlDisk extends GlObject {
	
	private float innerRadius;
	private float outerRadius;
	private int nrSlices;
	private int nrLoops;
	private float startAngle;
	private float stopAngle;
	
	private FloatBuffer coordsBuffer;
	private FloatBuffer normalsBuffer;
	private FloatBuffer texCoordsBuffer;
	
	public GlDisk(float innerRadius, float outerRadius, int slices, int loops) {
		this(innerRadius, outerRadius, slices, loops, 0.0f, (float)(2 * Math.PI));
	}
	
	public GlDisk(float innerRadius, float outerRadius, int slices, int loops, float startAngle, float stopAngle) {
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.nrSlices = slices;
		this.nrLoops = loops;
		this.startAngle = startAngle;
		this.stopAngle = stopAngle;
		generateData();
	}
	
	private void generateData() {
		
		float[] vertexCoords = new float[3 * nrLoops * 2 * (nrSlices + 1)];
		float[] normalCoords = new float[3 * nrLoops * 2 * (nrSlices + 1)];
		float[] textureCoords = new float[2 * nrLoops * 2 * (nrSlices + 1)];
		
		for (int i = 0; i < nrLoops; i++) {
			
			float r0 = innerRadius + (outerRadius - innerRadius) * i / nrLoops;
			float r1 = innerRadius + (outerRadius - innerRadius) * (i + 1) / nrLoops;
			
			int elemIdx = i * 2 * (nrSlices + 1);
			
			for (int j = 0; j <= nrSlices; j++) {
				
				double alpha = startAngle + (stopAngle - startAngle) * j / nrSlices;
				
				float sinAlpha = (float) Math.sin(alpha);
				float cosAlpha = (float) Math.cos(alpha);
				
				Utils.setXYZ(vertexCoords, 3 * elemIdx + 3 * 2 * j,
						cosAlpha * r0, sinAlpha * r0, 0);
				Utils.setXYZ(vertexCoords, 3 * elemIdx + 3 * 2 * j + 3,
						cosAlpha * r1, sinAlpha * r1, 0);

				Utils.setXYZ(normalCoords, 3 * elemIdx + 3 * 2 * j,
						0, 0, 1);
				Utils.setXYZ(normalCoords, 3 * elemIdx + 3 * 2 * j + 3,
						0, 0, 1);

				Utils.setXY(textureCoords, 2 * elemIdx + 2 * 2 * j,
						((float)j) / nrSlices, ((float)i) / nrLoops);
				Utils.setXY(textureCoords, 2 * elemIdx + 2 * 2 * j + 2,
						((float)j) / nrSlices, ((float)i + 1) / nrLoops);
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
		
		for (int i = 0; i < nrLoops; i++) {
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i * 2 * (nrSlices + 1), 2 * (nrSlices + 1));
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
				nrLoops * 2 * (nrSlices + 1));
	}
	
}
