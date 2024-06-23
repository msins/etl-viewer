package common.swing

import javax.swing.DefaultListSelectionModel

class DoNothingListSelectionModel : DefaultListSelectionModel() {

    override fun setAnchorSelectionIndex(anchorIndex: Int) {}
    override fun setLeadAnchorNotificationEnabled(flag: Boolean) {}
    override fun setLeadSelectionIndex(leadIndex: Int) {}
    override fun setSelectionInterval(index0: Int, index1: Int) {}
}