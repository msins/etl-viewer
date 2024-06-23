package screens.dashboard.model

import screens.dashboard.model.listeners.*
import common.swing.listeners.ExecutionListener
import metadata.models.DimensionAttribute
import metadata.models.Fact
import metadata.models.Measure
import java.util.concurrent.CompletableFuture

interface DashboardModel {

    var title: String
    var selectedFact: Fact?

    var selectedDimensionAttributes: Collection<DimensionAttribute>
    var selectedMeasures: Collection<Measure>

    fun loadInitialData()
    fun executeActiveQuery()

    fun addFactChangeListener(listener: FactChangeListener)
    fun addDimensionAttributeChangeListener(listener: DimensionAttributeChangeListener)
    fun addMeasureChangeListener(listener: MeasureChangeListener)
    fun addSqlChangeListener(listener: SqlChangeListener)
    fun addQueryResultsListener(listener: QueryResultsChangeListener)
    fun addQueryExecutionListener(listener: ExecutionListener)

    fun dispose(): CompletableFuture<Unit>
}