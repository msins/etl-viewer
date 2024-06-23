package metadata

import metadata.dtos.MetaTableAttributeAgrFun
import metadata.models.Dimension
import metadata.models.DimensionAttribute
import metadata.models.Fact
import metadata.models.Measure
import org.jooq.Field
import org.jooq.Table
import org.jooq.impl.DSL

object JooqQueryHelper {

    fun createAttributeFields(attributes: Collection<DimensionAttribute>): List<Field<*>> {
        val fields = mutableListOf<Field<*>>()

        for (attribute in attributes) {
            fields.add(DSL.field(attribute.sqlName).`as`(attribute.meta.name))
        }

        return fields
    }

    fun createGroupByFields(attributes: Collection<DimensionAttribute>): List<Field<*>> {
        val groupByList = mutableListOf<Field<*>>()

        for (attribute in attributes) {
            groupByList.add(DSL.field(attribute.sqlName))
        }

        return groupByList
    }

    fun createFromTables(
        fact: Fact,
        attributes: Collection<DimensionAttribute>
    ): List<Table<*>> {
        val fromList = mutableListOf<Table<*>>()

        fromList.add(DSL.table(fact.meta.sqlName))

        val distinctDimensions: List<Dimension> = attributes
            .map { it.dimension }
            .distinct()

        for (dimension in distinctDimensions) {
            fromList.add(DSL.table(dimension.meta.sqlName))
        }

        return fromList
    }

    fun createAggregateFields(measures: Collection<Measure>): List<Field<*>> {
        val aggregates = mutableListOf<Field<*>>()
        if (measures.isNotEmpty()) {
            for (measure in measures) {
                val field = DSL.field(measure.attribute.sqlName, Double::class.java)
                aggregates.add(
                    when (measure.aggregateFunction.type) {
                        MetaTableAttributeAgrFun.Type.SUM -> DSL.sum(field)
                        MetaTableAttributeAgrFun.Type.AVG -> DSL.avg(field)
                        MetaTableAttributeAgrFun.Type.MIN -> DSL.min(field)
                        MetaTableAttributeAgrFun.Type.MAX -> DSL.max(field)
                        MetaTableAttributeAgrFun.Type.COUNT -> DSL.count(field)
                    }.`as`(measure.aggregateFunction.attributeName)
                )
            }
        }

        return aggregates
    }
}