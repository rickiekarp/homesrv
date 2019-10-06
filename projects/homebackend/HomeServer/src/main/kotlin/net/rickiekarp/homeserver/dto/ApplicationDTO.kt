package net.rickiekarp.homeserver.dto

class ApplicationDTO {

    var identifier: String? = null
    var version: Int = 0
    var isUpdateEnable: Boolean = false

    override fun toString(): String {
        return "Application{" +
                "identifier='" + identifier + '\''.toString() +
                ", version=" + version +
                ", updateEnable=" + isUpdateEnable +
                '}'.toString()
    }
}
