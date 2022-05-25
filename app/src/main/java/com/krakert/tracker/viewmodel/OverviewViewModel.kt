package com.krakert.tracker.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krakert.tracker.repository.CoinGeckoRepository
import com.krakert.tracker.state.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class OverviewViewModel: ViewModel() {
    private var geckoRepo: CoinGeckoRepository = CoinGeckoRepository()

    init {
        getFavoriteCoins()
    }

    // backing property to avoid state updates from other classes
    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)

    // the UI collects from this StateFlow to get it's state update
    val favoriteCoins = _viewState.asStateFlow()

    fun getFavoriteCoins() = viewModelScope.launch(Dispatchers.IO) {
        geckoRepo.getFavoriteCoins().collect { result ->
            try {
                if (result.Coins?.size == null) {
                    _viewState.value = ViewState.Empty
                } else {
                    _viewState.value = ViewState.Success(result)
                }
            } catch (e: CoinGeckoRepository.ListCoinsRetrievalError) {
                val errorMsg = "Something went wrong while retrieving quest"

                Log.e(ContentValues.TAG, e.message ?: errorMsg)
                _viewState.value = ViewState.Error(e)
            }
        }
    }
}
