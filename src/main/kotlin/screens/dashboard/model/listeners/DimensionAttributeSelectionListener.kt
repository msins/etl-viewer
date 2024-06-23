package screens.dashboard.model.listeners

import metadata.models.DimensionAttribute

interface DimensionAttributeSelectionListener {

    fun onDimensionAttributesSelected(attributes: Collection<DimensionAttribute>)
}