package com.danny.xbase.base

import android.app.Application
import java.lang.ref.WeakReference

/**
 * application对象
 *
 * @author danny
 * @since 2020-11-20
 */
object Application {
    private var application: WeakReference<Application>? = null

    fun register(app: Application) {
        synchronized(com.danny.xbase.base.Application::class.java) {
            application = WeakReference(app)
        }
    }

    fun get(): Application? {
        synchronized(com.danny.xbase.base.Application::class.java) {
            return application?.get()
        }
    }
}