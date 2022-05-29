package com.krakert.tracker.state

import com.krakert.tracker.model.Coins
import com.krakert.tracker.model.Favorite
import com.krakert.tracker.model.MarketChart

sealed class ViewStateAddCoin {
    // Represents different states for the ListCoins screen
    object Empty : ViewStateAddCoin()
    object Loading : ViewStateAddCoin()
    data class Success(val coins: Coins) : ViewStateAddCoin()
    data class Error(val exception: Throwable) : ViewStateAddCoin()
}

sealed class ViewStateOverview {
    // Represents different states for the ListCoins screen
    object Empty : ViewStateOverview()
    object Loading : ViewStateOverview()
    data class Success(val favorite: Favorite) : ViewStateOverview()
    data class Error(val exception: Throwable) : ViewStateOverview()
}

sealed class ViewStateData {
    object Loading : ViewStateOverview()
    data class Success(val marketChart: MarketChart) : ViewStateOverview()
    data class Error(val exception: Throwable) : ViewStateOverview()
}