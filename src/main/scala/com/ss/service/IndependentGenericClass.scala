package main.scala.com.ss.service

import com.ss.kafka.{KafkaConsumerConfig, KafkaProducerConfig}
import com.ss.spark.SparkConfiguration

class IndependentGenericClass extends SparkConfiguration
  with KafkaProducerConfig
  with KafkaConsumerConfig
  with Serializable {
  //temp code to read lookup Dataframe

  def genericMethod1 () = {



  }

}
