package common.swing.frame

import javax.swing.JFrame

interface FrameContainer {

    fun asFrame(): JFrame

    fun initialize() {
        configureFrame()
        initializeComponents()
        styleViews()
        addToFrame()
    }

    fun configureFrame()
    fun initializeComponents()
    fun styleViews()
    fun addToFrame()
    fun showItself()
    fun dispose()
}