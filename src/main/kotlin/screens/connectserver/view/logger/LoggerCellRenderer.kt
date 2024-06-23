package screens.connectserver.view.logger

import java.awt.Component
import java.time.format.DateTimeFormatter
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer

class LoggerCellRenderer : ListCellRenderer<LogItem> {

    companion object {

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
    }

    private val label = JLabel()

    override fun getListCellRendererComponent(
        list: JList<out LogItem>,
        value: LogItem?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val logItem = list.model.getElementAt(index)
        val time = logItem.time.format(formatter)

        return label.apply {
            text = "[$time] ${logItem.text}"
            foreground = logItem.color
        }
    }
}