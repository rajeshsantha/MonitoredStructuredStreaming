package com.ss.service.utils

import org.apache.spark.sql.types.{LongType, StringType, StructType}

object Utilities {


  val getIndexJsonSchema = new StructType().add("index_name", StringType).add("action", StringType)

  val getEMPJsonSchema = new StructType()
    .add("Name", StringType)
    .add("EmpID", LongType)
    .add("City", StringType)
    .add("State", StringType)
    .add("DeptID", LongType)

}
