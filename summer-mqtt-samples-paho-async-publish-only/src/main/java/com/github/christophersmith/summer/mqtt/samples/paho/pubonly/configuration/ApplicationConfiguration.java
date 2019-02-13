/*******************************************************************************
 * Copyright (c) 2019 Christopher Smith - https://github.com/christophersmith
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.christophersmith.summer.mqtt.samples.paho.pubonly.configuration;

import java.util.concurrent.Executor;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionType;
import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;
import com.github.christophersmith.summer.mqtt.core.service.MqttClientService;
import com.github.christophersmith.summer.mqtt.paho.service.PahoAsyncMqttClientService;
import com.github.christophersmith.summer.mqtt.samples.paho.pubonly.service.ApplicationReconnectService;
import com.github.christophersmith.summer.mqtt.samples.paho.pubonly.util.ApplicationMqttClientConnectionStatusPublisher;

@Configuration
@EnableAsync
@EnableScheduling
public class ApplicationConfiguration
{
    private static final int          TASK_EXECUTOR_CORE_POOL_SIZE = 20;
    private static final int          TASK_SCHEDULER_POOL_SIZE     = 5;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /*
     * Create and start a MqttClientService instance using the PahoAsyncMqttClientService.
     */
    @Bean(destroyMethod = "close")
    @Qualifier("mqttClientPubOnlyService")
    public MqttClientService mqttClientPubOnlyService() throws MqttException
    {
        /*
         * Setup the PahoAsyncMqttClientService with a Broker URL and a Client ID. Here we're
         * specifying that the connection will be used for Publishing and Subscribing. We're also
         * not using client-side persistence.
         */
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService("tcp://localhost:1883",
            "TESTPUBSUB", MqttClientConnectionType.PUBLISHER, null);
        /*
         * The default Paho MqttConnectOptions is being used here. We're only setting Clean Session
         * to true and a Will. For other Paho options, please see the Paho documentation.
         */
        service.getMqttConnectOptions().setCleanSession(true);
        service.getMqttConnectOptions().setWill(
            ApplicationMqttClientConnectionStatusPublisher.STATUS_TOPIC,
            ApplicationMqttClientConnectionStatusPublisher.getConnectionStatusMessageAsBytes(
                service.getClientId(), service.getConnectionType().name(), "ERROR", null),
            MqttQualityOfService.QOS_1.getLevelIdentifier(), true);
        /*
         * The application will publish a custom message when the Client connects, and when the
         * Client gracefully disconnects. Our ApplicationMqttClientConnectionStatusPublisher will
         * control the messages and topics.
         */
        service.getMqttClientConfiguration().setMqttClientConnectionStatusPublisher(
            new ApplicationMqttClientConnectionStatusPublisher());
        /*
         * The PahoAsyncMqttClientService instance needs to subscribe to the
         * mqttOutboundMessageChannel so that it receives the messages it is supposed to publish.
         */
        mqttOutboundMessageChannel().subscribe(service);
        /*
         * Set the ApplicationEventPublisher so we can receive MqttStatusEvent broadcasts.
         */
        service.setApplicationEventPublisher(applicationEventPublisher);
        /*
         * We'll use our own custom logic for handling reconnects.
         */
        service.setReconnectDetails(new ApplicationReconnectService(), taskScheduler());
        /*
         * Start up our service!
         */
        service.start();
        return service;
    }

    /*
     * This Message Channel handles messages that should be published. We'll assign it to a Task
     * Executor so publishing from a worker thread is done is asynchronously. Messages pushed to
     * this Message Channel will be published onto the MQTT Broker by the MqttClientService.
     */
    @Bean
    @Qualifier("mqttOutboundMessageChannel")
    public ExecutorSubscribableChannel mqttOutboundMessageChannel()
    {
        return new ExecutorSubscribableChannel(taskExecutor());
    }

    /*
     * The Thread Executor is used to off load in-bound and out-bound messages to a separate thread.
     * This allows the threads that handle in-bound and out-bound traffic to have minimal
     * processing, and be more available to receive and publish messages.
     */
    @Bean
    @Qualifier("taskExecutor")
    public Executor taskExecutor()
    {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(TASK_EXECUTOR_CORE_POOL_SIZE);
        return executor;
    }

    /*
     * Since we'll be handling our own MQTT Client reconnect logic, a Task Scheduler is needed.
     * We'll also use this Task Scheduler to periodically publish some test messages.
     */
    @Bean(destroyMethod = "shutdown")
    @Qualifier("taskScheduler")
    public ThreadPoolTaskScheduler taskScheduler()
    {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(TASK_SCHEDULER_POOL_SIZE);
        return scheduler;
    }
}
