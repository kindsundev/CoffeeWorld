package kind.sun.dev.coffeeworld.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.ActivityAuthBinding
import kind.sun.dev.coffeeworld.ui.auth.login.LoginFragment
import kind.sun.dev.coffeeworld.utils.api.NetworkStateManager
import kind.sun.dev.coffeeworld.utils.common.Logger
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    @Inject lateinit var networkStateManager: NetworkStateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        registerNetworkState()
        initLoginFragment()
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

    private fun initLoginFragment() {
        val loginFragment = LoginFragment()
        supportFragmentManager.commit {
            replace(R.id.fragment_container, loginFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkStateManager.unregisterNetworkReceiver()
    }
}