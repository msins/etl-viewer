package common.theme

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont
import java.awt.Font
import java.awt.font.TextAttribute

object Fonts {

    val editor: Font = Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 13)
        .deriveFont(
            mapOf(
                TextAttribute.LIGATURES to TextAttribute.LIGATURES_ON
            )
        )

    val leadingTitle: Font = Font("Helvetica", Font.BOLD, 16)
}