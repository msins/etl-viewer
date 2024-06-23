package metadata.repository

import metadata.Metadata
import metadata.dtos.*
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Select

class MetadataRepositoryImpl(override var context: DSLContext) : MetadataRepository {

    override fun getTables(): List<MetaTable> {
        return context
            .select()
            .from(Metadata.META_TABLE)
            .fetch()
            .into(MetaTable::class.java)
    }

    override fun getMetaDimFacts(): List<MetaDimFact> {
        return context
            .select()
            .from(Metadata.META_DIM_FACT)
            .fetch()
            .into(MetaDimFact::class.java)
    }

    override fun getMetaTableAttributes(): List<MetaTableAttribute> {
        return context
            .select()
            .from(Metadata.META_TABLE_ATTRIBUTE)
            .fetch()
            .into(MetaTableAttribute::class.java)
    }

    override fun getMetaTableAttributeAgrFunctions(): List<MetaTableAttributeAgrFun> {
        return context
            .select()
            .from(Metadata.META_TABLE_ATTRIBUTE_AGR_FUN)
            .fetch()
            .into(MetaTableAttributeAgrFun::class.java)
    }

    override fun getMetaTableTypes(): List<MetaTableType> {
        return context
            .select()
            .from(Metadata.META_TABLE_TYPE)
            .fetch()
            .into(MetaTableType::class.java)
    }

    override fun from(name: String): Select<Record> {
        return context
            .select()
            .from(name)
    }
}