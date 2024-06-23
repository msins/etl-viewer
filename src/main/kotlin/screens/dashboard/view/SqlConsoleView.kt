package screens.dashboard.view

import common.swing.Padding
import common.swing.panel.AbstractPanelContainer
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JScrollPane
import javax.swing.JTextArea

class SqlConsoleView : AbstractPanelContainer() {

    var text: String = ""
        set(value) {
            field = value
            textArea.text = value
        }

    private lateinit var scrollPane: JScrollPane
    private lateinit var textArea: JTextArea

    init {
        initialize()
    }

    override fun configurePanel() {
        panel.layout = BorderLayout()
    }

    override fun initializeComponents() {
        textArea = JTextArea()
        scrollPane = JScrollPane(
            textArea,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        )
    }

    override fun styleViews() {
        panel.border = Padding.Small()
        scrollPane.border = BorderFactory.createEmptyBorder()
        textArea.background = panel.background
    }

    override fun addToPanel() {
        panel.add(scrollPane)
    }
}