package common.swing.panel

import javax.swing.JPanel

abstract class AbstractPanelContainer : PanelContainer {

    protected val panel = JPanel()

    override fun asPanel(): JPanel {
        return panel
    }
}