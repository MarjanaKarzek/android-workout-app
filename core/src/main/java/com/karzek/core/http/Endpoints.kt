package com.karzek.core.http

import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

data class Endpoints(
    val workoutsBaseUrl: String,
    val customSocketFactory: SSLSocketFactory? = null,
    val customTrustManager: X509TrustManager? = null
)