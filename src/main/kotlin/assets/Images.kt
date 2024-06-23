package assets

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

@Deprecated("Not used anymore")
object Images {

    val database: BufferedImage by lazy { loadImage("/icons/database.png") }
    val column: BufferedImage by lazy { loadImage("/icons/column.png") }
    val table: BufferedImage by lazy { loadImage("/icons/table.png") }
    val user: BufferedImage by lazy { loadImage("/icons/user.png") }
    val schema: BufferedImage by lazy { loadImage("/icons/schema.png") }

    private fun loadImage(fileName: String): BufferedImage {
        return ImageIO.read(javaClass.getResource(fileName))
    }
}