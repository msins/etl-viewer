package metadata.models

import metadata.dtos.MetaTableAttribute
import metadata.dtos.MetaTableAttributeAgrFun

data class Measure(
    val attribute: MetaTableAttribute,
    val aggregateFunction: MetaTableAttributeAgrFun
) {

    override fun toString(): String {
        return aggregateFunction.attributeName
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Measure) return false

        if (attribute != other.attribute) return false
        if (aggregateFunction != other.aggregateFunction) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attribute.hashCode()
        result = 31 * result + aggregateFunction.hashCode()
        return result
    }
}