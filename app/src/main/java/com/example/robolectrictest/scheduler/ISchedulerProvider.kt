package com.example.robolectrictest.scheduler

import io.reactivex.Scheduler

interface ISchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}