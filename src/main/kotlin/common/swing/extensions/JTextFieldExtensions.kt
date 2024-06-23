package common.swing.extensions

import com.formdev.flatlaf.FlatClientProperties
import java.awt.Color
import javax.swing.JTextField

fun JTextField.isErrorHintShown(isShown: Boolean) {
    if (isShown) {
        putClientProperty(
            FlatClientProperties.OUTLINE,
            FlatClientProperties.OUTLINE_ERROR
        )
    } else {
        putClientProperty(
            FlatClientProperties.OUTLINE,
            Color.TRANSLUCENT
        )
    }
}

fun JTextField.setPlaceholder(text: String) {
    putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT,
        text
    )
}

