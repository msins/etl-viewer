package metadata.models

import metadata.dtos.MetaTable
import metadata.dtos.MetaTableAttribute

data class Fact(
    val meta: MetaTable,
    val attributes: Collection<MetaTableAttribute>,
    val dimensions: Collection<Dimension>,
    val measures: Collection<Measure>
) {

    val dimensionAttributes: Collection<DimensionAttribute>
        get() {
            val attributes = mutableListOf<DimensionAttribute>()

            for (dimension in dimensions) {
                for (metaAttribute in dimension.attributes) {
                    attributes.add(DimensionAttribute(dimension, metaAttribute))
                }
            }

            return attributes
        }

    override fun toString(): String {
        return meta.name
    }
}