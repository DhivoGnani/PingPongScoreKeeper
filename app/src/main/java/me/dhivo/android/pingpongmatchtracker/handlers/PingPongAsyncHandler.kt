package me.dhivo.android.pingpongmatchtracker.handlers

import android.content.AsyncQueryHandler
import android.content.ContentResolver
import android.net.Uri

import java.lang.ref.WeakReference

class PingPongAsyncHandler(cr: ContentResolver, listener: AsyncQueryListener) : AsyncQueryHandler(cr) {

    private val mListener: WeakReference<AsyncQueryListener> = WeakReference(listener)

    interface AsyncQueryListener {
        fun onInsertComplete(token: Int, cookie: Any, uri: Uri)
    }

    override fun onInsertComplete(token: Int, cookie: Any, uri: Uri) {
        super.onInsertComplete(token, cookie, uri)

        val listener = mListener.get()
        listener?.onInsertComplete(token, cookie, uri)
    }

}