package com.karzek.core.di

import com.karzek.core.http.Endpoints
import dagger.Module
import dagger.Provides
import io.appflate.restmock.RESTMockServer

@Module
object HTTPTestConfigModule {

    @Provides
    fun provideEndpoints(): Endpoints {
        return Endpoints(RESTMockServer.getUrl(), RESTMockServer.getSSLSocketFactory(), RESTMockServer.getTrustManager())
    }
}