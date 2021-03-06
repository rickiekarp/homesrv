package net.rickiekarp.foundation.logger

import net.rickiekarp.foundation.config.BaseConfig
import org.apache.logging.log4j.core.Logger
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder

/**
 * Utility class to collect general-purpose loggers under the log.* namespace
 */
class Log private constructor() {
    init {
        throw UnsupportedOperationException("utility class should not be instantiated")
    }

    companion object {
        lateinit var DEBUG: Logger

        @JvmStatic
        fun setupLogging() {
            // Get context instance
            val context = LoggerContext("LoggerContext")
            val debugConfig = createConfiguration("debug", BaseConfig.get().foundation().getProperty("LogLevelDebug"))
            DEBUG = context.getLogger("debug")
            context.start(debugConfig)
        }

        private fun createConfiguration(loggerName: String, logLevel: String): Configuration {
            val builder = CustomLoggerConfigurationFactory().createConfiguration(loggerName, logLevel, DefaultConfigurationBuilder())
            return builder.build()
        }
    }
}
