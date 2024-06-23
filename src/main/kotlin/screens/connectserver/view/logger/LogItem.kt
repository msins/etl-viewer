package screens.connectserver.view.logger

import common.theme.Theme
import java.awt.Color
import java.time.LocalTime

sealed class LogItem(
    val text: String,
    val color: Color,
    val time: LocalTime = LocalTime.now()
) {

    class Info(text: String) : LogItem(text, Theme.onColorPrimary)
    class Success(text: String) : LogItem(text, Theme.success)
    class Error(text: String) : LogItem(text, Theme.error)

    companion object {

        fun info(text: String): LogItem {
            return Info(text)
        }

        fun error(text: String): LogItem {
            return Error(text)
        }

        fun success(text: String): LogItem {
            return Success(text)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LogItem) return false

        if (time != other.time) return false
        return text == other.text
    }

    override fun hashCode(): Int {
        var result = time.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }
}
