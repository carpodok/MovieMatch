package com.example.moviematchapp.di

import android.content.Context
import androidx.room.Room
import com.example.moviematchapp.data.local.datasources.database.MoviesDatabase
import com.example.moviematchapp.data.local.models.SavedMovieEntitiy
import com.example.moviematchapp.data.remote.datasources.MovieService
import com.example.moviematchapp.utils.Constants
import com.example.moviematchapp.utils.connection.NetworkConnectionInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val CONNECT_TIMEOUT = 20L
    private const val READ_TIMEOUT = 60L
    private const val WRITE_TIMEOUT = 120L

    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val headersInterceptor: Interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("accept", "application/json")
            .addHeader(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2M2I4MDAzOWEzNDM2ZWY3YjZkNGNhMmZiNDU3YjEzYyIsInN1YiI6IjY0YzAzMTBmMDE3NTdmMDBhZDc0MWY2YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.xPsO7Bkp9SyLMCBDxQH-PcdjU8XJNbiwzm5cILpY4kg"
            )
            .build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun okHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(headersInterceptor)
            .addInterceptor(NetworkConnectionInterceptor(context))
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()

    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()


    @Provides
    @Singleton
    fun provide(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, MoviesDatabase::class.java, "movies"
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db: MoviesDatabase) = db.noteDoa()

    @Provides
    fun provideEntity() = SavedMovieEntitiy()

}