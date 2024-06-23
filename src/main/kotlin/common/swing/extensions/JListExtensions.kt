package common.swing.extensions

import javax.swing.JList

fun <T> JList<T>.scrollToBottom() {
    val lastIndex = model.size - 1
    if (lastIndex >= 0) {
        this.ensureIndexIsVisible(lastIndex)
    }
}
