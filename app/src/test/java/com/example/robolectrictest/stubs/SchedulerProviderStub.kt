package com.example.robolectrictest.stubs

import com.example.robolectrictest.scheduler.ISchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class SchedulerProviderStub: ISchedulerProvider {
    override fun io(): Scheduler  = Schedulers.trampoline()

    override fun ui(): Scheduler = Schedulers.trampoline()
}