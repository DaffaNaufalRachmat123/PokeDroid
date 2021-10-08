package com.pokedroid.common.utils

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.*

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    data class Failed(val message: String?) : ViewState<Nothing>()
}

class StateMachineSingle<T> : SingleTransformer<T, ViewState<T>> {

    override fun apply(upstream: Single<T>): SingleSource<ViewState<T>> {
        return upstream
            .map {
                ViewState.Success(it) as ViewState<T>
            }
            .onErrorReturn {
                ViewState.Failed(it.message)
            }
            .doOnSubscribe {
                ViewState.Loading
            }
    }
}

class StateMachineObservable<T> : ObservableTransformer<T, ViewState<T>> {

    override fun apply(upstream: Observable<T>): ObservableSource<ViewState<T>> {
        return upstream
            .map {
                ViewState.Success(it) as ViewState<T>
            }
            .onErrorReturn {
                ViewState.Failed(it.message)
            }
            .doOnSubscribe {
                ViewState.Loading
            }
    }
}

fun <T> MutableLiveData<ViewState<T>>.setSuccess(data: T? = null) =
    if (data != null) {
        postValue(ViewState.Success(data))
    } else {
        postValue(ViewState.Failed("Data Null"))
    }


fun <T> MutableLiveData<ViewState<T>>.setLoading() =
    postValue(ViewState.Loading)

fun <T> MutableLiveData<ViewState<T>>.setError(message: String? = null) =
    postValue(ViewState.Failed(message))

