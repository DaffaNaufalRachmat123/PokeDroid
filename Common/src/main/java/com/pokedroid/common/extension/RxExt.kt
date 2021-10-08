package com.pokedroid.common.extension

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * Extension to apply the default schedulers to the [Observable].
 */
fun <T> Observable<T>.applySchedulers(): Observable<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

/**
 * Extension to apply the default schedulers to the [Flowable].
 */
fun <T> Flowable<T>.applySchedulers(): Flowable<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

/**
 * Extension to apply the default schedulers to the [Single].
 */
fun <T> Single<T>.applySchedulers(): Single<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

/**
 * Extension to apply the default schedulers to the [Completable].
 */
fun Completable.applySchedulers(): Completable =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
