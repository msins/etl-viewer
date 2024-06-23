package common.swing.frame

import javax.swing.JFrame

abstract class AbstractFrameContainer : FrameContainer {

    protected val frame = JFrame()

    override fun dispose() {
        frame.dispose()
    }

    override fun asFrame(): JFrame {
        return frame
    }
}