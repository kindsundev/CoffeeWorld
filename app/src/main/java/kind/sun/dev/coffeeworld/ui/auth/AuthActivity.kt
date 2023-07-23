package kind.sun.dev.coffeeworld.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.ActivityAuthBinding
import kind.sun.dev.coffeeworld.ui.auth.login.LoginFragment

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        initLoginFragment()
    }


    private fun initLoginFragment() {
        val loginFragment = LoginFragment()
        supportFragmentManager.commit {
            replace(R.id.fragment_container, loginFragment)
        }
    }

}