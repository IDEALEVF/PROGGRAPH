package com.example.dames;

import android.opengl.Matrix;

public class Camera {
    private float[] mMVPMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];

    public Camera(){
        Matrix.setLookAtM(mViewMatrix, 0, -1, -1, 5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);


    }

    public void surfaceChanged(int width, int height) {
        float ratio = (float) width / height;
        Matrix.perspectiveM(mProjectionMatrix, 0, 70.0f, ratio, 0.1f, 100.0f );
    }

    public float[] getMVPMatrix(){
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        return mMVPMatrix;
    }
}
