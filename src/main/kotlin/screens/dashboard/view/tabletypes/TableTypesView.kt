package screens.dashboard.view.tabletypes

import metadata.models.Fact
import common.swing.Padding
import common.swing.panel.AbstractPanelContainer
import screens.dashboard.model.listeners.DimensionAttributeSelectionListener
import screens.dashboard.model.listeners.FactSelectionListener
import screens.dashboard.model.listeners.MeasureSelectionListener
import metadata.models.DimensionAttribute
import metadata.models.Measure
import java.awt.GridLayout

class TableTypesView : AbstractPanelContainer() {

    private lateinit var factsSelectorView: TableTypeSelector<Fact>
    private lateinit var dimensionAttributesSelectorView: TableTypeSelector<DimensionAttribute>
    private lateinit var measuresSelectorView: TableTypeSelector<Measure>

    init {
        initialize()
    }

    override fun configurePanel() {
        panel.layout = GridLayout(3, 1)
    }

    override fun initializeComponents() {
        factsSelectorView = TableTypeSelector("Facts")
        dimensionAttributesSelectorView = TableTypeSelector("Dimensions")
        measuresSelectorView = TableTypeSelector("Measures")
    }

    override fun styleViews() {
        panel.border = Padding.Small()
        factsSelectorView.isSingleSelectionEnabled(true)
        dimensionAttributesSelectorView.isSingleSelectionEnabled(false)
        measuresSelectorView.isSingleSelectionEnabled(false)
    }

    override fun addToPanel() {
        panel.apply {
            add(factsSelectorView.asPanel())
            add(dimensionAttributesSelectorView.asPanel())
            add(measuresSelectorView.asPanel())
        }
    }

    fun isEnabled(value: Boolean) {
        factsSelectorView.isEnabled(value)
        dimensionAttributesSelectorView.isEnabled(value)
        measuresSelectorView.isEnabled(value)
    }

    fun setFacts(facts: Collection<Fact>) {
        factsSelectorView.model.clear()
        factsSelectorView.model.addAll(facts)
    }

    fun setDimensionAttributes(attributes: Collection<DimensionAttribute>) {
        dimensionAttributesSelectorView.model.clear()
        dimensionAttributesSelectorView.model.addAll(attributes)
    }

    fun setMeasures(measures: Collection<Measure>) {
        measuresSelectorView.model.clear()
        measuresSelectorView.model.addAll(measures)
    }

    fun setFactSelectionListener(listener: FactSelectionListener) {
        factsSelectorView.list.selectionModel.addListSelectionListener {
            if (!it.valueIsAdjusting) {
                listener.onFactSelected(factsSelectorView.list.selectedValue)
            }
        }
    }

    fun setMeasureSelectionListener(listener: MeasureSelectionListener) {
        measuresSelectorView.list.selectionModel.addListSelectionListener {
            if (!it.valueIsAdjusting) {
                listener.onMeasuresSelected(
                    measuresSelectorView.list.selectedValuesList
                )
            }
        }
    }

    fun setDimensionAttributesSelectionListener(listener: DimensionAttributeSelectionListener) {
        dimensionAttributesSelectorView.list.selectionModel.addListSelectionListener {
            if (!it.valueIsAdjusting) {
                listener.onDimensionAttributesSelected(
                    dimensionAttributesSelectorView.list.selectedValuesList
                )
            }
        }
    }
}