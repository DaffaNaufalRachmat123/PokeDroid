package com.android.pokedroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.multidex.MultiDex
import androidx.sqlite.db.SupportSQLiteDatabase
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import com.pokedroid.common.base.BaseApp
import com.github.ajalt.timberkt.e
import com.github.ajalt.timberkt.i
import com.pokedroid.core.local.AppDatabase
import com.pokedroid.core.local.DatabaseProvider
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject
import java.lang.reflect.Method

open class App : BaseApp() {
    private var deepLinkReceiver: BroadcastReceiver? = null
    private val dbProvider by inject<DatabaseProvider>()

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        //DatabaseManager.init(this)
        showDebugDBAddressLogToast(this)
        setInMemoryRoomDatabases(dbProvider.getInstance().openHelper.writableDatabase)
    }


    open fun showDebugDBAddressLogToast(context: Context?) {
        if (BuildConfig.DEBUG) {
            try {
                val debugDB = Class.forName("com.amitshekhar.DebugDB")
                val getAddressLog: Method = debugDB.getMethod("getAddressLog")
                val value: Any? = getAddressLog.invoke(null)
                i { "Debug_Database $value" }
            } catch (ignore: Exception) {
                e { "${ignore.message}" }
            }
        }
    }

    // for in memory database debugging
    private fun setInMemoryRoomDatabases(vararg database: SupportSQLiteDatabase) {
        if (BuildConfig.DEBUG) {
            try {
                val debugDB = Class.forName("com.amitshekhar.DebugDB")
                val argTypes = arrayOf<Class<*>>(HashMap::class.java)
                val inMemoryDatabases = HashMap<String, SupportSQLiteDatabase>()
                // set your inMemory databases
                inMemoryDatabases[AppDatabase.DATABASE_NAME] = database[0]
                val setRoomInMemoryDatabase =
                    debugDB.getMethod("setInMemoryRoomDatabases", *argTypes)
                setRoomInMemoryDatabase.invoke(null, inMemoryDatabases)
            } catch (ignore: Exception) {

            }
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        deepLinkReceiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
        }
    }
}