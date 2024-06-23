package common.swing.panel

import javax.swing.JPanel

interface PanelContainer {

    fun asPanel(): JPanel

    fun initialize() {
        configurePanel()
        initializeComponents()
        styleViews()
        addToPanel()
    }

    fun configurePanel()
    fun initializeComponents()
    fun styleViews()
    fun addToPanel()
}