package com.example.hossam1.mongez;

/**
 * Created by nasrallah on 6/30/18.
 */


        import android.content.Context;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private Context mContext;

    private VolleySingleton(Context context) {
        this.mContext = context;
        this.mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public <T> void addRequestQue(Request<T> request) {
        request.setShouldCache(false);
        mRequestQueue.add(request);
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }


}