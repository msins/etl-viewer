package metadata

import java.sql.Connection

data class DatabaseDescriptor(
    val connection: Connection,
    val productName: String,
    val productVersion: String,
    val name: String,
    val driver: String,
    val user: String,
    val service: MetadataService
) {

    fun description(): String {
        return "'$user' on $name, $productName $productVersion [$driver]"
    }
}

