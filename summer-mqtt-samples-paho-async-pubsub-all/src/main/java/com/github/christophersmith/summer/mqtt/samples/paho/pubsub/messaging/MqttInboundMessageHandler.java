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
package com.github.christophersmith.summer.mqtt.samples.paho.pubsub.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.github.christophersmith.summer.mqtt.core.util.MqttHeaderHelper;

@Component
public class MqttInboundMessageHandler implements MessageHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(MqttInboundMessageHandler.class);

    @Override
    public void handleMessage(Message<?> message) throws MessagingException
    {
        if (message.getPayload() != null)
        {
            String topic = MqttHeaderHelper.getTopicHeaderValue(message);
            String payload = null;
            if (message.getPayload() != null
                && message.getPayload() instanceof byte[])
            {
                payload = new String((byte[]) message.getPayload());
            }
            else if (message.getPayload() != null
                && message.getPayload() instanceof String)
            {
                payload = (String) message.getPayload();
            }
            LOG.info(
                String.format("MQTT Message Received - Topic: %s, Payload: %s", topic, payload));
        }
    }
}
