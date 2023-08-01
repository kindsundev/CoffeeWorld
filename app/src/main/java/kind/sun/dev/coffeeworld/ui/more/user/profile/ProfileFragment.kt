package kind.sun.dev.coffeeworld.ui.more.user.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import kind.sun.dev.coffeeworld.BuildConfig
import kind.sun.dev.coffeeworld.databinding.FragmentProfileBinding
import kind.sun.dev.coffeeworld.utils.api.TokenManager
import kind.sun.dev.coffeeworld.utils.common.Logger
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = tokenManager.getToken().toString()
        Logger.error(getUserNameFromJWTToken(token).toString())
    }

    private fun getUserNameFromJWTToken(jwtToken: String): String? {
        val secret= BuildConfig.TOKEN_SECRET
        return try {
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(secret.toByteArray())
                .build()
                .parseClaimsJws(jwtToken)
                .body
            claims["username"] as String?
        } catch (e: Exception) {
            Logger.error("Decrypt token: ${e.message}")
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}