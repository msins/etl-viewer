package common.swing.components

import javax.swing.JSplitPane
import javax.swing.plaf.basic.BasicSplitPaneUI

class ExtendedDragAreaSplitPane: JSplitPane(HORIZONTAL_SPLIT) {

    companion object {

        const val DIVIDER_DRAG_OFFSET: Int = 4
        const val DIVIDER_DRAT_SIZE: Int = 9
    }

    override fun doLayout() {
        super.doLayout()

        val divider = (getUI() as BasicSplitPaneUI).divider
        val bounds = divider.bounds
        if (orientation == HORIZONTAL_SPLIT) {
            bounds.x -= DIVIDER_DRAG_OFFSET
            bounds.width = DIVIDER_DRAT_SIZE
        } else {
            bounds.y -= DIVIDER_DRAG_OFFSET
            bounds.height = DIVIDER_DRAT_SIZE
        }

        divider.bounds = bounds
    }
}