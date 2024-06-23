package screens.connectserver.model.listeners

import screens.connectserver.view.logger.LogItem
import java.util.EventListener

interface LoggerListener : EventListener {

    fun onLogAdded(item: LogItem)
}