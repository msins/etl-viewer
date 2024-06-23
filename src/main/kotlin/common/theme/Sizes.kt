package common.theme

import java.awt.Dimension

object Sizes {

    object Dimensions {

        private const val ONBOARDING_WINDOW_WIDTH = 700
        private const val ONBOARDING_WINDOW_HEIGHT = 400
        val connectServerWindow = Dimension(ONBOARDING_WINDOW_WIDTH, ONBOARDING_WINDOW_HEIGHT)

        private const val MAIN_WINDOW_WIDTH = 1000
        private const val MAIN_WINDOW_HEIGHT = 800
        val mainWindow = Dimension(MAIN_WINDOW_WIDTH, MAIN_WINDOW_HEIGHT)
    }

    const val DIVIDER_WIDTH = 1
}