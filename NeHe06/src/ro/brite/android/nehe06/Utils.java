package ro.brite.android.nehe06;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

final class Utils {

	private static Matrix xFlipMatrix;
	
	static
	{
		xFlipMatrix = new Matrix();
		xFlipMatrix.postScale(-1, 1); // flip X axis
	}
	
	public static Bitmap getTextureFromBitmapResource(Context context, int resourceId)
	{
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
			return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), xFlipMatrix, false);
		}
		finally	{
			if (bitmap != null) {
				bitmap.recycle();
			}
		}
	}
	
}
