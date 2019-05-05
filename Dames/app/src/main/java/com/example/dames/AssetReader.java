package com.example.dames;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class AssetReader {

    public static String readTextFileFromAsset(final Context context,
                                                     final String filename)
    {
        String ans = "";
        try {
            InputStream is = context.getAssets().open(filename);
            int i;
            while((i = is.read()) != -1){
                char c = (char) i;
                ans += c;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ans;
    }


}
