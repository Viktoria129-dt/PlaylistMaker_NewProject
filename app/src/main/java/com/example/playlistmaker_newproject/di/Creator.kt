package com.example.playlistmaker_newproject.di

import android.content.Context
import com.example.playlistmaker_newproject.data.NetworkClient
import com.example.playlistmaker_newproject.data.network.PlayerRepositoryImpl
import com.example.playlistmaker_newproject.data.network.RetrofitNetworkClient
import com.example.playlistmaker_newproject.data.network.SearchHistoryRepositoryImpl
import com.example.playlistmaker_newproject.data.network.ThemeRepositoryImpl
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
    private fun getSharedPreferences(context: Context) =
        context.getSharedPreferences("app_setting", Context.MODE_PRIVATE)

    fun provideSearchInteractor(context: Context): SearchInteractorImpl {
        val networkClient = RetrofitNetworkClient()
        val trackRepository = TrackRepositoryImpl(networkClient)
        val searchHistoryRepository = SearchHistoryRepositoryImpl(getSharedPreferences(context))
        return SearchInteractorImpl(trackRepository)
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractorImpl {
        val searchHistoryRepository = SearchHistoryRepositoryImpl(getSharedPreferences(context))
        return SearchHistoryInteractorImpl(searchHistoryRepository)
    }

    fun provideThemeInteractor(context: Context): ThemeInteractorImpl {
        return ThemeInteractorImpl(provideThemeRepository(context))
    }

    private fun provideThemeRepository(context: Context): ThemeRepositoryImpl {
        return ThemeRepositoryImpl(getSharedPreferences(context))
    }


    fun providePlayerInteractor(): PlayerInteractorImpl {
        val playerRepository = PlayerRepositoryImpl()
        return PlayerInteractorImpl(playerRepository)
    }
}