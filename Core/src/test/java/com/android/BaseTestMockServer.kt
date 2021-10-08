package com.android

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import java.io.File

abstract class BaseTestMockServer : AutoCloseKoinTest() {

    protected lateinit var mockServer: MockWebServer

    @Before
    open fun setup() {
        mockServer = MockWebServer()
        mockServer.start()
        startKoin {
//            modules(createRemoteModule(mockServer.url("/").toString()))
            setupModules(this)
        }
    }

    abstract fun setupModules(koinApplication: KoinApplication)

    @After
    open fun tearDown() {
        mockServer.shutdown()
    }

    fun getMockResponse(mockServer : MockWebServer , fileName : String , responseCode : Int) : MockResponse {
        return MockResponse()
            .setResponseCode(responseCode)
            .setBody(getJson(fileName))
            .addHeader("Accept" , "application/json")
    }

    private fun getJson(path: String): String {
        val uri = this.javaClass.classLoader!!.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

}