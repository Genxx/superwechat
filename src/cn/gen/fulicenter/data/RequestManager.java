package cn.gen.fulicenter.data;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import cn.gen.fulicenter.utils.MD5;

public class RequestManager {
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;

	private static DiskLruImageCache mDiskCache;
//	private static ImageLoader.ImageCache aImageCache = new ImageCacheUtil();

	private RequestManager() {
		// no instances
	}

	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);

		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
				.getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 8;
		mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
		mDiskCache = new DiskLruImageCache(context,"pic",cacheSize, Bitmap.CompressFormat.PNG,100);
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}
	
	public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }
	
	public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

	/**
	 * Returns instance of ImageLoader initialized with {@see FakeImageCache}
	 * which effectively means that no memory caching is used. This is useful
	 * for images that you know that will be show only once.
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}

	public static Bitmap getBitmap(String url) {
		try {
			return mDiskCache.getBitmap(createKey(url));
		} catch (Exception e) {
			throw new IllegalStateException("Disk Cache Not initialized");
		}
	}
	public static void putBitmap(String url,Bitmap bitmap){
		try {
			mDiskCache.putBitmap(createKey(url),bitmap);
		} catch (Exception e) {
			throw new IllegalStateException("Disk Cache Not initialized");
		}
	}

	private static String createKey(String url){
		return MD5.getData(url);
	}
}
