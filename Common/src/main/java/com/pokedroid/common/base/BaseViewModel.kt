package com.pokedroid.common.base

import androidx.lifecycle.*
import com.github.ajalt.timberkt.i
import com.github.ajalt.timberkt.w
import com.pokedroid.common.extension.errorMessage
import com.pokedroid.common.extension.runOnUi
import com.pokedroid.core.AppDispatchers
import com.pokedroid.core.local.DaoProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseViewModel : ViewModel(), KoinComponent {

    var cities: List<String> = listOf()
    private val disposables = CompositeDisposable()
    val daoProvider by inject<DaoProvider>()

    fun launchDisposable(job: () -> Disposable) {
        disposables.add(job())
    }

    fun dispose() {
        disposables.clear()
    }

    internal fun <T> launchOnViewModelScope(block: suspend () -> LiveData<T>): LiveData<T> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emitSource(block())
        }
    }

    fun launchOnUi(
        onRequest: suspend CoroutineScope.() -> Unit,
        onError: (String) -> Unit,
        onFinally: suspend CoroutineScope.() -> Unit = {},
        handleCancellationExceptionManually: Boolean = true
    ): Job {
        return runOnUi {
            tryCatch(onRequest, onError, onFinally, handleCancellationExceptionManually)
        }
    }

    fun launchOnUi(
        dispatcher: AppDispatchers,
        onRequest: suspend CoroutineScope.() -> Unit,
        onError: (String) -> Unit,
        onFinally: suspend CoroutineScope.() -> Unit = {},
        handleCancellationExceptionManually: Boolean = true
    ): Job {
        return runOnUi(dispatcher = dispatcher) {
            tryCatch(onRequest, onError, onFinally, handleCancellationExceptionManually)
        }
    }


    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: (String) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Exception) {
                if (e !is CancellationException || handleCancellationExceptionManually) {
                    val error = e.errorMessage()
                    catchBlock(error)
                } else {
                    w { "Close exception" }
                }
            } finally {
                finallyBlock()
            }
        }
    }


    override fun onCleared() {
        disposables.clear()
        super.onCleared()
        i { "ViewModel Cleared" }
    }
}