package com.kvazars.radiot

import android.app.Application
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import com.crashlytics.android.Crashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import com.kvazars.radiot.data.DataModule
import com.kvazars.radiot.di.AppComponent
import com.kvazars.radiot.di.DaggerAppComponent
import com.kvazars.radiot.domain.player.PodcastStreamPlayer
import com.kvazars.radiot.services.BackgroundPlayerService
import com.kvazars.radiot.services.NotificationService
import com.kvazars.radiot.ui.main.MainScreenActivity
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins
import java.io.File


/**
 * Created by Leo on 01.05.2017.
 */
open class RadioTApplication : Application() {
    //region CONSTANTS -----------------------------------------------------------------------------

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private lateinit var appComponent: AppComponent
    private lateinit var serviceIntent: Intent

    //endregion

    //region LOCAL METHODS -------------------------------------------------------------------------

    @SuppressWarnings("CheckReturnValue")
    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .dataModule(DataModule(this, File(cacheDir, "http-cache")))
            .build()

        val crashReportingEnabled = appComponent.appPreferences().crashReportingEnabled
        if (crashReportingEnabled.get()) {
            Fabric.with(this, Crashlytics())
        }

        RxJavaPlugins.setErrorHandler {
            if (crashReportingEnabled.get()) {
                Crashlytics.logException(it)
            }
            it.printStackTrace()
        }

        serviceIntent = BackgroundPlayerService.createLaunchIntent(this)

        appComponent
            .streamPlayer()
            .statusUpdates
            .distinctUntilChanged()
            .filter { it == PodcastStreamPlayer.Status.PLAYING }
            .subscribe(
                {
                    startBackgroundPlayerService()
                }
            )

        NotificationService.setupAlarm(this)
    }

    private fun startBackgroundPlayerService() {
        ContextCompat.startForegroundService(
            this,
            serviceIntent
        )
    }

    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------

    companion object {
        fun getAppComponent(context: Context): AppComponent {
            return (context.applicationContext as RadioTApplication).appComponent
        }

        fun getAppLaunchIntent(context: Context): Intent {
            val intent = Intent(context, MainScreenActivity::class.java)
            intent.action = Intent.ACTION_MAIN
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        }
    }

    //endregion
}