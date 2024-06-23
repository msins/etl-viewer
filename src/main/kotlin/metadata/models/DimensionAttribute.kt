package metadata.models

import metadata.dtos.MetaTableAttribute

data class DimensionAttribute(
    val dimension: Dimension,
    val meta: MetaTableAttribute
) {

    val sqlName: String = "${dimension.meta.sqlName}.${meta.sqlName}"

    override fun toString(): String {
        return "$dimension > ${meta.name}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DimensionAttribute) return false

        if (dimension != other.dimension) return false
        if (meta != other.meta) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dimension.hashCode()
        result = 31 * result + meta.hashCode()
        return result
    }
}