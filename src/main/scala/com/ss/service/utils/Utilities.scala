package com.ss.service.utils

import org.apache.spark.sql.types._

object Utilities {

  val tempDirForStreaming = "G:\\Datasets\\TempFolderForStreaming"

  val getIndexJsonSchema = new StructType().add("index_name", StringType).add("action", StringType)

  val getEMPJsonSchema = new StructType()
    .add("Name", StringType)
    .add("EmpID", LongType)
    .add("City", StringType)
    .add("State", StringType)
    .add("DeptID", LongType)
  //InvoiceNo	StockCode	Description	Quantity	InvoiceDate	UnitPrice	CustomerID	Country

  val retailDataSchema = new StructType()
    .add("InvoiceNo", IntegerType)
    .add("StockCode", IntegerType)
    .add("Description", StringType)
    .add("Quantity", IntegerType)
    .add("InvoiceDate", TimestampType)
    .add("UnitPrice", DoubleType)
    .add("CustomerId", IntegerType)
    .add("Country", StringType)


}
