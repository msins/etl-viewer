package screens.dashboard.model.listeners

import metadata.models.Measure
import java.util.EventListener

interface MeasureChangeListener : EventListener {

    fun onMeasuresChanged(measures: Collection<Measure>)
}