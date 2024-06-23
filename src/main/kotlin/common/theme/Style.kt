package common.theme

import javax.swing.UIManager

object Style {

    private const val SPLIT_PANE_GRIP_DOT_COUNT = 0

    fun apply() {
        UIManager.put("SplitPaneDivider.gripDotCount", SPLIT_PANE_GRIP_DOT_COUNT)
        UIManager.put("SplitPane.dividerSize", Sizes.DIVIDER_WIDTH)
        UIManager.put("SplitPane.background", Theme.splitPaneDivider)
    }
}