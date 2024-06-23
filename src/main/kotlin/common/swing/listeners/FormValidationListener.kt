package common.swing.listeners

import java.util.EventListener

sealed class ValidationState {

    object Error : ValidationState()
    object Success : ValidationState()
}

interface FormValidationListener : EventListener {

    fun onValidationStateChanged(state: ValidationState)
}