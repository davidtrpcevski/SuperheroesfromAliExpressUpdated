package com.example.superheroesfromaliexpress.backgroundtask

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.crazylegend.kotlinextensions.rx.singleFrom
import io.reactivex.Single
import kotlin.random.Random


/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */
class BackgroundWorker(private val appContext: Context, workerParams: WorkerParameters) : RxWorker(appContext, workerParams) {

    override fun createWork(): Single<Result> {
        return doWork()
    }

    private fun doWork(): Single<Result> {
        val notifyUtils = NotifyUtils(appContext)
        val random = Random.nextInt(1, 563)

        notifyUtils.createNotification(random.toString(), "You're given a random hero", "Surprise", random)

        return singleFrom {
            Result.success()
        }
    }

}