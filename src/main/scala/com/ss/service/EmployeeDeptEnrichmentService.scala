package com.ss.service

import com.ss.kafka.{KafkaConsumerConfig, KafkaProducerConfig}
import com.ss.service.utils.Utilities._
import com.ss.spark.SparkConfiguration
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, lit, _}
import org.apache.spark.sql.streaming.{OutputMode, Trigger}

class EmployeeDeptEnrichmentService extends SparkConfiguration
  with KafkaProducerConfig
  with KafkaConsumerConfig
  with Serializable {
  //temp code to read lookup Dataframe

  val file_path_lookup = "D:\\InputfilePaths\\departmentSample.csv"
  val file_path_lookup2 = "D:\\InputfilePaths\\departmentSample2.csv"

  def filterEmployeeDeptAndEnrich = {

    try {
      var lookupDF = getLookupDataFrame(file_path_lookup)
      lookupDF.show(5, false)

      //doSomeOperation(lookupDF)

      val notificationDF_RAW = readNotificationInputDF.get
        .withColumn("cutted", expr("substring(value, 2, length(value)-3)"))
        .drop("value")

      val notificationDF = notificationDF_RAW
        .select(from_json(col("cutted").cast("string"), getIndexJsonSchema).as("finaldata"))
        .select("finaldata.*")

      val query1 = notificationDF.writeStream.foreachBatch {
        (ds: DataFrame, batchId: Long) =>
          ds.show()
          println("inside foreachBatch")
          //re-reading the dataframe
          lookupDF = getLookupDataFrame(file_path_lookup2)

          doSomeOperation(lookupDF)
          println("called doSomeOps in foreachBatch")
      }.trigger(Trigger.ProcessingTime("5 seconds"))
        .queryName("NotificationQuery")
        .outputMode(OutputMode.Update())
        .start()

      query1.awaitTermination()

    } catch {
      case ex: RuntimeException =>
        println("Spark Exception in filterSNMPNotificationAndEnrich processing" + ex.printStackTrace())
    }
  }

  def getLookupDataFrame (file_path_lookup: String) = {
    spark
      .read
      .format("com.databricks.spark.csv")
      .option("header", "true")
      .option("delimiter", ",")
      .load(file_path_lookup)
      .toDF("DeptmentID", "DeptName", "DeptDescription")
  }


  def doSomeOperation (lookup_DF: DataFrame) = {

    val employeeRAWDF = readEmployeeInputDF.get
      .withColumn("cutted", expr("substring(value, 2, length(value)-3)")).drop("value")

    val employeeDF = employeeRAWDF
      .select(from_json(col("cutted").cast("string"), getEMPJsonSchema).as("finaldata")).select("finaldata.*")

    val joinDF = employeeDF
      .join(lookup_DF, employeeDF("DeptID") === lookup_DF("DeptmentID"), "left_outer")

    val finalDF = joinDF
      .withColumn("isMappingFound", when(expr("DeptName is not null"), lit("true")).otherwise(lit("false")))
      .withColumn("resolvedDepartment", when(expr("DeptName is null"), lit("Not Found")).otherwise(col("DeptName")))
      .drop("DeptName", "DeptID")

    val managed_Emp_msg = finalDF.filter(col("isMappingFound") === "true")
    val unmanaged_Emp_msg = finalDF.filter(col("isMappingFound") === "false")
    val managed_EMP_Stream_msg = managed_Emp_msg.selectExpr("to_json(struct(*)) AS value").toDF()
    val unmanaged_EMP_Stream_msg = unmanaged_Emp_msg.selectExpr("to_json(struct(*)) AS value").toDF()

    writeEmployeeFilteredEventDFToKafka(managed_EMP_Stream_msg, unmanaged_EMP_Stream_msg)
  }
}