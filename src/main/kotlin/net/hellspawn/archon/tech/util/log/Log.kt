package net.hellspawn.archon.tech.util.log

import net.hellspawn.archon.tech.Archontech.MOD_ID
import org.slf4j.LoggerFactory

object Log {
    private val logger = LoggerFactory.getLogger("MOD_ID")

    public fun log(level: LogLevel, message: String) {
        when (level) {
            LogLevel.INFO -> logger.info("[${MOD_ID.uppercase()}] $message")
            LogLevel.WARN -> logger.warn("[${MOD_ID.uppercase()}] $message")
            LogLevel.ERROR -> logger.error("[${MOD_ID.uppercase()}] $message")
            LogLevel.DEBUG -> logger.debug("[${MOD_ID.uppercase()}] $message")
            LogLevel.TRACE -> logger.trace("[${MOD_ID.uppercase()}] $message")
        }
    }

    public fun log(level: LogLevel, tag: String, message: String) {
        log(level, "[$tag] $message")
    }
}