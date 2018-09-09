# Summer MQTT Paho Async PUB/SUB Client Sample Application

This sample demonstrates how the Summer MQTT Paho Async Client can be used for Publishing and Subscribing to a MQTT Broker. While we attempted to hit on all the common usage scenarios that you may want to use Summer MQTT Paho Async Client for, it's not exhaustive.

## Getting Started

This is a runnable Spring Boot console application. The best way to get started is to dig into the code located in the [ApplicationConfiguration](src/main/java/com/github/christophersmith/summer/mqtt/samples/paho/pubsub/configuration/ApplicationConfiguration.java) class.

You'll likely need to modify the following connection parameters for the Paho Client to match your environment.

```java
PahoAsyncMqttClientService service = new PahoAsyncMqttClientService("tcp://localhost:1883", "TESTPUBSUB", MqttClientConnectionType.PUBSUB, null);
```

Upon running, and a successful connection, this sample will publish:

1. A Connection Status message when connected and disconnected
1. A Atomic Integer message every 2 seconds

Since these messages are published on topics the Paho Client is subscribed to, you'll see the messages being logged once they arrive.
