package ir.bamap.blu.adapter.keycloak.util

import org.slf4j.spi.LoggingEventBuilder

class LogUtil {
    companion object {
        fun init(
            builder: LoggingEventBuilder,
            message: String?,
            arguments: MutableMap<String?, Any?>
        ): LoggingEventBuilder {
            builder.setMessage(message)
            arguments.forEach { (s: String?, o: Any?) -> builder.addKeyValue(s, o) }

            return builder
        }

        fun init(builder: LoggingEventBuilder, message: String?, vararg arguments: Any?): LoggingEventBuilder {
            val mapArguments = HashMap<String?, Any?>()
            var i = 0
            while (i < arguments.size) {
                val key: String? = if (arguments[i] != null) arguments[i].toString() else "null"
                val value = if (arguments.size >= i + 2) arguments[i + 1] else null
                mapArguments[key] = value
                i += 2
            }

            return init(builder, message, mapArguments)
        }

        fun log(builder: LoggingEventBuilder, message: String?, arguments: MutableMap<String?, Any?>) {
            init(builder, message, arguments).log()
        }
    }
}