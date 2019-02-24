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

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;
import com.github.christophersmith.summer.mqtt.core.service.MqttClientService;
import com.github.christophersmith.summer.mqtt.core.util.MqttHeaderHelper;
import com.github.christophersmith.summer.mqtt.samples.paho.pubonly.domain.AtomicIntegerMessage;

@Service
public class MessageSenderService
{
    private static final Logger       LOG           = LoggerFactory
        .getLogger(MessageSenderService.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private AtomicInteger             atomicInteger = new AtomicInteger();
    @Autowired
    @Qualifier("mqttOutboundMessageChannel")
    private MessageChannel            outboundMessageChannel;
    @Autowired
    @Qualifier("mqttClientPubOnlyService")
    private MqttClientService         mqttClientService;

    @Scheduled(initialDelay = 2000, fixedRate = 2000)
    public void sendMessage()
    {
        if (mqttClientService.isStarted())
        {
            String uuid = UUID.randomUUID().toString();
            AtomicIntegerMessage message = new AtomicIntegerMessage();
            message.setUuid(uuid);
            message.setAtomicInteger(atomicInteger.incrementAndGet());
            try
            {
                outboundMessageChannel
                    .send(MessageBuilder.withPayload(OBJECT_MAPPER.writeValueAsString(message))
                        .setHeader(MqttHeaderHelper.TOPIC, "inbound")
                        .setHeader(MqttHeaderHelper.QOS, MqttQualityOfService.QOS_1)
                        .setHeader(MqttHeaderHelper.CORRELATION_ID, uuid).build());
            }
            catch (JsonProcessingException ex)
            {
                LOG.error(String.format(
                    "Error publishing Atomic Integer message! UUID: %s, Atomic Integer: %s",
                    message.getUuid(), String.valueOf(message.getAtomicInteger())), ex);
            }
        }
    }
}
