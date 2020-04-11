package main.scala.com.ss.service

import com.ss.kafka.{KafkaConsumerConfig, KafkaProducerConfig}
import com.ss.service.utils.Utilities._
import com.ss.spark.SparkConfiguration
import org.apache.spark.sql.streaming.{OutputMode, Trigger}

class IndependentGenericClass extends SparkConfiguration
  with KafkaProducerConfig
  with KafkaConsumerConfig
  with Serializable {
  //temp code to read lookup Dataframe

  def genericMethod1 () = {

    val streamingData = spark
      .readStream
      .schema(retailDataSchema)
      .csv(tempDirForStreaming)

    val filteredData = streamingData.filter("Quantity > 10")


    val query = filteredData.writeStream
      .format("console")
      .queryName("filteredByCountry")
      .outputMode(OutputMode.Update())
      .trigger(Trigger.ProcessingTime("5 seconds"))
      .start()

    query.awaitTermination()


  }

}
