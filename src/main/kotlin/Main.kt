import com.formdev.flatlaf.themes.FlatMacLightLaf
import common.theme.Style
import screens.connectserver.ConnectServerController
import javax.swing.SwingUtilities

fun main() {
    // for sql server driver
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    FlatMacLightLaf.setup()
    Style.apply()
    SwingUtilities.invokeLater {
        ConnectServerController.new().show()
    }
}