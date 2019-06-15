package com.example.superheroesfromaliexpress.app

import android.app.Application
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.superheroesfromaliexpress.backgroundtask.BackgroundWorker
import java.util.concurrent.TimeUnit


/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */
class HeroApp : Application() {
    companion object {
        private const val Notify_JOB_ID = "1331"
    }



    override fun onCreate() {
        super.onCreate()

        val constraints = Constraints.Builder()
        val notificationCheckBuilder = PeriodicWorkRequestBuilder<BackgroundWorker>(20, TimeUnit.MINUTES)
        notificationCheckBuilder.addTag(Notify_JOB_ID)
        notificationCheckBuilder.setConstraints(constraints.build())
        WorkManager.getInstance(this).enqueue(notificationCheckBuilder.build())


    }
}