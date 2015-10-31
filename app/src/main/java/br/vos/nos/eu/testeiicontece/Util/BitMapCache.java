package br.vos.nos.eu.testeiicontece.Util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Lucas on 24/10/2015.
 */
public class BitMapCache extends LruCache<String,Bitmap> implements ImageLoader.ImageCache {

    public BitMapCache() {
        super(getDefaultLruCacheSize());
    }

    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        return maxMemory/8;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url,bitmap);
    }
}
