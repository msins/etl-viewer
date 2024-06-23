package screens.connectserver.view

import common.theme.Margins
import screens.connectserver.model.listeners.UriValidationListener
import common.swing.frame.AbstractFrameContainer
import common.swing.panel.AbstractPanelContainer
import screens.connectserver.view.logger.LogItem
import screens.connectserver.view.logger.LoggerContainer
import screens.connectserver.model.ConnectServerModel
import common.swing.Padding
import common.swing.extensions.isErrorHintShown
import common.swing.extensions.setPlaceholder
import common.swing.listeners.ExecutionListener
import common.swing.listeners.ExecutionState
import common.swing.listeners.ValidationState
import common.theme.Sizes
import screens.connectserver.model.listeners.LoggerListener
import java.awt.BorderLayout
import java.awt.BorderLayout.*
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.WindowConstants.DISPOSE_ON_CLOSE

class ConnectServerView(
    model: ConnectServerModel
) : AbstractFrameContainer() {

    init {
        initialize()
        model.addLoggerListener(LoggerListenerImpl())
        model.addConnectionListener(ConnectionListenerImpl())
        model.addUriValidationListener(UriValidationListenerImpl())
    }

    val uri: String
        get() = formContainer.uri

    private lateinit var formContainer: FormContainer
    private lateinit var loggerContainer: LoggerContainer

    override fun configureFrame() {
        frame.apply {
            title = "Connect to database"
            defaultCloseOperation = DISPOSE_ON_CLOSE
            size = Sizes.Dimensions.connectServerWindow
            setLocationRelativeTo(null)
            isResizable = true
        }
    }

    override fun initializeComponents() {
        formContainer = FormContainer()
        loggerContainer = LoggerContainer()
    }

    override fun styleViews() {
    }

    override fun addToFrame() {
        frame.add(ContentContainer().asPanel())
    }

    override fun showItself() {
        frame.isVisible = true
    }

    // =========================================
    // Containers
    // =========================================

    private inner class ContentContainer : AbstractPanelContainer() {

        init {
            initialize()
        }

        private lateinit var formPanel: JPanel
        private lateinit var loggerPanel: JPanel

        override fun configurePanel() {
            panel.layout = BorderLayout()
        }

        override fun initializeComponents() {
            formPanel = formContainer.asPanel()
            loggerPanel = loggerContainer.asPanel()
        }

        override fun styleViews() {
            loggerPanel.border = Padding.Medium(top = 0)
        }

        override fun addToPanel() {
            panel.apply {
                add(formPanel, NORTH)
                add(loggerPanel, CENTER)
            }
        }
    }


    private inner class FormContainer : AbstractPanelContainer() {

        init {
            initialize()
        }

        val uri: String
            get() = databaseUriTextField.text

        lateinit var databaseUriTextField: JTextField
        lateinit var connectButton: JButton
        lateinit var databaseUrlLabel: JLabel

        override fun configurePanel() {
            panel.layout = BorderLayout(Margins.SMALL, 0)
        }

        override fun initializeComponents() {
            databaseUrlLabel = JLabel()
            databaseUriTextField = JTextField()
            connectButton = JButton()
        }

        override fun styleViews() {
            panel.border = Padding.Medium()
            databaseUrlLabel.text = "Database URL:"
            connectButton.text = "Connect"
            databaseUriTextField.apply {
                setPlaceholder("ex. jdbc:sqlserver://localhost:1433;databaseName=Test")
                text = """
                jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;
                databaseName=AdvWorks3DZ2023;user=test123;password=test123
                """.trimIndent()
            }
        }

        override fun addToPanel() {
            panel.apply {
                add(databaseUrlLabel, LINE_START)
                add(databaseUriTextField, CENTER)
                add(connectButton, LINE_END)
            }
        }

        fun setEnabled(isEnabled: Boolean) {
            databaseUriTextField.isEnabled = isEnabled
            connectButton.isEnabled = isEnabled
        }
    }

    // =========================================
    // Listeners
    // =========================================

    fun addConnectClickListener(listener: ActionListener) {
        formContainer.connectButton.addActionListener(listener)
    }


    inner class ConnectionListenerImpl : ExecutionListener {

        override fun onExecutionStateChanged(state: ExecutionState) {
            when (state) {
                ExecutionState.STARTED -> {
                    formContainer.setEnabled(false)
                }

                ExecutionState.ENDED -> {
                    formContainer.setEnabled(true)
                }
            }
        }
    }


    inner class LoggerListenerImpl : LoggerListener {

        override fun onLogAdded(item: LogItem) {
            loggerContainer.model.addElement(item)
            loggerContainer.scrollToBottom()
        }
    }


    inner class UriValidationListenerImpl : UriValidationListener {

        override fun onValidationStateChanged(state: ValidationState) {
            when (state) {
                is ValidationState.Error -> {
                    formContainer.databaseUriTextField.isErrorHintShown(true)
                }

                is ValidationState.Success -> {
                    formContainer.databaseUriTextField.isErrorHintShown(false)
                }
            }
        }
    }
}
