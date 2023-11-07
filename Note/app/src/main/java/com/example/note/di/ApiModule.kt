package com.example.note.di

import android.content.Context
import com.example.note.connectivity.NetworkObserverImpl
import com.example.note.data.remote.NoteApi
import com.example.note.data.repository.NetworkRepositoryImpl
import com.example.note.domain.repository.NetworkRepository
import com.example.note.utils.Constants.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.JavaNetCookieJar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.net.CookieManager
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideCookieManager(): CookieManager = CookieManager()


    @Provides
    @Singleton
    fun provideHttpClient(cookieManager: CookieManager): OkHttpClient =
        OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .cookieJar(JavaNetCookieJar(cookieManager))
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteApi(retrofit: Retrofit): NoteApi = retrofit.create(NoteApi::class.java)

    @Provides
    @Singleton
    fun provideNetworkRepository(noteApi: NoteApi): NetworkRepository =
        NetworkRepositoryImpl(noteApi = noteApi)

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(@ApplicationContext context: Context) =
        NetworkObserverImpl(context = context)
}