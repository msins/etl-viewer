package screens.connectserver.view.logger

import common.swing.panel.AbstractPanelContainer
import common.swing.DoNothingListSelectionModel
import common.swing.extensions.scrollToBottom
import java.awt.BorderLayout
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.JScrollPane

class LoggerContainer : AbstractPanelContainer() {

    init {
        initialize()
    }

    lateinit var model: DefaultListModel<LogItem>

    private lateinit var scrollPane: JScrollPane
    private lateinit var logList: JList<LogItem>

    override fun configurePanel() {
        panel.layout = BorderLayout()
    }

    override fun initializeComponents() {
        model = DefaultListModel()
        logList = JList(model)
        scrollPane = JScrollPane(logList)
    }

    override fun styleViews() {
        logList.apply {
            selectionModel = DoNothingListSelectionModel()
            cellRenderer = LoggerCellRenderer()
            isEnabled = false
        }
    }

    override fun addToPanel() {
        panel.add(scrollPane)
    }

    fun scrollToBottom() {
        logList.scrollToBottom()
    }
}
