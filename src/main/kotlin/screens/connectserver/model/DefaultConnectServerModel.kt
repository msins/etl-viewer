package screens.connectserver.model

import common.model.AbstractModel
import common.swing.listeners.ExecutionListener
import common.swing.listeners.ExecutionState
import common.swing.listeners.ValidationState
import screens.connectserver.view.logger.LogItem
import screens.connectserver.model.listeners.LoggerListener
import screens.connectserver.model.listeners.UriValidationListener
import java.sql.Connection
import java.sql.DriverManager
import java.util.concurrent.CompletableFuture

class DefaultConnectServerModel : AbstractModel(), ConnectServerModel {

    override fun addLoggerListener(listener: LoggerListener) {
        add(listener)
    }

    override fun log(item: LogItem) {
        fire<LoggerListener> {
            it.onLogAdded(item)
        }
    }


    override fun addUriValidationListener(listener: UriValidationListener) {
        add(listener)
    }

    override fun uriValidationState(state: ValidationState) {
        fire<UriValidationListener> {
            it.onValidationStateChanged(state)
        }
    }


    override fun addConnectionListener(listener: ExecutionListener) {
        add(listener)
    }

    override fun connectionState(state: ExecutionState) {
        fire<ExecutionListener> {
            it.onExecutionStateChanged(state)
        }
    }

    override fun connect(uri: String): CompletableFuture<Connection> {
        return CompletableFuture.supplyAsync {
            DriverManager.getConnection(uri)
        }
    }
}