package common.swing.listeners

import java.util.EventListener

enum class ExecutionState {
    STARTED,
    ENDED
}

interface ExecutionListener : EventListener {

    fun onExecutionStateChanged(state: ExecutionState)
}