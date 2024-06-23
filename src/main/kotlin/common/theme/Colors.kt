package common.theme

import java.awt.Color
import javax.swing.UIManager

object Colors {

    val splitPaneDividerGripColor = color(key = "SplitPaneDivider.gripColor")
    val green = color(key = "Objects.Green")
    val red = color(key = "Objects.Red")
    val labelForeground = color(key = "Label.foreground")

    private fun color(key: String) : Color {
        return Color(UIManager.getColor(key).rgb)
    }
}