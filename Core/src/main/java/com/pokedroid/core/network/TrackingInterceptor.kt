package com.pokedroid.core.network

//import com.google.firebase.perf.FirebasePerformance
//import com.google.firebase.perf.metrics.HttpMetric
//import okhttp3.HttpUrl
//import okhttp3.Interceptor
//import okhttp3.Request
//import okhttp3.Response
//
//
//class TrackingInterceptor : Interceptor {
//
//    private var metric: HttpMetric? = null
////    val trace = FirebasePerformance.getInstance().newTrace("_metricInterceptor_interceptTraceV2")
//
//    //    @AddTrace(name = "_metricInterceptor_interceptTrace", enabled = true)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
////        trace.start()
//        startTracking(request)
//        val response = chain.proceed(request)
//        stopMetricts(response.code)
////        trace.stop()
////        trace.putAttribute("request_method", request.method)
////        trace.putAttribute("request_url", request.url.toString())
////        trace.putAttribute("response_successful", response.isSuccessful.toString())
////        trace.putMetric("response_code", response.code.toLong())
////        trace.putMetric("response_delta", response.receivedResponseAtMillis - response.sentRequestAtMillis)
//
//        return response
//    }
//
//    private fun startTracking(request: Request) {
//        val (url: HttpUrl, method: String) = request
//        metric = FirebasePerformance.getInstance().newHttpMetric(
//                url.toString(),
//                method
//        )
//        metric?.setRequestPayloadSize(request.body?.contentLength() ?: 0)
//        metric?.start()
//    }
//
//    private fun stopMetricts(responseCode: Int) {
//        metric?.setHttpResponseCode(responseCode)
//        metric?.stop()
//    }
//
//
//}
//
//private operator fun Request.component2(): String = method
//private operator fun Request.component1(): HttpUrl = url