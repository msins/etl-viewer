package screens.dashboard.model.listeners

import metadata.models.DimensionAttribute
import java.util.EventListener

interface DimensionAttributeChangeListener : EventListener {

    fun onAttributesChanged(attributes: Collection<DimensionAttribute>)
}