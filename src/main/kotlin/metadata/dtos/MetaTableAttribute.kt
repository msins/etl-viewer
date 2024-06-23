package metadata.dtos

class MetaTableAttribute(
    val idTable: Long,
    val attrOrdinal: Int,
    attrSQLName: String,
    idAttrType: Int,
    attrName: String
) {

    enum class Type {
        MEASURE,
        DIMENSION_ATTRIBUTE,
        FOREIGN_KEY;

        companion object {

            fun fromValue(id: Int): Type {
                return when (id) {
                    1 -> MEASURE
                    2 -> DIMENSION_ATTRIBUTE
                    3 -> FOREIGN_KEY
                    else -> { throw IllegalArgumentException() }
                }
            }
        }
    }

    val name: String = attrName.trim()
    val sqlName: String = attrSQLName.trim()
    val type: Type = Type.fromValue(idAttrType)

    override fun toString(): String {
        return "MetaTableAttribute(" +
                "idTable=$idTable, " +
                "attrOrdinal=$attrOrdinal, " +
                "attrSQLName='$sqlName', " +
                "type=$type, " +
                "attrName='$name')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MetaTableAttribute) return false

        if (idTable != other.idTable) return false
        if (attrOrdinal != other.attrOrdinal) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idTable.hashCode()
        result = 31 * result + attrOrdinal
        return result
    }
}