package common.swing

import common.theme.Margins
import javax.swing.border.EmptyBorder

sealed class Padding(
    top: Int,
    left: Int,
    bottom: Int,
    right: Int
) : EmptyBorder(top, left, bottom, right) {

    class Small(
        top: Int = Margins.SMALL,
        left: Int = Margins.SMALL,
        bottom: Int = Margins.SMALL,
        right: Int = Margins.SMALL
    ) : Padding(top, left, bottom, right)

    class Medium(
        top: Int = Margins.MEDIUM,
        left: Int = Margins.MEDIUM,
        bottom: Int = Margins.MEDIUM,
        right: Int = Margins.MEDIUM
    ) : Padding(top, left, bottom, right)
}