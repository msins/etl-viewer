package metadata.repository

import metadata.dtos.*
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Select

interface MetadataRepository {

    // =========================================
    // Metadata
    // =========================================

    var context: DSLContext

    fun getTables(): List<MetaTable>
    fun getMetaDimFacts(): List<MetaDimFact>
    fun getMetaTableAttributes(): List<MetaTableAttribute>
    fun getMetaTableAttributeAgrFunctions(): List<MetaTableAttributeAgrFun>
    fun getMetaTableTypes(): List<MetaTableType>

    fun getFactTables(): List<MetaTable> {
        return getTables().filter { it.isFact }
    }

    fun getDimensionTables(): List<MetaTable> {
        return getTables().filter { it.isDimension }
    }

    fun getDimensionTablesByFactId(factId: Long): List<MetaTable> {
        val dimensions = getDimensionTables()
        val dimensionIdsForFact = getMetaDimFacts()
            .filter { it.idFactTable == factId }
            .map { it.idDimTable }

        return dimensions.filter { dimensionIdsForFact.contains(it.id) }
    }

    fun getAttributesByDimensionId(dimensionId: Long): List<MetaTableAttribute> {
        return getMetaTableAttributes()
            .filter { it.idTable == dimensionId }
            .filter { it.type == MetaTableAttribute.Type.DIMENSION_ATTRIBUTE }
    }

    fun getAttributesByFactId(factId: Long): List<MetaTableAttribute> {
        return getMetaTableAttributes()
            .filter { it.idTable == factId }
    }

    fun getMeasuresByFactId(factId: Long): List<MetaTableAttributeAgrFun> {
        val measures = getMetaTableAttributeAgrFunctions()
        return measures.filter { it.idTable == factId }
    }

    // =========================================
    // Queries
    // =========================================

    fun from(name: String): Select<Record>
}