package metadata.models

import metadata.dtos.MetaTable
import metadata.dtos.MetaTableAttribute

data class Dimension(
    val meta: MetaTable,
    val primaryKey: MetaTableAttribute,
    val attributes: Collection<MetaTableAttribute>
) {

    override fun toString(): String {
        return meta.name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Dimension) return false

        if (meta != other.meta) return false

        return true
    }

    override fun hashCode(): Int {
        return meta.hashCode()
    }
}