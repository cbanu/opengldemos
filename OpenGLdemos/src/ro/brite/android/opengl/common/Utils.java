package ro.brite.android.opengl.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLUtils;


public final class Utils {

	private static Matrix yFlipMatrix;
	
	static {
		yFlipMatrix = new Matrix();
		yFlipMatrix.postScale(1, -1); // flip Y axis
		
		System.loadLibrary("opengl-math");
	}
	
	public static native void computeSphereEnvTexCoords(
		    FloatBuffer vEyeBuff, FloatBuffer mInvRotBuff,
		    FloatBuffer coordsBuf, FloatBuffer normalsBuff, FloatBuffer texCoordsBuff,
		    int length);
	
	public static FloatBuffer wrapDirect(float[] v) {
		ByteBuffer buff = ByteBuffer.allocateDirect(v.length * Float.SIZE / Byte.SIZE);
		buff.order(ByteOrder.nativeOrder());
		
		FloatBuffer data = buff.asFloatBuffer();
		data.put(v);
		data.position(0);
		
		return data;
	}
	
	public static FloatBuffer wrapDirect(float[][] m) {
		int totalLength = 0;
		for (int i = 0; i < m.length; i++)
			totalLength += m[i].length;
		
		ByteBuffer buff = ByteBuffer.allocateDirect(totalLength * Float.SIZE / Byte.SIZE);
		buff.order(ByteOrder.nativeOrder());
		
		FloatBuffer data = buff.asFloatBuffer();
		for (int i = 0; i < m.length; i++)
			data.put(m[i]);
		
		data.position(0);
		
		return data;
	}
	
	public static Bitmap getTextureFromBitmapResource(Context context, int resourceId) {
		
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
			return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), yFlipMatrix, false);
		}
		finally	{
			if (bitmap != null) {
				bitmap.recycle();
			}
		}
	}	

	public static void generateMipmapsForBoundTexture(Bitmap texture) {
		
		// generate the full texture (mipmap level 0)
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, texture, 0);
		
		Bitmap currentMipmap = texture;
		
		int width = texture.getWidth();
		int height = texture.getHeight();
		int level = 0;
		
		boolean reachedLastLevel;
		do {
			
			// go to next mipmap level
			if (width > 1) width /= 2;
			if (height > 1) height /= 2;
			level++;
			reachedLastLevel = (width == 1 && height == 1);
			
			// generate next mipmap
			Bitmap mipmap = Bitmap.createScaledBitmap(currentMipmap, width, height, true);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, mipmap, 0);
			
			// recycle last mipmap (but don't recycle original texture)
			if (currentMipmap != texture)
			{
				currentMipmap.recycle();
			}
			
			// remember last generated mipmap
			currentMipmap = mipmap;
			
		} while (!reachedLastLevel);
		
		// once again, recycle last mipmap (but don't recycle original texture)
		if (currentMipmap != texture) {
			currentMipmap.recycle();
		}
	}

	public static void setXYZ(float[] vector, int offset, float x, float y, float z) {
		vector[offset] = x;
		vector[offset + 1] = y;
		vector[offset + 2] = z;
	}

	public static void setXYZn(float[] vector, int offset, float x, float y, float z) {
		float r = (float)Math.sqrt(x * x + y * y + z * z);
		if (r == 0.0f) r = 1.0f;
		setXYZ(vector, offset, x / r, y / r, z / r);
	}
	
	public static void setXY(float[] vector, int offset, float x, float y) {
		vector[offset] = x;
		vector[offset + 1] = y;
	}

}
