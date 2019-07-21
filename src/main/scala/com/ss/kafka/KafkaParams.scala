package com.ss.kafka

trait KafkaParams {

  val kafkaNotificationConsumerOptionsForTopic2 = Map("kafka.bootstrap.servers" -> "localhost:9092",
    "subscribe" -> "notificationTopic",
    "key.deserializer" -> "classOf[StringDeserializer]",
    "value.deserializer" -> "classOf[StringDeserializer]",
    //"kafkaConsumer.pollTimeoutMs" -> "5000",
    "startingOffsets" -> "earliest", //latest
    "failOnDataLoss" -> "false")


  val kafkaEmployeeConsumerOptionsForTopic1 = Map("kafka.bootstrap.servers" -> "localhost:9092",
    "subscribe" -> "employeeTopic",
    "key.deserializer" -> "classOf[StringDeserializer]",
    "value.deserializer" -> "classOf[StringDeserializer]",
    //"kafkaConsumer.pollTimeoutMs" -> "5000",
    "startingOffsets" -> "earliest", //latest
    "failOnDataLoss" -> "false")

  val kafkaEnrichedTopicProducerOptions = Map("kafka.bootstrap.servers" -> "localhost:9092",
    "topic" -> "enrichedOutputDummyTopic")
  val kafkaFilteredTopicProducerOptions = Map("kafka.bootstrap.servers" -> "localhost:9092",
    "topic" -> "filteredOutputDummyTopic")

}
