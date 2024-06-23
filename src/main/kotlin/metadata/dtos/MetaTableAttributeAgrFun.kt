package metadata.dtos

class MetaTableAttributeAgrFun(
    val idTable: Long,
    val attrOrdinal: Int,
    idAgrFun: Int,
    attrName: String
) {

    enum class Type {
        SUM,
        COUNT,
        AVG,
        MIN,
        MAX;

        companion object {

            fun fromId(id: Int): Type {
                return when (id) {
                    1 -> SUM
                    2 -> COUNT
                    3 -> AVG
                    4 -> MIN
                    5 -> MAX
                    else -> { throw IllegalArgumentException() }
                }
            }
        }
    }

    val attributeName: String = attrName.trim()
    val aggregateFunctionId: Int = idAgrFun
    val type: Type = Type.fromId(idAgrFun)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MetaTableAttributeAgrFun) return false

        if (idTable != other.idTable) return false
        if (attrOrdinal != other.attrOrdinal) return false
        if (aggregateFunctionId != other.aggregateFunctionId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idTable.hashCode()
        result = 31 * result + attrOrdinal
        result = 31 * result + aggregateFunctionId
        return result
    }
}