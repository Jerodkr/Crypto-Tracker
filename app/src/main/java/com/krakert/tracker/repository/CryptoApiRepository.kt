package com.krakert.tracker.repository

import android.util.Log
import com.krakert.tracker.api.CoinGeckoApi
import com.krakert.tracker.api.CoinGeckoApiService
import com.krakert.tracker.api.Resource
import com.krakert.tracker.models.ListCoins
import com.krakert.tracker.models.responses.CoinFullData
import com.krakert.tracker.models.responses.MarketChart
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException

class CryptoApiRepository {
    private val coinGeckoApiService: CoinGeckoApiService = CoinGeckoApi.createApi()

    suspend fun getListCoins(
        currency: String = "usd",
        ids: String? = null,
        order: String = "market_cap_desc",
        perPage: Int = 100,
        page: Int = 1,
    ): Resource<ListCoins> {
        val response = try {
            withTimeout(10_000) {
                coinGeckoApiService.getListCoins(
                    currency = currency,
                    ids = ids,
                    order = order,
                    perPage = perPage,
                    page = page
                )
            }
        } catch (e: HttpException) {
            Log.e("CryptoApiRepository",
                "Retrieval of a list of coins was unsuccessful -> got code: ${e.code()}, details: ${e.message}")
            return Resource.Error(e.code().toString())
        }

        return Resource.Success(response)
    }

    suspend fun getPriceCoins(
        idCoins: String,
        currency: String = "usd",
        marketCap: String = "false",
        dayVol: String = "false",
        dayChange: String = "true",
        lastUpdated: String = "false",
    ): Resource<MutableMap<String, MutableMap<String, Any>>> {
        val response = try {
            withTimeout(10_000) {
                coinGeckoApiService.getPriceByListCoinIds(
                    ids = idCoins,
                    currency = currency,
                    marketCap = marketCap,
                    dayVol = dayVol,
                    dayChange = dayChange,
                    lastUpdated = lastUpdated
                )
            }
        } catch (e: HttpException) {
            Log.e("CryptoApiRepository",
                "Retrieval of latest price of coins was unsuccessful -> got code: ${e.code()}, details: ${e.message}")
            return Resource.Error(e.code().toString())
        }

        return Resource.Success(response)
    }

    suspend fun getDetailsCoinByCoinId(
        coinId: String,
        localization: String = "false",
        tickers: Boolean = false,
        markerData: Boolean = true,
        communityData: Boolean = false,
        developerData: Boolean = false,
        sparkline: Boolean = false,
    ): Resource<CoinFullData> {
        val response = try {
            withTimeout(10_000) {
                coinGeckoApiService.getDetailsCoinByCoinId(
                    id = coinId,
                    localization = localization,
                    tickers = tickers,
                    markerData = markerData,
                    communityData = communityData,
                    developerData = developerData,
                    sparkline = sparkline
                )
            }
        } catch (e: HttpException) {
            Log.e("CryptoApiRepository",
                "Retrieval of details of the coin was unsuccessful -> got code: ${e.code()}, details: ${e.message}")
            return Resource.Error(e.code().toString())
        }
        return Resource.Success(response)
    }

    suspend fun getHistoryByCoinId(
        coinId: String,
        currency: String,
        days: String,
    ): Resource<MarketChart> {
        val response = try {
            withTimeout(10_000) {
                coinGeckoApiService.getHistoryByCoinId(
                    id = coinId,
                    currency = currency,
                    days = days
                )
            }
        } catch (e: HttpException) {
            Log.e("CryptoApiRepository",
                "Retrieval of history data of coin was unsuccessful -> got code: ${e.code()}, details: ${e.message}")
            return Resource.Error(e.code().toString())
        }
        return Resource.Success(response)
    }


    class CoinGeckoExceptionError(message: String) : Exception(message)
}