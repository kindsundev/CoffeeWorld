package kind.sun.dev.coffeeworld.utils.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NetworkStateManager @Inject constructor(
    private val context: Context,
) : BroadcastReceiver() {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isConnectedLiveData = MutableLiveData<Boolean>()
    val isConnectedLiveData: LiveData<Boolean>
        get() = _isConnectedLiveData

    override fun onReceive(p0: Context?, p1: Intent?) {
        updateConnectionStatus()
    }

    fun registerNetworkReceiver() {
        connectivityManager.registerDefaultNetworkCallback(object: ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { updateConnectionStatus() }

            override fun onLost(network: Network) { updateConnectionStatus() }
        })
        updateConnectionStatus()
    }

    private fun updateConnectionStatus() {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork
        )
        val isConnected =
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        _isConnectedLiveData.postValue(isConnected)
    }

}