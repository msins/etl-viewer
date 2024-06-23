package screens.dashboard.model.listeners

import java.util.EventListener

interface SqlChangeListener : EventListener {

    fun onSqlChanged(sql: String)
}