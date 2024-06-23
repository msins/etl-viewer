package screens.dashboard.model.listeners

import metadata.models.Fact
import java.util.EventListener

interface FactChangeListener : EventListener {

    fun onFactsChanged(facts: Collection<Fact>)
}