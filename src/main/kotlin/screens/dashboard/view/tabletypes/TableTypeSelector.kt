package screens.dashboard.view.tabletypes

import com.formdev.flatlaf.FlatClientProperties
import common.swing.components.LeadingTitle
import common.swing.Padding
import common.swing.panel.AbstractPanelContainer
import java.awt.BorderLayout
import javax.swing.*
import javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
import javax.swing.ListSelectionModel.SINGLE_SELECTION

class TableTypeSelector<T>(val title: String): AbstractPanelContainer() {

    init {
        initialize()
    }

    private lateinit var titleView: LeadingTitle
    private lateinit var listScrollPane: JScrollPane
    lateinit var list: JList<T>
    lateinit var model: DefaultListModel<T>

    override fun configurePanel() {
        panel.layout = BorderLayout()
    }

    override fun initializeComponents() {
        titleView = LeadingTitle()
        model = DefaultListModel()
        listScrollPane = JScrollPane()
        list = JList(model)
    }

    override fun styleViews() {
        titleView.text = title
        panel.border = Padding.Small()

        list.putClientProperty(FlatClientProperties.STYLE, "selectionArc: 6")
        list.background = panel.background

        listScrollPane.border = BorderFactory.createEmptyBorder()
    }

    override fun addToPanel() {
        listScrollPane.setViewportView(list)
        panel.apply {
            add(titleView.asPanel(), BorderLayout.NORTH)
            add(listScrollPane, BorderLayout.CENTER)
        }
    }

    fun isEnabled(value: Boolean) {
        list.isEnabled = value
    }

    fun isSingleSelectionEnabled(isEnabled: Boolean) {
        list.selectionMode =
            if (isEnabled) SINGLE_SELECTION
            else MULTIPLE_INTERVAL_SELECTION
    }
}