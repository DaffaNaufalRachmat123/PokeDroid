package com.pokedroid.core

import kotlinx.coroutines.CoroutineDispatcher

interface IDispatchers {

    /**
     * Use it for operations on main thread.
     */
    val Main: CoroutineDispatcher

    /**
     *  Unbounded thread pool.
     */
    val IO: CoroutineDispatcher

    /**
     * Bounded thread pool by the number of CPU cores.
     */
    val Default: CoroutineDispatcher
}