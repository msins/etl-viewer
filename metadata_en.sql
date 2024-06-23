-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE MetaTableType (
    idTableType   TINYINT  CONSTRAINT pkMetaTableType PRIMARY KEY 
  , tableTypeName CHAR(30) NOT NULL  
)
INSERT INTO MetaTableType VALUES (1, 'Fact table');
INSERT INTO MetaTableType VALUES (2, 'Dimension table');

CREATE TABLE MetaTable (
    idTable          INTEGER IDENTITY (100, 1) CONSTRAINT pkMetaTable PRIMARY KEY
  , tableName        CHAR(100) NOT NULL
  , tableSQLName     CHAR(100) NOT NULL   
  , idTableType      TINYINT   NOT NULL
)

ALTER TABLE MetaTable ADD CONSTRAINT fkMetaTableMetaTableType FOREIGN KEY (idTableType) REFERENCES MetaTableType (idTableType)



CREATE TABLE MetaAttrType (
    idAttrType     TINYINT CONSTRAINT pkMetaAttrType PRIMARY KEY
  , attrTypeName   CHAR(40) NOT NULL  
)
INSERT INTO MetaAttrType VALUES (1, 'Measure')
INSERT INTO MetaAttrType VALUES (2, 'Dimension attribute')
INSERT INTO MetaAttrType VALUES (3, 'Foreign key')

CREATE TABLE MetaAgrFun (
    idAgrFun   TINYINT PRIMARY KEY
  , agrFunName CHAR(6) NOT NULL  
)
INSERT INTO MetaAgrFun VALUES (1, 'SUM')
INSERT INTO MetaAgrFun VALUES (2, 'COUNT')
INSERT INTO MetaAgrFun VALUES (3, 'AVG')
INSERT INTO MetaAgrFun VALUES (4, 'MIN')
INSERT INTO MetaAgrFun VALUES (5, 'MAX')


CREATE TABLE MetaTableAttribute (
    idTable        INTEGER  CONSTRAINT fkMetaTableAttributeTable REFERENCES MetaTable(idTable)
  , attrOrdinal    TINYINT  NOT NULL  
  , attrSQLName    CHAR(50) NOT NULL  
  , idAttrType     TINYINT CONSTRAINT fkMetaTableAttributeMetaAttrType REFERENCES MetaAttrType(idAttrType)  
  , attrName       CHAR(50) NOT NULL
  , CONSTRAINT pkMetaTableAttribute  PRIMARY KEY (idTable, attrOrdinal)
)

CREATE TABLE MetaDimFact (
    idFactTable     INTEGER
  , idDimTable      INTEGER
  , factAttrOrdinal TINYINT  NOT NULL  
  , dimAttrOrdinal  TINYINT  NOT NULL  
  , CONSTRAINT pkMetaDimFact PRIMARY KEY (idFactTable, idDimTable, factAttrOrdinal, dimAttrOrdinal) 
  , CONSTRAINT fkMetaDimFactMetaTableAttribute1 FOREIGN KEY (idFactTable, factAttrOrdinal) REFERENCES MetaTableAttribute(idTable, attrOrdinal)
  , CONSTRAINT fkMetaDimFactMetaTableAttribute2 FOREIGN KEY (idDimTable, dimAttrOrdinal)   REFERENCES MetaTableAttribute(idTable, attrOrdinal)
)

CREATE TABLE MetaTableAttributeAgrFun (
    idTable         INTEGER  
  , attrOrdinal     TINYINT  
  , idAgrFun        TINYINT  CONSTRAINT fkMetaTableAttributeAgrFunMetaAgrFun REFERENCES MetaAgrFun(idAgrFun)
  , attrName        CHAR(50) NOT NULL
  , CONSTRAINT pkMetaTableAttributeAgrFun PRIMARY KEY (idTable, attrOrdinal, idAgrFun)
  , CONSTRAINT fkMetaTableAttributeAgrFunMetaTableAttribute FOREIGN KEY (idTable, attrOrdinal) REFERENCES MetaTableAttribute(idTable, attrOrdinal)  
)


------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------
--    Loading the above tables with test data (based on the FKs and table names in your data warehouse)
------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------

INSERT INTO MetaTable  (tableName, tableSQLName, idTableType) 
SELECT name, name, 1
  FROM sysobjects
 WHERE xtype = 'u'
   AND name LIKE 'Fact%'


INSERT INTO MetaTable  (tableName, tableSQLName, idTableType) 
SELECT name, name, 2
  FROM sysobjects
 WHERE xtype = 'u'
   AND name LIKE 'Dim%'
   

-- Copies info of all attributes 
-- Manually change few attributes to test name <> SQLname


--dim atr:
INSERT INTO MetaTableAttribute  
SELECT (SELECT idTable FROM MetaTable WHERE tableSQLName =  t.name)
     , c.colid
     , c.name
     , 2
     , c.name
     
  FROM sysobjects t, syscolumns c
  WHERE t.id = c.id
    AND t.xtype = 'u'
    AND t.name LIKE 'Dim%'
  ORDER BY 1, 2
  

-- Copies the details of all the fact table attributes
-- Change a name to test name  <> SQLname
-- Measures and other fact TABLE attributes:
-- queries will correctly fill tabAtribut table if you follow some attribute naming convention
-- eg. dimension keys and foreign keys fact tables contain a pattern ('%sif%' or '%id%' or '%key%', ...)

  


INSERT INTO MetaTableAttribute  

