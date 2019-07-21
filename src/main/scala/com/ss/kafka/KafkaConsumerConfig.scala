package com.ss.kafka

import org.apache.spark.sql.DataFrame
import com.ss.spark.SparkConfiguration
import scala.util.Try

trait KafkaConsumerConfig extends KafkaParams with SparkConfiguration {


  def readEmployeeInputDF: Try[DataFrame] = {
    val inputDF = spark
      .readStream
      .format("kafka")
      .options(kafkaEmployeeConsumerOptionsForTopic1)
      .load()

    println("reading inputDF from kafka")
    Try(inputDF)
  }

  def readNotificationInputDF: Try[DataFrame] = {
    val inputDF =
      spark
        .readStream
        .format("kafka")
        .options(kafkaNotificationConsumerOptionsForTopic2)
        .load()
    println("reading NotificationDF from kafka")
    Try(inputDF)

  }


}
