package metadata.repository

import metadata.dtos.*
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Select

class CachedMetadataRepository(private val delegate: MetadataRepository) : MetadataRepository {

    override var context: DSLContext = delegate.context

    private var tables: List<MetaTable> = listOf()
    private var facts: List<MetaTable> = listOf()
    private var dimensions: List<MetaTable> = listOf()
    private var metaDimFacts: List<MetaDimFact> = listOf()
    private var metaTableAttributes: List<MetaTableAttribute> = listOf()
    private var metaTableAttributeAgrFunctions: List<MetaTableAttributeAgrFun> = listOf()
    private var metaTableTypes: List<MetaTableType> = listOf()

    override fun getTables(): List<MetaTable> {
        if (tables.isEmpty()) {
            tables = delegate.getTables()
        }

        return tables
    }

    override fun getFactTables(): List<MetaTable> {
        if (facts.isEmpty()) {
            facts = getTables().filter { it.isFact }
        }

        return facts
    }

    override fun getDimensionTables(): List<MetaTable> {
        if (dimensions.isEmpty()) {
            dimensions = delegate.getTables().filter { it.isDimension }
        }

        return dimensions
    }

    override fun from(name: String): Select<Record> {
        return delegate.from(name)
    }

    override fun getMetaDimFacts(): List<MetaDimFact> {
        if (metaDimFacts.isEmpty()) {
            metaDimFacts = delegate.getMetaDimFacts()
        }

        return metaDimFacts
    }

    override fun getMetaTableAttributes(): List<MetaTableAttribute> {
        if (metaTableAttributes.isEmpty()) {
            metaTableAttributes = delegate.getMetaTableAttributes()
        }

        return metaTableAttributes
    }

    override fun getMetaTableAttributeAgrFunctions(): List<MetaTableAttributeAgrFun> {
        if (metaTableAttributeAgrFunctions.isEmpty()) {
            metaTableAttributeAgrFunctions = delegate.getMetaTableAttributeAgrFunctions()
        }

        return metaTableAttributeAgrFunctions
    }

    override fun getMetaTableTypes(): List<MetaTableType> {
        if (metaTableTypes.isEmpty()) {
            metaTableTypes = delegate.getMetaTableTypes()
        }

        return metaTableTypes
    }
}