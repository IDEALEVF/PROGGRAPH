package com.example.dames;

import android.opengl.GLES31;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;


public class Pion {

    private final int mProgram;
    private final int mColorHandle;
    private final int mMVPHandle;
    private final int mPositionHandle;
    private final int mNormaleHandle;

    private float couleur[];

    private FloatBuffer sommetsBuffer;
    private ShortBuffer indicesBuffer;
    private FloatBuffer normalesBuffer;

    private static final int COORDS_PER_VERTEX = 3;


    /**
     * @param nbCotes le nombre de cotés du pion
     */
    public Pion(int nbCotes, int program) {
        mProgram = program;

        double angle = (2 * Math.PI) / nbCotes;
        float[] sommets = new float[(nbCotes * 2 + 2) * 3];
        float[] normales = new float[(nbCotes * 2 + 2) * 3];

        sommets[0] = 0.0f;
        sommets[1] = 0.0f;
        sommets[2] = 0.0f;

        sommets[(nbCotes + 1) * 3] = 0.0f;
        sommets[(nbCotes + 1) * 3 + 1] = 0.5f;
        sommets[(nbCotes + 1) * 3 + 2] = 0.0f;

        Arrays.fill(normales, 0.0f);

        for ( int i = 0; i < nbCotes; ++i) {
            float x = (float)Math.cos(i * angle);
            float z = (float)Math.sin(i * angle);
            sommets[(1 + i) * 3] = x;
            sommets[(1 + i) * 3 + 1] = 0.0f;
            sommets[(1 + i) * 3 + 2] = z;

            sommets[(2 + i + nbCotes) * 3 ] = x;
            sommets[(2 + i + nbCotes) * 3 + 1] = 0.5f;
            sommets[(2 + i + nbCotes) * 3 + 2] = z;
        }

        short[] indices = new short[nbCotes * 4 * 3];
        for (short i = 0; i < nbCotes; ++i) {
            indices[i * 3] = 0;
            indices[i * 3 + 1] = (short) (i + 1);
            indices[i * 3 + 2] = (short) ((i + 1) % nbCotes + 1);

            indices[(i + nbCotes) * 3] = (short) (nbCotes + 1);
            indices[(i + nbCotes) * 3 + 1] = (short) (nbCotes + 2 + i);
            indices[(i + nbCotes) * 3 + 2] = (short) (nbCotes + (i + 1) % nbCotes + 2);

            indices[(i + nbCotes * 2) * 3] = (short) (i + 1);
            indices[(i + nbCotes * 2) * 3 + 1] = (short) ((i + 1) % nbCotes + 1);
            indices[(i + nbCotes * 2) * 3 + 2] = (short) (nbCotes + 2 + (i + 1) % nbCotes);

            indices[(i + nbCotes * 3) * 3] = (short) (nbCotes + 2 + (i + 1) % nbCotes);
            indices[(i + nbCotes * 3) * 3 + 1] = (short) (nbCotes + 2 + i);
            indices[(i + nbCotes * 3) * 3 + 2] = (short) (i + 1);
        }

        for(int i = 0 ; i < indices.length ; i += 3){
            int v1 = indices[i];
            int v2 = indices[i + 1];
            int v3 = indices[i + 2];

            float[] n = UtilsKt.triangle_normal(new float[]{sommets[v1 * 3], sommets[v1 * 3 + 1], sommets[v1 * 3 + 2]},
                    new float[]{sommets[v2 * 3], sommets[v2 * 3 + 1], sommets[v2 * 3 + 2]},
                    new float[]{sommets[v3 * 3], sommets[v3 * 3 + 1], sommets[v3 * 3 + 2]});

            normales[v1 * 3] += n[0];
            normales[v1 * 3 + 1] += n[1];
            normales[v1 * 3 + 2] += n[2];

            normales[v2 * 3] += n[0];
            normales[v2 * 3 + 1] += n[1];
            normales[v2 * 3 + 2] += n[2];

            normales[v3 * 3] += n[0];
            normales[v3 * 3 + 1] += n[1];
            normales[v3 * 3 + 2] += n[2];
        }

        for(int i = 0 ; i < normales.length ; i += 3) {
            float [] n = UtilsKt.normalize(new float[]{normales[i], normales[i + 1], normales[i + 2]});
            normales[i] = n[0];
            normales[i + 1] = n[1];
            normales[i + 2] = n[2];

        }


        ByteBuffer bb = ByteBuffer.allocateDirect(
                sommets.length * 4);
        bb.order(ByteOrder.nativeOrder());

        sommetsBuffer = bb.asFloatBuffer();
        sommetsBuffer.put(sommets);
        sommetsBuffer.position(0);

        bb = ByteBuffer.allocateDirect(
                normales.length * 4);
        bb.order(ByteOrder.nativeOrder());
        normalesBuffer = bb.asFloatBuffer();
        normalesBuffer.put(normales);
        normalesBuffer.position(0);

        bb = ByteBuffer.allocateDirect(
                indices.length * 2);
        bb.order(ByteOrder.nativeOrder());
        indicesBuffer = bb.asShortBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0);

        mPositionHandle = GLES31.glGetAttribLocation(mProgram, "vPosition");
        mNormaleHandle = GLES31.glGetAttribLocation(mProgram, "vNormale");
        mColorHandle = GLES31.glGetUniformLocation(mProgram, "vColor");
        mMVPHandle = GLES31.glGetUniformLocation(mProgram, "uMVPMatrix");

    }



    public void setCouleur(float[] c){
        couleur = c;
    }

    public void draw(float[] MVPMatrix) {
        GLES31.glUseProgram(mProgram);

        GLES31.glEnableVertexAttribArray(mPositionHandle);
        GLES31.glEnableVertexAttribArray(mNormaleHandle);

        int vertexStride = COORDS_PER_VERTEX * 4;
        GLES31.glVertexAttribPointer(mPositionHandle,COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, vertexStride, sommetsBuffer);
        GLES31.glVertexAttribPointer(mNormaleHandle,COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, vertexStride, normalesBuffer);
        GLES31.glUniformMatrix4fv(mMVPHandle, 1, false, MVPMatrix, 0);
        GLES31.glUniform3fv(mColorHandle, 1, couleur, 0);

        GLES31.glDrawElements(GLES31.GL_TRIANGLES, indicesBuffer.capacity(), GLES31.GL_UNSIGNED_SHORT, indicesBuffer);

        GLES31.glDisableVertexAttribArray(mPositionHandle);
        GLES31.glUseProgram(0);
    }
}
