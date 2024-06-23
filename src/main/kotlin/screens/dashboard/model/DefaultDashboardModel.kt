package screens.dashboard.model

import metadata.models.Fact
import metadata.MetadataService
import common.model.AbstractModel
import org.jooq.Record
import org.jooq.Select
import screens.dashboard.model.listeners.*
import screens.dashboard.view.resultset.ResultSetModel
import common.swing.listeners.ExecutionListener
import common.swing.listeners.ExecutionState
import metadata.DatabaseDescriptor
import metadata.models.DimensionAttribute
import metadata.models.Measure
import org.jooq.exception.DataAccessException
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.concurrent.CompletableFuture

class DefaultDashboardModel(
    private val descriptor: DatabaseDescriptor,
    private val service: MetadataService = descriptor.service
) : AbstractModel(), DashboardModel {

    /**
     * Query displayed to the user.
     */
    private var activeQuery: Select<Record>? = null
    private val activeQuerySql: String
        get() = activeQuery?.sql ?: ""

    private var facts: List<Fact> = listOf()

    override var title: String = descriptor.description()

    override var selectedFact: Fact? = null
        set(value) {
            field = value
            onSelectedFactChanged()
            updateActiveQueryAndNotify()
        }

    override var selectedMeasures: Collection<Measure> = listOf()
        set(value) {
            field = value
            updateActiveQueryAndNotify()
        }

    override var selectedDimensionAttributes: Collection<DimensionAttribute> = listOf()
        set(value) {
            field = value
            updateActiveQueryAndNotify()
        }

    override fun loadInitialData() {
        facts = service.getFacts()
        fire<FactChangeListener> {
            it.onFactsChanged(facts)
        }
    }

    private fun onSelectedFactChanged() {
        val selectedFact = selectedFact ?: return

        fire<DimensionAttributeChangeListener> {
            it.onAttributesChanged(selectedFact.dimensionAttributes)
        }
        fire<MeasureChangeListener> {
            it.onMeasuresChanged(selectedFact.measures)
        }
    }

    private fun updateActiveQueryAndNotify() {
        val selectedFact = selectedFact ?: return

        activeQuery = service.generateQuery(
            selectedFact,
            selectedDimensionAttributes,
            selectedMeasures
        )

        fire<SqlChangeListener> {
            it.onSqlChanged(activeQuerySql)
        }
    }

    override fun executeActiveQuery() {
        fire<ExecutionListener> {
            it.onExecutionStateChanged(ExecutionState.STARTED)
        }

        val model: ResultSetModel = try {
            val result = activeQuery?.fetch() ?: throw IllegalStateException()
            ResultSetModel.Success.fromJooqResult(result)
        } catch (e: DataAccessException) {
            ResultSetModel.Error(e.cause?.message ?: e.javaClass.canonicalName)
        } catch (e: Exception) {
            ResultSetModel.Error(e.javaClass.canonicalName)
        }

        fire<QueryResultsChangeListener> {
            it.onQueryResultsLoaded(model)
        }

        fire<ExecutionListener> {
            it.onExecutionStateChanged(ExecutionState.ENDED)
        }
    }

    override fun dispose(): CompletableFuture<Unit> {
        return CompletableFuture.supplyAsync {
            descriptor.connection.apply {
                commit()
                close()
            }
        }
    }

    // =========================================
    // Listeners
    // =========================================

    override fun addMeasureChangeListener(listener: MeasureChangeListener) {
        add(listener)
    }

    override fun addFactChangeListener(listener: FactChangeListener) {
        add(listener)
    }

    override fun addDimensionAttributeChangeListener(listener: DimensionAttributeChangeListener) {
        add(listener)
    }

    override fun addSqlChangeListener(listener: SqlChangeListener) {
        add(listener)
    }

    override fun addQueryResultsListener(listener: QueryResultsChangeListener) {
        add(listener)
    }

    override fun addQueryExecutionListener(listener: ExecutionListener) {
        add(listener)
    }
}