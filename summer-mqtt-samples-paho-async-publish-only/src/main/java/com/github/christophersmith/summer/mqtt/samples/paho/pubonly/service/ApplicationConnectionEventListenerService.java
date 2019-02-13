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
package com.github.christophersmith.summer.mqtt.samples.paho.pubonly.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectedEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectionFailureEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectionLostEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientDisconnectedEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttConnectionStatusEvent;

@Service
public class ApplicationConnectionEventListenerService
    implements ApplicationListener<MqttConnectionStatusEvent>
{
    private static final Logger LOG = LoggerFactory
        .getLogger(ApplicationConnectionEventListenerService.class);

    @Override
    @Async
    public void onApplicationEvent(MqttConnectionStatusEvent event)
    {
        if (event instanceof MqttClientConnectedEvent)
        {
            MqttClientConnectedEvent instance = (MqttClientConnectedEvent) event;
            LOG.info(String.format("Connected Event - Client ID: %s, Broker: %s, Topics: [%s]",
                instance.getClientId(), instance.getServerUri(),
                StringUtils.arrayToCommaDelimitedString(instance.getSubscribedTopics())));
        }
        if (event instanceof MqttClientConnectionLostEvent)
        {
            MqttClientConnectionLostEvent instance = (MqttClientConnectionLostEvent) event;
            LOG.info(String.format("Connection Lost Event - Client ID: %s, Auto Reconnect: %s",
                instance.getClientId(), String.valueOf(instance.isAutoReconnect())));
        }
        if (event instanceof MqttClientDisconnectedEvent)
        {
            MqttClientDisconnectedEvent instance = (MqttClientDisconnectedEvent) event;
            LOG.info(String.format("Disconnected Event - Client ID: %s", instance.getClientId()));
        }
        if (event instanceof MqttClientConnectionFailureEvent)
        {
            MqttClientConnectionFailureEvent instance = (MqttClientConnectionFailureEvent) event;
            LOG.warn(
                String.format("Connection Failure Event - Client ID: %s, Auto Reconnect: %s",
                    instance.getClientId(), String.valueOf(instance.isAutoReconnect())),
                instance.getThrowable());
        }
    }
}
