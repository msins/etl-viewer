package screens.connectserver

import metadata.repository.MetadataRepositoryImpl
import metadata.MetadataService
import screens.connectserver.view.logger.LogItem
import screens.connectserver.model.ConnectServerModel
import screens.connectserver.model.DefaultConnectServerModel
import common.swing.listeners.ValidationState
import screens.connectserver.view.ConnectServerView
import metadata.DatabaseDescriptor
import org.jooq.Constants
import org.jooq.DSLContext
import org.jooq.conf.RenderKeywordCase
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import screens.dashboard.DashboardController
import common.swing.listeners.ExecutionState
import metadata.repository.CachedMetadataRepository
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.sql.Connection
import java.util.concurrent.ExecutionException
import javax.swing.SwingWorker

class ConnectServerController(
    private val view: ConnectServerView,
    private val model: ConnectServerModel
) : ActionListener {

    init {
        view.addConnectClickListener(this)
    }

    companion object {

        fun new(): ConnectServerController {
            val model = DefaultConnectServerModel()
            val view = ConnectServerView(model)
            return ConnectServerController(view, model)
        }
    }

    fun show() {
        view.showItself()
    }

    // =========================================
    // Actions
    // =========================================

    override fun actionPerformed(e: ActionEvent) {
        when (e.actionCommand) {
            "Connect" -> {
                onConnectButtonClicked()
            }
        }
    }

    private fun onConnectButtonClicked() {
        val isUriValid = view.uri.isNotEmpty()

        if (!isUriValid) {
            model.uriValidationState(ValidationState.Error)
            return
        }

        model.uriValidationState(ValidationState.Success)

        model.log(LogItem.info("Connecting to database using jOOQ ${Constants.VERSION} ..."))

        model.connectionState(ExecutionState.STARTED)
        ConnectToDatabaseWorker(view.uri).execute()
    }

    // =========================================
    // Workers
    // =========================================

    /**
     * Tries to establish connection with database for a given URI.
     */
    private inner class ConnectToDatabaseWorker(
        val uri: String
    ) : SwingWorker<Connection, LogItem>() {

        override fun doInBackground(): Connection {
            return model.connect(uri).join()
        }

        override fun done() {
            try {
                val connection = get()
                model.log(LogItem.success("Connection established!"))
                ConnectionMetadataWorker(connection).execute()
            } catch (e: ExecutionException) {
                e.printStackTrace()
                model.log(LogItem.error("Connection failed. Check standard error stream."))
            } catch (e: Exception) {
                e.printStackTrace()
                model.log(LogItem.error("Unknown error."))
            }

            model.connectionState(ExecutionState.ENDED)
        }
    }

    /**
     * Gathers metadata for a given database connection.
     */
    private inner class ConnectionMetadataWorker(
        private val connection: Connection
    ) : SwingWorker<Unit, LogItem>() {

        private val context: DSLContext
        private val databaseProductName: String
        private val databaseProductVersion: String
        private val databaseName: String
        private val databaseDriverName: String
        private val databaseUser: String
        private val service: MetadataService

        init {
            databaseProductName = connection.metaData.databaseProductName
            databaseProductVersion = connection.metaData.databaseProductVersion
            databaseName = connection.catalog
            databaseDriverName = connection.metaData.driverName
            databaseUser = connection.metaData.userName

            context = DSL.using(
                connection,
                Settings().apply {
                    withRenderFormatted(true)
                    renderKeywordCase = RenderKeywordCase.UPPER
                }
            )

            service = MetadataService(
                CachedMetadataRepository(
                    MetadataRepositoryImpl(context)
                )
            )
        }

        override fun doInBackground() {
            publish(LogItem.info("==============================="))
            publish(LogItem.info("Database: '${databaseName}'."))
            publish(LogItem.info("Product: '${databaseProductName}' '${databaseProductVersion}'."))
            publish(LogItem.info("Driver: '${databaseDriverName}'."))
            publish(LogItem.info("User: '${databaseUser}'."))
            publish(LogItem.info("==============================="))
            publish(LogItem.info("Loading facts..."))
            publish(LogItem.info("Facts loaded: '${service.getFacts()}'."))
            publish(LogItem.success("Done."))
        }

        override fun process(chunks: MutableList<LogItem>?) {
            chunks?.forEach {
                model.log(it)
            }
        }

        override fun done() {
            try {
                get()
            } catch (e: Exception) {
                model.log(LogItem.error("${e.message}"))
                model.log(LogItem.info("Error encountered. Read error message and try again."))
                return
            }

            NavigateToDashboardWorker(createDescriptor()).execute()
        }

        private fun createDescriptor(): DatabaseDescriptor {
            return DatabaseDescriptor(
                connection = connection,
                productName = databaseProductName,
                productVersion = databaseProductVersion,
                name = databaseName,
                driver = databaseDriverName,
                user = databaseUser,
                service = service
            )
        }
    }

    /**
     * Provides smoother UX when navigating to main screen with a small delay before closing.
     */
    private inner class NavigateToDashboardWorker(
        val descriptor: DatabaseDescriptor
    ) : SwingWorker<Unit, Void>() {

        override fun doInBackground() {
            Thread.sleep(2000)
        }

        override fun done() {
            view.dispose()
            DashboardController.new(descriptor).show()
        }
    }
}
