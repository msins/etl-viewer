package screens.dashboard.view

import java.awt.event.ActionListener
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

class DashboardMenuBar: JMenuBar() {

    private val fileMenu = JMenu("File")
    val openNewConnectionItem = JMenuItem("New connection")
    val exitApplicationItem = JMenuItem("Exit")

    init {
        add(fileMenu)
        fileMenu.apply {
            add(openNewConnectionItem)
            addSeparator()
            add(exitApplicationItem)
        }
    }

    fun addOpenNewConnectionClickListener(listener: ActionListener) {
        openNewConnectionItem.addActionListener(listener)
    }

    fun addExitApplicationClickListener(listener: ActionListener) {
        exitApplicationItem.addActionListener(listener)
    }
}