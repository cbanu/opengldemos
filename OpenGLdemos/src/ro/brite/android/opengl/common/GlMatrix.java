package ro.brite.android.opengl.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GlMatrix {
	
	static {
		System.loadLibrary("opengl-math");
	}
	
	private static native void identity(FloatBuffer fb);
	private static native void assign(FloatBuffer fbDst, FloatBuffer fbSrc);
	private static native void multiply(FloatBuffer fbDst, FloatBuffer fbSrc);
	private static native void premultiply(FloatBuffer fbDst, FloatBuffer fbSrc);
	private static native void translate(FloatBuffer fb, float dx, float dy, float dz);
	private static native void rotate(FloatBuffer fb, float angle, float x, float y, float z);
	private static native void transform(FloatBuffer matrixBuff, FloatBuffer vertexBuff);
	private static native void transpose(FloatBuffer matrixBuff);
	
	public FloatBuffer data;

	private void allocate() {
		ByteBuffer mem = ByteBuffer.allocateDirect(16 * Float.SIZE / Byte.SIZE);
		mem.order(ByteOrder.nativeOrder());
		data = mem.asFloatBuffer();
	}	
	
	public GlMatrix() {
		allocate();
		identity();
	}
	
	public void identity() {
		GlMatrix.identity(data);
	}
	
	public void assign(GlMatrix matrix) {
		GlMatrix.assign(data, matrix.data);
	}
	
	public void multiply(GlMatrix matrix) {
		GlMatrix.multiply(data, matrix.data);
	}
	
	public void premultiply(GlMatrix matrix) {
		GlMatrix.premultiply(data, matrix.data);
	}
	
	public void translate(float dx, float dy, float dz) {
		GlMatrix.translate(data, dx, dy, dz);
	}
	
	public void rotate(float angle, float x, float y, float z) {
		GlMatrix.rotate(data, angle, x, y, z);
	}
	
	public void transform(GlVertex vertex) {
		GlMatrix.transform(data, vertex.data);
	}
	
	public void transpose() {
		GlMatrix.transpose(data);
	}
	
}
