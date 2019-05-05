package com.example.dames;

import android.content.Context;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MyGLRenderer implements GLSurfaceView.Renderer {

    private final Context mContext;

    Pion mPion;
    Camera mCamera;
    Plateau mPlateau;
    Camera camera2;

    MyGLRenderer(final Context context){
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {



        int vertHandler = ShaderHelper.compileShader(GLES31.GL_VERTEX_SHADER, AssetReader.readTextFileFromAsset(mContext,"PionVertexShader.vert"));
        int fragHandler = ShaderHelper.compileShader(GLES31.GL_FRAGMENT_SHADER, AssetReader.readTextFileFromAsset(mContext,"PionFragmentShader.frag"));
        int vertHandler2 = ShaderHelper.compileShader(GLES31.GL_VERTEX_SHADER, AssetReader.readTextFileFromAsset(mContext,"PlateauVertexShader.vert"));
        int fragHandler2 = ShaderHelper.compileShader(GLES31.GL_FRAGMENT_SHADER, AssetReader.readTextFileFromAsset(mContext,"PlateauFragmentShader.frag"));
        int program = ShaderHelper.createAndLinkProgram(vertHandler, fragHandler, null);
        int program2 = ShaderHelper.createAndLinkProgram(vertHandler2, fragHandler2, null);
        //GLES31.GL_UNSIGNED_INT image =TextureHelper.loadTexture(mContext,AssetReader.readTextFileFromAsset(mContext, "bg.bmp"));
        //int texture=TextureHelper.loadTexture(mContext,mContext.getResources());

        mPion = new Pion(20, program);
        mPion.setCouleur(new float[]{ 0.2f, 0.709803922f, 0.898039216f});
        mPlateau= new Plateau(4,program2);
        mPlateau.setCouleur(new float[]{ 0.2f, 0.709803922f, 0.898039216f});

        mCamera = new Camera();
        camera2=new Camera();

        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void onDrawFrame(GL10 unused) {

        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);
        mPion.draw(mCamera.getMVPMatrix());
        mPlateau.draw(camera2.getMVPMatrix());
    }


    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
        mCamera.surfaceChanged(width, height);
        camera2.surfaceChanged(width, height);
    }


}