#include <jni.h>
#include <math.h>
#include <string.h>

// *** GlVertex *** //

void vertexNormalize(float* v)
{
  jfloat x = v[0];
  jfloat y = v[1];
  jfloat z = v[2];

  jfloat w = sqrtf( x*x + y*y + z*z );

  if (w != 0.0f) {
    v[0] = x / w;
    v[1] = y / w;
    v[2] = z / w;
  }

  v[3] = 1.0f;
}


void vertexAssign(float* dst, float* src)
{
  dst[0] = src[0];
  dst[1] = src[1];
  dst[2] = src[2];
  dst[3] = src[3];
}


void vertexAdd(float* dst, float* src)
{
  dst[0] += src[0];
  dst[1] += src[1];
  dst[2] += src[2];
}


void vertexSubtract(float* dst, float* src)
{
  dst[0] -= src[0];
  dst[1] -= src[1];
  dst[2] -= src[2];
}


void vertexScale(float* dst, float factor)
{
  dst[0] *= factor;
  dst[1] *= factor;
  dst[2] *= factor;
}


float vertexDotProduct(float* v1, float* v2)
{
  // vectors must be normalized
  return (v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2]);
}


void
Java_ro_brite_android_opengl_common_GlVertex_set(
    JNIEnv* env, jobject thiz,
    jobject buff, jfloat x, jfloat y, jfloat z, jfloat w)
{
  jfloat* data = (*env)->GetDirectBufferAddress(env, buff);
  data[0] = x;
  data[1] = y;
  data[2] = z;
  data[3] = w;
}


void
Java_ro_brite_android_opengl_common_GlVertex_assign(
    JNIEnv* env, jobject thiz,
    jobject dstBuff, jobject srcBuff)
{
  jfloat* dstData = (*env)->GetDirectBufferAddress(env, dstBuff);
  jfloat* srcData = (*env)->GetDirectBufferAddress(env, srcBuff);
  vertexAssign(dstData, srcData);
}


void
Java_ro_brite_android_opengl_common_GlVertex_add(
    JNIEnv* env, jobject thiz,
    jobject dstBuff, jobject srcBuff)
{
  jfloat* dstData = (*env)->GetDirectBufferAddress(env, dstBuff);
  jfloat* srcData = (*env)->GetDirectBufferAddress(env, srcBuff);
  vertexAdd(dstData, srcData);
}


void
Java_ro_brite_android_opengl_common_GlVertex_subtract(
    JNIEnv* env, jobject thiz,
    jobject dstBuff, jobject srcBuff)
{
  jfloat* dstData = (*env)->GetDirectBufferAddress(env, dstBuff);
  jfloat* srcData = (*env)->GetDirectBufferAddress(env, srcBuff);
  vertexSubtract(dstData, srcData);
}


void
Java_ro_brite_android_opengl_common_GlVertex_normalize(
    JNIEnv* env, jobject thiz,
    jobject buff)
{
  jfloat* data = (*env)->GetDirectBufferAddress(env, buff);
  vertexNormalize(data);
}


void
Java_ro_brite_android_opengl_common_GlVertex_scale(
    JNIEnv* env, jobject thiz,
    jobject buff, jfloat factor)
{
  jfloat* data = (*env)->GetDirectBufferAddress(env, buff);
  vertexScale(data, factor);
}


jfloat
Java_ro_brite_android_opengl_common_GlVertex_dotProduct(
    JNIEnv* env, jobject thiz,
    jobject buff1, jobject buff2)
{
  jfloat* data1 = (*env)->GetDirectBufferAddress(env, buff1);
  jfloat* data2 = (*env)->GetDirectBufferAddress(env, buff2);
  return vertexDotProduct(data1, data2);
}


// *** GlMatrix *** //

void matrixMakeIdentity(float* data)
{
  int i, j;
  for (i = 0; i < 4; i++)
    for (j = 0; j < 4; j++)
      data[4 * i + j] = (i != j) ? 0 : 1;
}


void matrixAssign(float* dst, float* src)
{
  int i;
  for (i = 0; i < 16; i++)
    dst[i] = src[i];
}


void matrixMultiply(float* dst, float* src)
{
  float tmp[16];

  int i, j, k;
  for (i = 0; i < 4; i++)
    for (j = 0; j < 4; j++) {
      int tmpIdx = 4 * i + j;
      tmp[tmpIdx] = 0.0f;
      for (k = 0; k < 4; k++) {
        tmp[tmpIdx] += dst[4 * i + k] * src[4 * k + j];
      }
    }

  matrixAssign(dst, tmp);
}


void matrixTransform(float* m, float* v)
{
  jfloat tmp[4];
  int i, j;
  for (i = 0; i < 4; i++) {
    tmp[i] = 0.0f;
    for (j = 0; j < 4; j++) {
      tmp[i] += m[4 * i + j] * v[j];
    }
  }
  
  vertexAssign(v, tmp);
}


void
Java_ro_brite_android_opengl_common_GlMatrix_identity(
    JNIEnv* env, jobject thiz,
    jobject buff)
{
  jfloat* data = (*env)->GetDirectBufferAddress(env, buff);
  matrixMakeIdentity(data);
}


