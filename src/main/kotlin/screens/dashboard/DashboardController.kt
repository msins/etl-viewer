package screens.dashboard

import screens.dashboard.model.listeners.DimensionAttributeSelectionListener
import screens.dashboard.model.listeners.ExecuteQueryListener
import screens.dashboard.model.listeners.FactSelectionListener
import screens.dashboard.model.listeners.MeasureSelectionListener
import metadata.DatabaseDescriptor
import metadata.models.Fact
import metadata.models.Measure
import metadata.models.DimensionAttribute
import screens.connectserver.ConnectServerController
import screens.dashboard.model.DashboardModel
import screens.dashboard.model.DefaultDashboardModel
import screens.dashboard.view.DashboardView
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class DashboardController(
    private val view: DashboardView,
    private val model: DashboardModel
) : ActionListener,
    FactSelectionListener,
    MeasureSelectionListener,
    ExecuteQueryListener,
    DimensionAttributeSelectionListener {

    init {
        view.addExitApplicationClickListener(this)
        view.addOpenNewConnectionClickListener(this)
        view.addExecuteQueryClickListener(this)
        view.addFactSelectedListener(this)
        view.addMeasuresSelectedListener(this)
        view.addDimensionAttributesSelectedListener(this)
    }

    companion object {

        fun new(descriptor: DatabaseDescriptor): DashboardController {
            val model = DefaultDashboardModel(descriptor)
            val view = DashboardView(model)
            return DashboardController(view, model)
        }
    }

    fun show() {
        view.showItself()
        model.loadInitialData()
    }

    // =========================================
    // Actions
    // =========================================

    override fun actionPerformed(e: ActionEvent) {
        when (e.actionCommand) {
            "New connection" -> {
                onNewConnectionClicked()
            }

            "Exit" -> {
                onExitClicked()
            }

            "Execute Query" -> {
                onExecuteQueryClicked()
            }
        }
    }

    private fun onNewConnectionClicked() {
        ConnectServerController.new().show()
    }

    private fun onExitClicked() {
        model.dispose()
             .thenRun {
                 view.dispose()
             }
    }

    // =========================================
    // Hooks
    // =========================================

    override fun onFactSelected(fact: Fact) {
        model.selectedFact = fact
    }

    override fun onDimensionAttributesSelected(attributes: Collection<DimensionAttribute>) {
        model.selectedDimensionAttributes = attributes
    }

    override fun onMeasuresSelected(measures: Collection<Measure>) {
        model.selectedMeasures = measures
    }

    override fun onExecuteQueryClicked() {
        model.executeActiveQuery()
    }
}