package screens.dashboard.model.listeners

import screens.dashboard.view.resultset.ResultSetModel
import java.util.EventListener

interface QueryResultsChangeListener: EventListener {

    fun onQueryResultsLoaded(model: ResultSetModel)
}