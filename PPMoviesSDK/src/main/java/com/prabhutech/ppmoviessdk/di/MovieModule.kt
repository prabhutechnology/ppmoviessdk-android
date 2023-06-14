package com.prabhutech.ppmoviessdk.di

import com.prabhutech.ppmoviessdk.model.remote.MoviesApi
import com.prabhutech.ppmoviessdk.model.repository.MoviesRepository
import com.prabhutech.ppmoviessdk.model.repository.MoviesRepositoryImpl
import com.prabhutech.ppmoviessdk.core.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object MovieModule {
    @Provides
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi = retrofit.create(MoviesApi::class.java)

    @Provides
    fun provideMoviesRepository(
        api: MoviesApi,
        dispatcherProvider: DispatcherProvider
    ): MoviesRepository = MoviesRepositoryImpl(api, dispatcherProvider)
}