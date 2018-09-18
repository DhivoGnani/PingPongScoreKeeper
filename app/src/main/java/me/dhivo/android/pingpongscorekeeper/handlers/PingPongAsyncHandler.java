package me.dhivo.android.pingpongscorekeeper.handlers;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.net.Uri;

import java.lang.ref.WeakReference;

public class PingPongAsyncHandler extends AsyncQueryHandler {

    private WeakReference<AsyncQueryListener> mListener;

    public interface AsyncQueryListener {
        void onInsertComplete(int token, Object cookie, Uri uri);
    }

    public PingPongAsyncHandler(ContentResolver cr, AsyncQueryListener listener) {
        super(cr);
        mListener = new WeakReference<AsyncQueryListener>(listener);
    }

    public PingPongAsyncHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        super.onInsertComplete(token, cookie, uri);

        final AsyncQueryListener listener = mListener.get();
        if (listener != null) listener.onInsertComplete(token, cookie, uri);
    }

}