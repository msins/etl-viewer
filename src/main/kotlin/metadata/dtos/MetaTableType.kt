package metadata.dtos

class MetaTableType(
    val idTableType: Long,
    tableTypeName: String
) {

    val tableTypeName: String = tableTypeName.trim()

    override fun toString(): String {
        return "MetaTableType(" +
                "idTableType=$idTableType, " +
                "tableTypeName='$tableTypeName')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MetaTableType) return false

        if (idTableType != other.idTableType) return false

        return true
    }

    override fun hashCode(): Int {
        return idTableType.hashCode()
    }
}