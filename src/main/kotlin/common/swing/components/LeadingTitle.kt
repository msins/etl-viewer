package common.swing.components

import common.swing.panel.AbstractPanelContainer
import common.theme.Fonts
import java.awt.FlowLayout
import javax.swing.JLabel

class LeadingTitle(private val title: String) : AbstractPanelContainer() {

    constructor(): this("")

    init {
        initialize()
    }

    var text: String
        get() = label.text
        set(value) {
            label.text = value
        }

    private lateinit var label: JLabel

    override fun configurePanel() {
        panel.layout = FlowLayout(FlowLayout.LEADING)
    }

    override fun initializeComponents() {
        label = JLabel()
    }

    override fun styleViews() {
        label.text = title
        label.font = Fonts.leadingTitle
    }

    override fun addToPanel() {
        panel.add(label)
    }
}