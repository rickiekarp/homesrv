package net.rickiekarp.loginserver.rest.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.rickiekarp.foundation.core.services.mail.EmailService
import net.rickiekarp.foundation.data.dao.ApplicationSettingsDao
import net.rickiekarp.foundation.data.dto.EmailDto
import net.rickiekarp.foundation.data.dto.ResultDTO
import net.rickiekarp.foundation.logger.Log
import net.rickiekarp.foundation.model.NotificationTokenData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("notify")
class NotificationApi {

    @Autowired
    var service: EmailService? = null

    @Autowired
    var repo: ApplicationSettingsDao? = null

    @PostMapping(value = ["send"])
    fun sendMailContent(@RequestHeader(name = "X-Notification-Token") notificationToken: String, @RequestBody mail: EmailDto): ResponseEntity<ResultDTO> {
        val setting = repo!!.getApplicationSettingByIdentifier("notificationtoken")
        if (setting != null) {
            val notificationList: List<NotificationTokenData> = jacksonObjectMapper().readValue(setting.content!!)
            val notificationData = service!!.findNotificationData(notificationToken, notificationList)
            if (notificationData != null) {
                service!!.sendInfoMail(mail, notificationData)
                return ResponseEntity(ResultDTO("success"), HttpStatus.OK)
            } else {
                Log.DEBUG.info("Mail was not sent! Invalid notification token: $notificationToken")
            }
        }

        return ResponseEntity(ResultDTO("not_sent"), HttpStatus.OK)
    }

    @PostMapping("sendWithAttachment")
    fun send(
            @RequestHeader(name = "X-Notification-Token") notificationToken: String,
            @RequestHeader(name = "X-Recipient") recipient: String,
            @RequestHeader(name = "X-Subject") subject: String,
            @RequestParam("file") uploadfile: MultipartFile): ResponseEntity<*> {
        val setting = repo!!.getApplicationSettingByIdentifier("notificationtoken")
        if (setting != null) {
            val notificationList: List<NotificationTokenData> = jacksonObjectMapper().readValue(setting.content!!)
            val notificationData = service!!.findNotificationData(notificationToken, notificationList)
            if (notificationData != null) {
                val mailDto = EmailDto()
                mailDto.to = recipient
                mailDto.subject = subject
                service!!.sendFromByteArray(mailDto, uploadfile.bytes)
                return ResponseEntity(ResultDTO("success"), HttpStatus.OK)
            } else {
                Log.DEBUG.info("Mail was not sent! Invalid notification token: $notificationToken")
            }
        }

        return ResponseEntity(ResultDTO("not_sent"), HttpStatus.OK)
    }
}
