package screens.dashboard.model.listeners

import metadata.models.Fact

interface FactSelectionListener {

    fun onFactSelected(fact: Fact)
}