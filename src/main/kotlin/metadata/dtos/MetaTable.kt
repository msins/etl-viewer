package metadata.dtos

class MetaTable(
    idTable: Long,
    tableName: String,
    tableSQLName: String,
    idTableType: Int
) {

    enum class Type {
        FACT,
        DIMENSION;

        companion object {

            fun fromTypeId(id: Int): Type {
                return when (id) {
                    1 -> FACT
                    2 -> DIMENSION
                    else -> { throw IllegalArgumentException() }
                }
            }
        }
    }

    val id: Long = idTable
    val name: String = tableName.trim()
    val sqlName: String = tableSQLName.trim()
    val type: Type = Type.fromTypeId(idTableType)

    var isFact: Boolean = type == Type.FACT
    var isDimension: Boolean = type == Type.DIMENSION

    override fun toString(): String {
        return sqlName
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MetaTable) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}