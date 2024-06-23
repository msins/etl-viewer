package screens.dashboard.view

import metadata.models.Fact
import common.swing.components.ExtendedDragAreaSplitPane
import common.swing.frame.AbstractFrameContainer
import common.swing.panel.AbstractPanelContainer
import common.theme.Sizes
import screens.dashboard.model.listeners.FactSelectionListener
import screens.dashboard.model.DashboardModel
import screens.dashboard.model.listeners.FactChangeListener
import screens.dashboard.model.listeners.QueryResultsChangeListener
import screens.dashboard.model.listeners.SqlChangeListener
import screens.dashboard.view.resultset.ResultSetModel
import screens.dashboard.view.resultset.ResultSetView
import screens.dashboard.view.tabletypes.TableTypesView
import common.swing.listeners.ExecutionListener
import common.swing.listeners.ExecutionState
import screens.dashboard.model.listeners.DimensionAttributeSelectionListener
import screens.dashboard.model.listeners.MeasureSelectionListener
import metadata.models.DimensionAttribute
import metadata.models.Measure
import screens.dashboard.model.listeners.DimensionAttributeChangeListener
import screens.dashboard.model.listeners.MeasureChangeListener
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.ActionListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.WindowConstants


class DashboardView(
    val model: DashboardModel
) : AbstractFrameContainer() {

    init {
        initialize()
        model.addFactChangeListener(FactsChangedListenerImpl())
        model.addDimensionAttributeChangeListener(DimensionAttributesChangedListenerImpl())
        model.addMeasureChangeListener(MeasuresChangedListenerImpl())
        model.addQueryExecutionListener(QueryExecutionListenerImpl())
        model.addSqlChangeListener(SqlChangedListenerImpl())
        model.addQueryResultsListener(QueryResultsChangeListenerImpl())

        frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                super.windowClosed(e)
                menu.exitApplicationItem.doClick()
            }
        })
    }

    private lateinit var menu: DashboardMenuBar

    private lateinit var propertiesContainer: PropertiesContainer
    private lateinit var resultsContainer: ResultsContainer

    override fun configureFrame() {
        frame.apply {
            title = model.title
            defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
            size = Sizes.Dimensions.mainWindow
            setLocationRelativeTo(null)
            isResizable = true
        }
    }

    override fun initializeComponents() {
        menu = DashboardMenuBar()
        propertiesContainer = PropertiesContainer()
        resultsContainer = ResultsContainer()
    }

    override fun styleViews() {
    }

    override fun addToFrame() {
        frame.jMenuBar = menu
        frame.add(SplitContainer().asPanel())
    }

    override fun showItself() {
        frame.isVisible = true
    }

    // =========================================
    // Containers
    // =========================================

    private inner class SplitContainer : AbstractPanelContainer() {

        init {
            initialize()
        }

        private lateinit var splitPane: ExtendedDragAreaSplitPane

        override fun configurePanel() {
            panel.layout = BorderLayout()
        }

        override fun initializeComponents() {
            splitPane = ExtendedDragAreaSplitPane()
            splitPane.leftComponent = propertiesContainer.asPanel()
            splitPane.rightComponent = resultsContainer.asPanel()
        }

        override fun styleViews() {
            splitPane.isContinuousLayout = true
            splitPane.resizeWeight = 0.3
        }

        override fun addToPanel() {
            panel.add(splitPane)
        }
    }


    private inner class ResultsContainer : AbstractPanelContainer() {

        init {
            initialize()
        }

        private lateinit var splitPane: ExtendedDragAreaSplitPane

        private lateinit var resultSetView: ResultSetView
        private lateinit var sqlConsoleView: SqlConsoleView

        override fun configurePanel() {
            panel.layout = BorderLayout()
        }

        override fun initializeComponents() {
            splitPane = ExtendedDragAreaSplitPane()

            resultSetView = ResultSetView()
            sqlConsoleView = SqlConsoleView()

            splitPane.topComponent = resultSetView.asPanel()
            splitPane.bottomComponent = sqlConsoleView.asPanel()
        }

        override fun styleViews() {
            splitPane.isContinuousLayout = true
            splitPane.orientation = JSplitPane.VERTICAL_SPLIT
        }

        override fun addToPanel() {
            panel.add(splitPane)
        }

        fun setResultSetModel(model: ResultSetModel) {
            resultSetView.setModel(model)
        }

        fun setSql(sql: String) {
            sqlConsoleView.text = sql
        }
    }


    private inner class PropertiesContainer : AbstractPanelContainer() {

        lateinit var tableTypesView: TableTypesView

        lateinit var runButtonContainer: JPanel
        lateinit var runButton: JButton

        init {
            initialize()
        }

        override fun configurePanel() {
            panel.layout = BorderLayout()
        }

        override fun initializeComponents() {
            tableTypesView = TableTypesView()

            runButtonContainer = JPanel()
            runButton = JButton()
            runButton.actionCommand = "Execute Query"
        }

        override fun styleViews() {
            runButton.layout = FlowLayout()
            runButton.text = "Execute"
        }

        override fun addToPanel() {
            runButtonContainer.add(runButton)

            panel.apply {
                add(tableTypesView.asPanel(), BorderLayout.CENTER)
                add(runButtonContainer, BorderLayout.SOUTH)
            }
        }

        fun addExecuteQueryClickListener(listener: ActionListener) {
            runButton.addActionListener(listener)
        }
    }

    // =========================================
    // Listeners
    // =========================================

    fun addOpenNewConnectionClickListener(listener: ActionListener) {
        menu.addOpenNewConnectionClickListener(listener)
    }

    fun addExitApplicationClickListener(listener: ActionListener) {
        menu.addExitApplicationClickListener(listener)
    }

    fun addExecuteQueryClickListener(listener: ActionListener) {
        propertiesContainer.addExecuteQueryClickListener(listener)
    }

    fun addFactSelectedListener(listener: FactSelectionListener) {
        propertiesContainer.tableTypesView.setFactSelectionListener(listener)
    }

    fun addMeasuresSelectedListener(listener: MeasureSelectionListener) {
        propertiesContainer.tableTypesView.setMeasureSelectionListener(listener)
    }

    fun addDimensionAttributesSelectedListener(listener: DimensionAttributeSelectionListener) {
        propertiesContainer.tableTypesView.setDimensionAttributesSelectionListener(listener)
    }

    private inner class QueryResultsChangeListenerImpl : QueryResultsChangeListener {

        override fun onQueryResultsLoaded(model: ResultSetModel) {
            resultsContainer.setResultSetModel(model)
        }
    }


    private inner class QueryExecutionListenerImpl : ExecutionListener {

        override fun onExecutionStateChanged(state: ExecutionState) {
            when (state) {
                ExecutionState.STARTED -> {
                    propertiesContainer.tableTypesView.isEnabled(false)
                }

                ExecutionState.ENDED -> {
                    propertiesContainer.tableTypesView.isEnabled(true)
                }
            }
        }
    }


    private inner class FactsChangedListenerImpl : FactChangeListener {

        override fun onFactsChanged(facts: Collection<Fact>) {
            propertiesContainer.tableTypesView.setFacts(facts)
        }
    }


    private inner class DimensionAttributesChangedListenerImpl : DimensionAttributeChangeListener {

        override fun onAttributesChanged(attributes: Collection<DimensionAttribute>) {
            propertiesContainer.tableTypesView.setDimensionAttributes(attributes)
        }
    }


    private inner class MeasuresChangedListenerImpl : MeasureChangeListener {

        override fun onMeasuresChanged(measures: Collection<Measure>) {
            propertiesContainer.tableTypesView.setMeasures(measures)
        }
    }


    private inner class SqlChangedListenerImpl : SqlChangeListener {

        override fun onSqlChanged(sql: String) {
            resultsContainer.setSql(sql)
        }
    }
}
