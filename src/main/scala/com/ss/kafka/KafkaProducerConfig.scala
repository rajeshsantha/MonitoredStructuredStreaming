package com.ss.kafka

import com.ss.spark.SparkConfiguration
import org.apache.spark.SparkException
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.streaming.{OutputMode, Trigger}

trait KafkaProducerConfig extends KafkaParams with SparkConfiguration {


  def writeEmployeeFilteredEventDFToKafka (managed: DataFrame, unmanaged: DataFrame): Unit = {
    try {
      println("Inside writeEmployeeFilteredEventDFToKafka ")

      val resolvedEmployeeStream = managed
        .writeStream
        .format("kafka")
        //.format("console")
        .options(kafkaEnrichedTopicProducerOptions)
        .option("checkpointLocation", "/app/spark/apps/resolvedEmployeeStream/checkpoints/enrichment")
        .trigger(Trigger.ProcessingTime("15 seconds"))
        .queryName("resolvedEmployeeStreamQuery")
        .outputMode(OutputMode.Update())
        .start()

      val unresolvedEmployeeStream = unmanaged
        .writeStream
        .format("kafka")
        //.format("console")
        .options(kafkaFilteredTopicProducerOptions)
        .option("checkpointLocation", "/app/spark/apps/unresolvedEmployeeStream/checkpoints/unmanaged")
        .trigger(Trigger.ProcessingTime("15 seconds"))
        .queryName("unresolvedEmployeeStreamQuery")
        .outputMode(OutputMode.Update())
        .start()
      println("both streams started")

      resolvedEmployeeStream.awaitTermination()
      unresolvedEmployeeStream.awaitTermination()

    } catch {
      case ex: SparkException =>
        println("Streaming Exception while writing Employee Event to Kafka" + ex.getStackTrace)
      case rex: RuntimeException =>
        println("Unknown Exception while writing Employee Event to Kafka" + rex.getStackTrace)


    }
  }


}
