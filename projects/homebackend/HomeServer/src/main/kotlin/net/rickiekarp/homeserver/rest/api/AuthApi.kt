package net.rickiekarp.homeserver.rest.api

import net.rickiekarp.foundation.config.BaseConfig
import net.rickiekarp.foundation.parser.PropertiesParser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("auth")
class AuthApi {

    /**
     * Checks whether the user is allowed to execute the requested plugin
     * @param pluginIdentifier Plugin to check
     * @return True if user is allowed, false otherwise
     */
    @RequestMapping(value = ["validate"], method = [RequestMethod.POST])
    fun validatePlugin(pluginIdentifier: String): ResponseEntity<String> {
        val result = BaseConfig.get().application().getProperty("$pluginIdentifier.enable")
        return ResponseEntity(result, HttpStatus.OK)
    }

    @RequestMapping(
            value = ["validateProperties"],
            method = [RequestMethod.GET]
    )
    fun validateProperties(pluginIdentifier: String): ResponseEntity<HashMap<String, HashMap<Any, Any>>> {
        val filteredProperties = PropertiesParser.filterPropertiesAndReplaceIdentifier(BaseConfig.get().application(), pluginIdentifier)
        val list = PropertiesParser.getPropertyMapWithIdentifier(filteredProperties, pluginIdentifier)
        return ResponseEntity(list, HttpStatus.OK)
    }
}
