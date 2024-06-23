package common.swing.extensions

import java.util.EventListener
import javax.swing.event.EventListenerList

fun <T : EventListener> EventListenerList.fire(type: Class<T>, consumer: (T) -> Unit) {
    getListeners(type).forEach { consumer(it) }
}

