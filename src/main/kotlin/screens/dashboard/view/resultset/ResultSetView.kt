package screens.dashboard.view.resultset

import common.swing.panel.AbstractPanelContainer
import common.theme.Colors
import common.theme.Margins
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.DefaultTableModel
import kotlin.math.max


class ResultSetView: AbstractPanelContainer() {

    private lateinit var scrollPane: JScrollPane
    private lateinit var table: JTable
    private lateinit var model: DefaultTableModel

    init {
        initialize()
    }

    override fun configurePanel() {
        panel.layout = BorderLayout()
    }

    override fun initializeComponents() {
        model = DefaultTableModel()
        table = JTable(model)
        scrollPane = JScrollPane(
            table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        )
    }

    override fun styleViews() {
        scrollPane.border = BorderFactory.createEmptyBorder()

        table.apply {
            background = panel.background
            setShowGrid(true)
            gridColor = Colors.splitPaneDividerGripColor
            isEnabled = false
            autoResizeMode = JTable.AUTO_RESIZE_OFF
            tableHeader.background = panel.background
        }
    }

    override fun addToPanel() {
        panel.add(scrollPane)
    }

    fun setModel(model: ResultSetModel) {
        when (model) {
            is ResultSetModel.Success -> {
                this.model.setDataVector(model.data, model.headers)
                setPreferredColumnWidths(model)
            }

            is ResultSetModel.Error -> {
                this.model.dataVector.removeAllElements()

                this.model.setColumnIdentifiers(arrayOf("Error"))
                val messageParts = model.message.chunked(100)
                for (part in messageParts) {
                    this.model.addRow(arrayOf(part))
                }

                table.columnModel
                    .getColumn(0)
                    .setPreferredWidth(preferredColumnWidthForError(messageParts.first()))
            }
        }
    }

    private fun setPreferredColumnWidths(model: ResultSetModel.Success) {
        val firstRowData: Array<String> = model.data.firstOrNull() ?: arrayOf()

        val widthsBasedOnHeaders: IntArray = preferredColumnWidthsBasedOnHeaders(model.headers)
        val widthsBasedOnFirstRow: IntArray = preferredColumnWidthsBasedOnFirstRow(firstRowData)

        for (i in model.headers.indices) {
            val width = max(widthsBasedOnFirstRow[i], widthsBasedOnHeaders[i])
            table.columnModel.getColumn(i).setPreferredWidth(width)
        }
    }

    private fun preferredColumnWidthsBasedOnFirstRow(row: Array<String>): IntArray {
        if (row.size != table.columnCount) {
            return IntArray(table.columnCount) { 0 }
        }

        val fontMetrics = table.getFontMetrics(table.font)

        val widths = IntArray(table.columnCount)

        for (column in 0 until table.columnCount) {
            widths[column] = fontMetrics.stringWidth(row[column]) + 2 * Margins.LARGE
        }

        return widths
    }

    private fun preferredColumnWidthsBasedOnHeaders(headers: Array<String>): IntArray {
        if (headers.size != table.columnCount) {
            return IntArray(table.columnCount) { 0 }
        }

        val fontMetrics = table.tableHeader.getFontMetrics(table.tableHeader.font)

        val widths = IntArray(table.columnCount)

        for (column in 0 until table.columnCount) {
            widths[column] = fontMetrics.stringWidth(headers[column]) + 2 * Margins.LARGE
        }

        return widths
    }

    private fun preferredColumnWidthForError(message: String): Int {
        val fontMetrics = table.getFontMetrics(table.font)
        return fontMetrics.stringWidth(message) + 2 * Margins.LARGE
    }
}