package net.rickiekarp.foundation.model

import org.springframework.data.redis.core.RedisHash
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

@RedisHash("token")
class User : Credentials {
    var id: Int = 0
    var token: String? = null
    var accountType: Byte? = null
    var isAccountEnabled: Boolean = false
    lateinit var role: kotlin.Pair<Int, String>

    constructor() {
        //empty
    }

    constructor(username: String, password: String) : super() {
        this.username = username
        this.password = password
    }

//    @JsonIgnore
    fun getName(): String? {
        return username
    }

    fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities = ArrayList<SimpleGrantedAuthority>()
//        for (role in this.getUserRoles()) {
//            authorities.add(SimpleGrantedAuthority("ROLE_" + role.getRole()))
//        }
        authorities.add(SimpleGrantedAuthority("ROLE_ADMIN"))
        return authorities
    }

    override fun toString(): String {
        return "User(id=$id, token='$token', role=$role)"
    }
}