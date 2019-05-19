package net.rickiekarp.foundation.config.redis

import net.rickiekarp.foundation.logger.Log
import net.rickiekarp.foundation.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import java.util.*
import java.util.ArrayList
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Component
class CustomAuthenticationProvider : AuthenticationProvider {

    @Autowired
    private val tokenService: TokenRepository? = null

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication? {
        val userid = authentication.name.toInt()
        val accessToken = authentication.credentials.toString()

        // use the credentials and authenticate against the third-party system
        val user = retrieveUserFromRedis(userid)
        return if (user.isPresent && user.get().token == accessToken) {
            val updatedAuthorities = ArrayList<SimpleGrantedAuthority>()
            val authority = SimpleGrantedAuthority(user.get().role.second)
            updatedAuthorities.add(authority)
            UsernamePasswordAuthenticationToken(userid, accessToken, updatedAuthorities)
        } else {
            Log.DEBUG.debug("UserId[$userid] could not be authenticated!")
            null
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }

    private fun retrieveUserFromRedis(userid: Int): Optional<User> {
        return tokenService!!.findById(userid.toString())
    }
}