void
Java_ro_brite_android_opengl_common_GlMatrix_assign(
    JNIEnv* env, jobject thiz,
    jobject dstBuff, jobject srcBuff)
{
  jfloat* dstData = (*env)->GetDirectBufferAddress(env, dstBuff);
  jfloat* srcData = (*env)->GetDirectBufferAddress(env, srcBuff);
  matrixAssign(dstData, srcData);
}


void
Java_ro_brite_android_opengl_common_GlMatrix_multiply(
    JNIEnv* env, jobject thiz,
    jobject dstBuff, jobject srcBuff)
{
  jfloat* dstData = (*env)->GetDirectBufferAddress(env, dstBuff);
  jfloat* srcData = (*env)->GetDirectBufferAddress(env, srcBuff);
  matrixMultiply(dstData, srcData);
}


void
Java_ro_brite_android_opengl_common_GlMatrix_translate(
    JNIEnv* env, jobject thiz,
    jobject buff, jfloat dx, jfloat dy, jfloat dz)
{
  jfloat* data = (*env)->GetDirectBufferAddress(env, buff);
  jfloat tr[16];

  matrixMakeIdentity(tr);
  tr[0 * 4 + 3] = dx;
  tr[1 * 4 + 3] = dy;
  tr[2 * 4 + 3] = dz;
  matrixMultiply(data, tr);
}


void
Java_ro_brite_android_opengl_common_GlMatrix_rotate(
    JNIEnv* env, jobject thiz,
    jobject buff, jfloat angle, jfloat x, jfloat y, jfloat z)
{
  jfloat* data = (*env)->GetDirectBufferAddress(env, buff);

  angle *= (jfloat) (M_PI / 180.0f);

  jfloat c = cosf(angle);
  jfloat s = sinf(angle);
  jfloat _c = 1.0f - c;

  float v[4];
  v[0] = x; v[1] = y; v[2] = z;
  vertexNormalize(v);
  x = v[0]; y = v[1]; z = v[2];

  float rot[16];

  rot[0] = x * x * _c + c;
  rot[1] = x * y * _c - z * s;
  rot[2] = x * z * _c + y * s;
  rot[3] = 0.0f;

  rot[4] = y * x * _c + z * s;
  rot[5] = y * y * _c + c;
  rot[6] = y * z * _c - x * s;
  rot[7] = 0.0f;

  rot[8] = z * x * _c - y * s;
  rot[9] = z * y * _c + x * s;
  rot[10] = z * z * _c + c;
  rot[11] = 0.0f;

  rot[12] = 0.0f;
  rot[13] = 0.0f;
  rot[14] = 0.0f;
  rot[15] = 1.0f;

  matrixMultiply(data, rot);
}


void
Java_ro_brite_android_opengl_common_GlMatrix_transform(
    JNIEnv* env, jobject thiz,
    jobject matrixBuff, jobject vertexBuff)
{
  jfloat* m = (*env)->GetDirectBufferAddress(env, matrixBuff);
  jfloat* v = (*env)->GetDirectBufferAddress(env, vertexBuff);
  matrixTransform(m, v);
}


// *** Utils *** //

void
Java_ro_brite_android_opengl_common_Utils_computeSphereEnvTexCoords(
    JNIEnv* env, jobject thiz,
    jobject vEyeBuff, jobject mInvRotBuff,
    jobject coordsBuff, jobject normalsBuff, jobject texCoordsBuff,
    int length)
{
  jfloat* vEye = (*env)->GetDirectBufferAddress(env, vEyeBuff);
  jfloat* mInvRot = (*env)->GetDirectBufferAddress(env, mInvRotBuff);
  jfloat* coords = (*env)->GetDirectBufferAddress(env, coordsBuff);
  jfloat* normals = (*env)->GetDirectBufferAddress(env, normalsBuff);
  jfloat* texCoords = (*env)->GetDirectBufferAddress(env, texCoordsBuff);

  float vP[4];
  float vN[4];

  float vE[4];
  float vR[4];

  float x, y, z;
  float p;

  const int fSize = sizeof(float);
 
  int i;
  for (i = 0; i < length; i++)
  {
    memcpy(vP, coords + 3 * i, 3 * fSize);
    vP[3] = 1.0f;

    memcpy(vN, normals + 3 * i, 3 * fSize);
    vN[3] = 1.0f;

    vertexAssign(vE, vEye);
    vertexSubtract(vE, vP);
    vertexNormalize(vE);

    float cos = vertexDotProduct(vE, vN);
    vertexAssign(vR, vN);
	vertexScale(vR, 2 * cos);
	vertexSubtract(vR, vE);
	
	matrixTransform(mInvRot, vR);
    x = vR[0];
    y = vR[1];
    z = vR[2] + 1.01f;

    p = sqrtf( x*x + y*y + z*z );

    texCoords[2 * i]     = (p != 0) ? 0.5f * (x / p + 1) : 0; // S coord
    texCoords[2 * i + 1] = (p != 0) ? 0.5f * (y / p + 1) : 0; // T coord
  }
}
