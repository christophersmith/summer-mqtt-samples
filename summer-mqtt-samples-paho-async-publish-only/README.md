# Summer MQTT Paho Async Publish Only Client Sample Application

This sample demonstrates how the Summer MQTT Paho Async Client can be used for Publishing only to a MQTT Broker. Also demonstrated is the
use of Event Listeners for showing when a message is Published and Delivered.

## Getting Started

This is a runnable Spring Boot console application. The best way to get started is to dig into the code located in the [ApplicationConfiguration](src/main/java/com/github/christophersmith/summer/mqtt/samples/paho/pubonly/configuration/ApplicationConfiguration.java) class.

You'll likely need to modify the following connection parameters for the Paho Client to match your environment.

```java
PahoAsyncMqttClientService service = new PahoAsyncMqttClientService("tcp://localhost:1883", "TESTPUBONLY", MqttClientConnectionType.PUBLISHER, null);
```

Upon running, and a successful connection, this sample will publish:

1. A Connection Status message when connected and disconnected
1. A Atomic Integer message every 2 seconds, using a QoS of 1
