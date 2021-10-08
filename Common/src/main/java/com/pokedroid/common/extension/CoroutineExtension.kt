package com.pokedroid.common.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.w
import com.pokedroid.core.AppDispatchers
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.coroutineContext

infix fun <T> Deferred<T>.then(block: (T) -> Unit): Job {
    return GlobalScope.launch(Dispatchers.Main) {
        block(this@then.await())
    }
}

infix fun <T, R> Deferred<T>.thenAsync(block: (T) -> R): Deferred<R> {

    return GlobalScope.async(context = Dispatchers.Main) {
        block(this@thenAsync.await())
    }
}

/*How to use this ?
* this retryIo need [Coroutines] scope because it's suspend function, if it to work on MainThread don't forget using ui Dispatchers
* simple yet powerful
*  uiScope.launch {
            retryIO {
                liveDataState.value = OnGetData(retryIO { api.getWhatEver() })
                liveDataState.value = OnSuccessGetData(true)
            }
        }
* */
suspend fun <T> retryIO(
        times: Int = Int.MAX_VALUE,
        initialDelay: Long = 1000, // 1 second
        maxDelay: Long = 4000,    // 4 seconds
        factor: Double = 2.0,
        desc: String = "<Missing Description>",
        block: suspend () -> T?
): T? {
    var currentDelay = initialDelay
    repeat(times - 1) {
        if (!coroutineContext.isActive) throw Exception("Job canceled when trying to execute retryIO")

        try {
            return block()
        } catch (e: IOException) {
            w { "Description (IOException): $desc, fail counter: ${it + 1}, Exception: ${e.message} " }
        } catch (e: HttpException) {
            w { "Description (HttpException): $desc, fail counter: ${it + 1}, Exception: ${e.message} " }
        }

        if (!coroutineContext.isActive) throw Exception("Job canceled when trying to execute retryIO")
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    if (!coroutineContext.isActive) throw Exception("Job canceled when trying to execute retryIO")

    return block() // last attempt
}


suspend fun <T> Deferred<T>.awaitOrNull(timeout: Long = 0L): T? {
    val result = kotlin.runCatching {
        if (timeout > 0) {

            withTimeout(timeout) {

                this@awaitOrNull.await()
            }

        } else {

            this.await()
        }
    }
    return if (result.isSuccess) {
        result.getOrNull()
    } else {
        null
    }
}

inline fun ViewModel.runOnUi(crossinline block: suspend (CoroutineScope.() -> Unit)): Job {
    return viewModelScope.launch(Dispatchers.Main) {
        block()
    }
}

inline fun ViewModel.runOnUi(dispatcher: AppDispatchers, crossinline block: suspend (CoroutineScope.() -> Unit)): Job {
    return viewModelScope.launch(dispatcher.main) {
        block()
    }
}

/*This serial of function is used for concurrent call*/
fun <A, B> computeDoubleResult(await1: A, await2: B): Pair<A, B> {
    return Pair(await1, await2)
}

fun <A, M, I> computeTripleResult(await1: A, await2: M, await3: I): Triple<A, M, I> {
    return Triple(await1, await2, await3)
}

fun <N, O, V, I> computeQuadResult(await1: N, await2: O, await3: V, await4: I): Pair<Pair<N, O>, Pair<V, I>> {
    return Pair(Pair(await1, await2), Pair(await3, await4))
}

fun <L, Y, N, D, A> computeQuintResult(await1: L, await2: Y, await3: N, await4: D, await5: A): Pair<Triple<L, Y, N>, Pair<D, A>> {
    return Pair(Triple(await1, await2, await3), Pair(await4, await5))
}

fun <R, O, D, I, T, H> computeSixResult(await1: R, await2: O, await3: D, await4: I, await5: T, await6: H): Pair<Triple<R, O, D>, Triple<I, T, H>> {
    return Pair(Triple(await1, await2, await3), Triple(await4, await5, await6))
}