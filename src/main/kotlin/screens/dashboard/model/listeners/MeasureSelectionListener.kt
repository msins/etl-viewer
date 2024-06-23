package screens.dashboard.model.listeners

import metadata.models.Measure

interface MeasureSelectionListener {

    fun onMeasuresSelected(measures: Collection<Measure>)
}