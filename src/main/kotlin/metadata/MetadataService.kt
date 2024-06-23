package metadata

import metadata.dtos.*
import metadata.models.Dimension
import metadata.models.DimensionAttribute
import metadata.models.Fact
import metadata.models.Measure
import metadata.repository.MetadataRepository
import org.jooq.Condition
import org.jooq.Field
import org.jooq.Record
import org.jooq.Select
import org.jooq.Table
import org.jooq.impl.DSL

class MetadataService(private val repository: MetadataRepository) {

    fun getFacts(): List<Fact> {
        return repository.getFactTables()
            .map { getFactByFactId(it.id) }
    }

    fun getFactByFactId(factId: Long): Fact {
        val metaTable: MetaTable = repository.getFactTables().first { it.id == factId }
        val attributes: List<MetaTableAttribute> = repository.getAttributesByFactId(factId)
        val dimensions: List<Dimension> = getDimensionsByFactId(factId)
        val measures: List<Measure> = getMeasuresByFactId(factId)

        return Fact(metaTable, attributes, dimensions, measures)
    }

    fun getDimensionsByFactId(factId: Long): List<Dimension> {
        val dimensionTables: List<MetaTable> = repository.getDimensionTablesByFactId(factId)

        val dimensions = mutableListOf<Dimension>()

        for (table in dimensionTables) {
            val attributes: List<MetaTableAttribute> = repository.getAttributesByDimensionId(table.id)
            val primaryKey: MetaTableAttribute = attributes.first { it.attrOrdinal == 1 }
            val otherAttributes: List<MetaTableAttribute> = attributes.filter { it != primaryKey }

            dimensions.add(Dimension(table, primaryKey, otherAttributes))
        }

        return dimensions
    }

    fun getMeasuresByFactId(factId: Long): List<Measure> {
        val attributes: List<MetaTableAttribute> = repository
            .getMetaTableAttributes()
            .filter { it.idTable == factId }
            .filter { it.type == MetaTableAttribute.Type.MEASURE }

        val aggregateFunctions: List<MetaTableAttributeAgrFun> = repository
            .getMeasuresByFactId(factId)

        val measures: MutableList<Measure> = mutableListOf()

        for (attribute in attributes) {
            val matchingAggregates = aggregateFunctions
                .filter { it.attrOrdinal == attribute.attrOrdinal }
            for (aggregateFunction in matchingAggregates) {
                measures.add(
                    Measure(
                        attribute,
                        aggregateFunction
                    )
                )
            }
        }

        return measures
    }

    fun generateQuery(
        fact: Fact,
        attributes: Collection<DimensionAttribute>,
        measures: Collection<Measure>
    ): Select<Record> {
        val context = repository.context

        val aggregateFields: List<Field<*>> = JooqQueryHelper.createAggregateFields(measures)
        val attributeFields: List<Field<*>> = JooqQueryHelper.createAttributeFields(attributes)
        val fromTables: List<Table<*>> = JooqQueryHelper.createFromTables(fact, attributes)
        val groupByFields:List<Field<*>> = JooqQueryHelper.createGroupByFields(attributes)
        val whereConditions: List<Condition> = attributes
            .map { it.dimension }
            .distinct()
            .map {
                createWhereConditionForFactId(fact.meta.id, it)
            }

        val query = context
            .select(aggregateFields)
            .select(attributeFields)
            .from(fromTables)
            .where(whereConditions)

        if (groupByFields.isEmpty()) {
            return query
        }

        return query
            .groupBy(groupByFields)
            .orderBy(groupByFields)
    }

    private fun createWhereConditionForFactId(factId: Long, dimension: Dimension): Condition {
        val dimensionToFact: List<MetaDimFact> = repository.getMetaDimFacts()
        val fact: Fact = getFactByFactId(factId)

        val metaDimFact: MetaDimFact = dimensionToFact.first {
            it.idFactTable == factId && it.idDimTable == dimension.meta.id
        }

        val factForeignKey: MetaTableAttribute = fact.attributes.first {
            it.attrOrdinal == metaDimFact.factAttrOrdinal
        }

        val dimensionPrimaryKey: MetaTableAttribute = dimension.primaryKey

        return DSL.condition(
            "${fact.meta.sqlName}.${factForeignKey.sqlName} = ${dimension.meta.sqlName}.${dimensionPrimaryKey.sqlName}"
        )
    }
}
