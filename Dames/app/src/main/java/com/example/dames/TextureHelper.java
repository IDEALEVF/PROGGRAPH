package com.example.dames;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES31;
import android.opengl.GLUtils;

public class TextureHelper {

        public static int loadTexture(final Context context, final int resourceId) {
            final int[] textureHandle = new int[1];

            GLES31.glGenTextures(1, textureHandle, 0);

            if (textureHandle[0] == 0) {
                throw new RuntimeException("Error generating texture name.");
            }

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;    // No pre-scaling

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST);
            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();

            return textureHandle[0];
        }
}
