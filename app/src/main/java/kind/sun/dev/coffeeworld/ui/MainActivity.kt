package kind.sun.dev.coffeeworld.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.ActivityMainBinding
import kind.sun.dev.coffeeworld.utils.api.NetworkStateManager
import kind.sun.dev.coffeeworld.utils.common.Logger
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var networkStateManager: NetworkStateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        registerNetworkState()
        setupBottomNav()
    }

    private fun registerNetworkState() {
        networkStateManager.registerNetworkReceiver()
        networkStateManager.isConnectedLiveData.observe(this) { isConnected ->
            if (isConnected) {
                Toast.makeText(this, "Connected to the Internet", Toast.LENGTH_SHORT).show()
                Logger.error("Connected to the Internet")
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
                Logger.error("No Internet Connection")
            }
        }
    }

    private fun setupBottomNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_view)
        navHostFragment?.let {
            val navController = it.findNavController()
            binding.bottomNavView.setupWithNavController(navController)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkStateManager.unregisterNetworkReceiver()
    }
}