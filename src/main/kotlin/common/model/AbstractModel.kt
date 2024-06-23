package common.model

import common.swing.extensions.fire
import java.util.*
import javax.swing.event.EventListenerList

abstract class AbstractModel {

    protected val listeners = EventListenerList()

    protected inline fun <reified T: EventListener> add(listener: T) {
        listeners.add(T::class.java, listener)
    }

    protected inline fun <reified T: EventListener> fire(noinline consumer: (T) -> Unit) {
        listeners.fire(T::class.java, consumer)
    }
}