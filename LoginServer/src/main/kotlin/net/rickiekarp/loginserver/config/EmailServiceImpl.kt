package net.rickiekarp.loginserver.config

import freemarker.template.Configuration
import net.rickiekarp.loginserver.dto.EmailDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import java.util.HashMap

@Component
class EmailServiceImpl {

    @Autowired
    var sender: JavaMailSender? = null

    @Value("\${spring.mail.username}")
    private val smtpEmail: String? = null

    @Autowired
    private val freemarkerConfig: Configuration? = null

    fun sendMail(email: EmailDto) {
        val message = SimpleMailMessage()
        message.setTo(email.to)
        message.subject = email.subject
        message.text = email.message
        sender!!.send(message)
    }

    @Throws(Exception::class)
    fun sendInfoMail(email: EmailDto, jobIdentifier: String) {
        val message = sender!!.createMimeMessage()
        val helper = MimeMessageHelper(message)

        val additionalData = email.additionalData as LinkedHashMap<*, *>

        val model = HashMap<String, String>()
        model["message"] = email.message
        model["job"] = jobIdentifier
        model["date"] = additionalData["backupDate"] as String
        model["backupPath"] = additionalData["backupPath"] as String
        model["backupFile"] = additionalData["backupFile"] as String

        // set loading location to src/main/resources
        freemarkerConfig!!.setClassForTemplateLoading(this.javaClass, "/templates/")

        val template = freemarkerConfig.getTemplate("mail/cronjob.ftl")
        val templateContentText = FreeMarkerTemplateUtils.processTemplateIntoString(template, model)

        helper.setFrom(smtpEmail!!)
        helper.setTo(email.to)
        helper.setSubject(email.subject)
        helper.setText(templateContentText, true) // set to html

        sender!!.send(message)
    }

}