SELECT (SELECT idTable FROM MetaTable WHERE tableSQLName =  t.name)
     , c.colid
     , c.name
     , 1  --mjere
     , c.name
     
  FROM sysobjects t, syscolumns c
 WHERE t.id = c.id
   AND t.xtype = 'u'
   AND t.name LIKE 'Fact%'
   AND c.name NOT LIKE '%Key%'

UNION 

SELECT (SELECT idTable FROM MetaTable WHERE tableSQLName =  t.name)
     , c.colid
     , c.name
     , 3 --strani ključevi prema dimenzijskim tablicama
     , c.name
     
  FROM sysobjects t, syscolumns c
 WHERE t.id = c.id
   AND t.xtype = 'u'
   AND t.name LIKE 'Fact%'
   AND c.name LIKE '%Key%'

 ORDER BY 1, 2

  


-- Using FKs to define star joins
INSERT INTO MetaDimFact
  
  SELECT (SELECT idTable FROM MetaTable WHERE tableSQLName =  t1.name)
       , (SELECT idTable FROM MetaTable WHERE tableSQLName =  t2.name)
      , (SELECT attrOrdinal FROM MetaTableAttribute, MetaTable
          WHERE MetaTableAttribute.idTable = MetaTable.idTable
            AND MetaTable.tableSQLName = t1.name
            AND UPPER(MetaTableAttribute.attrSQLName) = UPPER(c1.name))
      , (SELECT attrOrdinal FROM MetaTableAttribute, MetaTable
          WHERE MetaTableAttribute.idTable = MetaTable.idTable
            AND MetaTable.tableSQLName = t2.name
            AND UPPER(MetaTableAttribute.attrSQLName) = UPPER(c2.name))            

      
  FROM sys.sysforeignkeys fk, sysobjects t1, sysobjects t2, syscolumns c1, syscolumns c2

 WHERE fk.fkeyid = t1.id
   AND fk.rkeyid = t2.id
   AND fk.fkey   = c1.colid
   AND t1.id = c1.id
   AND fk.rkey   = c2.colid
   AND t2.id = c2.id
   AND t1.name LIKE 'Fact%'

 ORDER BY 1, 2
 
 
-- For each measure we define SUM and AVG
-- Of course, some combinations can be meaningless and should be manually corrected.
  
INSERT INTO MetaTableAttributeAgrFun 

SELECT idTable
     , attrOrdinal
     , 1 -- sum
     , 'Sum of ' + attrName     
  FROM MetaTableAttribute
 WHERE idAttrType = 1
 ORDER BY 1, 2
 
 
INSERT INTO MetaTableAttributeAgrFun
  
SELECT idTable
     , attrOrdinal
     , 3 -- avg
     , 'Avg of ' + attrName     
  FROM MetaTableAttribute
 WHERE idAttrType = 1
 ORDER BY 1, 2
  

--------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------
--
--             App queries (i.e. how to fill the fact table menu and dim/measure tree?)
-- 
--------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------


 
-- returns the fact tables:
SELECT * 
  FROM MetaTable 
 WHERE idTableType = 1 
  
-- returns measures:  
 
 SELECT * 
  FROM MetaTableAttribute, MetaAgrFun, MetaTable, MetaTableAttributeAgrFun                                          
 WHERE MetaTableAttribute.idTable =  100 -- Replace withs e.g. <<this.RadioButtonListFacts.SelectedItem.Value>>
   AND MetaTableAttribute.idTable = MetaTable.idTable 
   AND MetaTableAttribute.idTable  = MetaTableAttributeAgrFun.idTable 
   AND MetaTableAttribute.attrOrdinal  = MetaTableAttributeAgrFun.attrOrdinal 
   AND MetaTableAttributeAgrFun.idAgrFun = MetaAgrFun.idAgrFun 
   AND MetaTableAttribute.idAttrType = 1
 ORDER BY MetaTableAttribute.attrOrdinal
 
 
 -- return dimensions and attributes: 
 

SELECT   dimTable.tableName
       , dimTable.tableSQLName AS dimTableSqlName
       , factTable.tableSQLName AS factTableSqlName
       , factTableAttr.attrSQLName AS factFK 
       , dimTableAttr.attrSQLName AS dimPK
       , MetaTableAttribute.*
  FROM MetaTableAttribute, MetaDimFact
     , MetaTable dimTable, MetaTable factTable 
     , MetaTableAttribute factTableAttr, MetaTableAttribute dimTableAttr
 WHERE MetaDimFact.idDimTable  = dimTable.idTable
   AND MetaDimFact.idFactTable = factTable.idTable
   
   AND MetaDimFact.idFactTable = factTableAttr.idTable
   AND MetaDimFact.factAttrOrdinal = factTableAttr.attrOrdinal
   
   AND MetaDimFact.idDimTable = dimTableAttr.idTable
   AND MetaDimFact.dimAttrOrdinal = dimTableAttr.attrOrdinal

   AND MetaTableAttribute.idTable  = MetaDimFact.idDimTable
   AND idFactTable = 100 --  Replace 
   AND MetaTableAttribute.idAttrType = 2
 ORDER BY dimTable.tableName, attrOrdinal
 
 
 

  --- DROP TABLES:
DROP  TABLE MetaDimFact
DROP  TABLE MetaTableAttributeAgrFun 
DROP  TABLE MetaTableAttribute
DROP  TABLE MetaTable
DROP  TABLE MetaTableType
DROP  TABLE MetaAttrType
DROP  TABLE MetaAgrFun
 