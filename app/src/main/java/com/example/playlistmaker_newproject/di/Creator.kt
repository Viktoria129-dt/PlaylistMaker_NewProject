package com.example.playlistmaker_newproject.di

import android.content.Context
import com.example.playlistmaker_newproject.data.NetworkClient
import com.example.playlistmaker_newproject.data.network.RetrofitNetworkClient
import com.example.playlistmaker_newproject.data.network.SearchHistoryRepositoryImpl
import com.example.playlistmaker_newproject.data.network.TrackRepositoryImpl
import com.example.playlistmaker_newproject.domain.api.PlayerInteractor
import com.example.playlistmaker_newproject.domain.api.SearchHistoryInteractor
import com.example.playlistmaker_newproject.domain.api.SearchInteractor
import com.example.playlistmaker_newproject.domain.api.ThemeInteractor
import com.example.playlistmaker_newproject.domain.api.TrackRepository
import com.example.playlistmaker_newproject.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker_newproject.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker_newproject.domain.impl.SearchInteractorImpl
import com.example.playlistmaker_newproject.domain.impl.ThemeInteractorImpl

object Creator {
    private fun provideNetworkClient(): NetworkClient {
        return RetrofitNetworkClient()
    }
    private fun provideTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(provideNetworkClient())
    }
    private fun provideSearchHistoryRepository(context: Context): SearchHistoryRepositoryImpl {
        return SearchHistoryRepositoryImpl(
            context.getSharedPreferences("Songs", Context.MODE_PRIVATE)
        )
    }
    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(provideTrackRepository())
    }
    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository(context))
    }
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }
    fun provideThemeInteractor(context: Context): ThemeInteractor {
        return ThemeInteractorImpl(
            context.getSharedPreferences("app_setting", Context.MODE_PRIVATE)
        )
    }
}