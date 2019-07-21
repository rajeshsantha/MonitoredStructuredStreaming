package com.ss.spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

trait SparkConfiguration {

  val settings = Map("spark.sql.session.timeZone" -> "PST",
    "spark.sql.shuffle.partitions" -> "4",
    "spark.serializer" -> "org.apache.spark.serializer.KryoSerializer")

  private lazy val conf = new SparkConf()
    .setAll(settings)

  implicit lazy val spark = SparkSession
    .builder
    .master("local[*]")
    .config(conf)
    .appName("MonitoredStreamingApp")
    .getOrCreate


}
