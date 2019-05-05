package com.example.dames;

import android.opengl.GLES31;
import android.util.Log;

public class ShaderHelper {

    public static int compileShader(final int shaderType, final String shaderSource)
    {
        int shaderHandle = GLES31.glCreateShader(shaderType);

        if (shaderHandle != 0)
        {
            GLES31.glShaderSource(shaderHandle, shaderSource);
            GLES31.glCompileShader(shaderHandle);
            final int[] compileStatus = new int[1];
            GLES31.glGetShaderiv(shaderHandle, GLES31.GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0)
            {

                Log.e("ShaderHelper", "Error compiling shader: " + GLES31.glGetShaderInfoLog(shaderHandle));
                GLES31.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0)
        {
            throw new RuntimeException("Error creating shader.");
        }

        return shaderHandle;
    }

    public static int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes)
    {
        int programHandle = GLES31.glCreateProgram();

        if (programHandle != 0)
        {
            GLES31.glAttachShader(programHandle, vertexShaderHandle);
            GLES31.glAttachShader(programHandle, fragmentShaderHandle);

            if (attributes != null)
            {
                final int size = attributes.length;
                for (int i = 0; i < size; i++)
                {
                    GLES31.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }

            GLES31.glLinkProgram(programHandle);

            final int[] linkStatus = new int[1];
            GLES31.glGetProgramiv(programHandle, GLES31.GL_LINK_STATUS, linkStatus, 0);

            if (linkStatus[0] == 0)
            {
                Log.e("ShaderHelper", "Error compiling program: " + GLES31.glGetProgramInfoLog(programHandle));
                GLES31.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }
}
