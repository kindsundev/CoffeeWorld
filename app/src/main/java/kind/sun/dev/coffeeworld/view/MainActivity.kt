package kind.sun.dev.coffeeworld.view

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.ActivityMainBinding
import kind.sun.dev.coffeeworld.utils.helper.network.NetworkReceiverHelper
import kind.sun.dev.coffeeworld.utils.helper.view.showSnackbarMessage
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var networkStateManager: NetworkReceiverHelper
    private var wasConnectedToInternet = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply { setKeepOnScreenCondition { false } }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setupBottomNavController()
    }

    private fun setupBottomNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_view)
        navHostFragment?.let {
            val navController = it.findNavController()
            setVisibilityForMainFragments(navController)
            binding.bottomNavView.setupWithNavController(navController)
        }
    }

    private fun setVisibilityForMainFragments(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.cafeFragment, R.id.orderFragment,
                R.id.cartFragment, R.id.moreFragment -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNavView.visibility = View.GONE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerNetworkState()
    }

    private fun registerNetworkState() {
        networkStateManager.registerNetworkReceiver()
        networkStateManager.isConnectedLiveData.observe(this) { isConnected ->
            if (isConnected) {
                if (!wasConnectedToInternet) showNetworkState(R.string.connected_internet)
            } else {
                showNetworkState(R.string.no_internet_connected)
            }
            wasConnectedToInternet = isConnected
        }
    }

    private fun showNetworkState(resMessageId: Int) {
        showSnackbarMessage(this, binding.root as CoordinatorLayout, resMessageId)
    }

}