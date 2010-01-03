package ro.brite.android.opengl.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GlVertex {
	
	static {
		System.loadLibrary("opengl-math");
	}
	
	private static native void set(FloatBuffer fb, float x, float y, float z, float w);
	private static native void assign(FloatBuffer fbDst, FloatBuffer fbSrc);
	private static native void add(FloatBuffer fbDst, FloatBuffer fbSrc);
	private static native void subtract(FloatBuffer fbDst, FloatBuffer fbSrc);
	private static native void normalize(FloatBuffer fb);
	private static native void scale(FloatBuffer fb, float factor);
	private static native float dotProduct(FloatBuffer a, FloatBuffer b);
	
	public FloatBuffer data;

	private void allocate() {
		ByteBuffer mem = ByteBuffer.allocateDirect(4 * Float.SIZE / Byte.SIZE);
		mem.order(ByteOrder.nativeOrder());
		data = mem.asFloatBuffer();
	}

	public GlVertex() {
		allocate();
		GlVertex.set(data, 0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public GlVertex(float x, float y, float z) {
		allocate();
		GlVertex.set(data, x, y, z, 1.0f);
	}
	
	public GlVertex(GlVertex vertex) {
		allocate();
		GlVertex.assign(data, vertex.data);
	}

	public void assign(GlVertex vertex) {
		GlVertex.assign(data, vertex.data);
	}

	public void add(GlVertex vertex) {
		GlVertex.add(data, vertex.data);
	}

	public void subtract(GlVertex vertex) {
		GlVertex.subtract(data, vertex.data);
	}
	
	public void normalize() {
		GlVertex.normalize(data);
	}
	
	public void scale(float factor) {
		GlVertex.scale(data, factor);
	}
	
	public static float dotProduct(GlVertex a, GlVertex b) {
		return GlVertex.dotProduct(a.data, b.data);
	}
}
