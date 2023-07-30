package kind.sun.dev.coffeeworld.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.ActivityMainBinding
import kind.sun.dev.coffeeworld.utils.network.NetworkStateManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var networkStateManager: NetworkStateManager
    private var wasConnectedToInternet = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
                if (!wasConnectedToInternet) { showNetworkState("Connected to the Internet") }
            } else {
                showNetworkState("No Internet Connection")
            }
            wasConnectedToInternet = isConnected
        }
    }

    private fun showNetworkState(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).apply {
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.let {
                it.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
            view.layoutParams = (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
                gravity = Gravity.TOP and Gravity.CENTER_HORIZONTAL
                val marginPx : Int = (16 * resources.displayMetrics.density).toInt()
                setMargins(marginPx, marginPx, marginPx, marginPx)
            }
        }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkStateManager.unregisterNetworkReceiver()
    }
}