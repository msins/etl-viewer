package screens.connectserver.model

import common.swing.listeners.ExecutionListener
import common.swing.listeners.ExecutionState
import common.swing.listeners.ValidationState
import screens.connectserver.view.logger.LogItem
import screens.connectserver.model.listeners.LoggerListener
import screens.connectserver.model.listeners.UriValidationListener
import java.sql.Connection
import java.util.concurrent.CompletableFuture

interface ConnectServerModel {

    fun addLoggerListener(listener: LoggerListener)
    fun log(item: LogItem)

    fun addUriValidationListener(listener: UriValidationListener)
    fun uriValidationState(state: ValidationState)

    fun addConnectionListener(listener: ExecutionListener)
    fun connectionState(state: ExecutionState)

    fun connect(uri: String): CompletableFuture<Connection>
}