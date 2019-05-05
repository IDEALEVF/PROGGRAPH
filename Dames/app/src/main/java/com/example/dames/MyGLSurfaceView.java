package com.example.dames;

import android.content.Context;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;

import java.io.IOException;
import java.io.InputStream;

class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context){
        super(context);

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8 , 8, 8, 8, 16, 4);

        mRenderer = new MyGLRenderer(context);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);// Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
