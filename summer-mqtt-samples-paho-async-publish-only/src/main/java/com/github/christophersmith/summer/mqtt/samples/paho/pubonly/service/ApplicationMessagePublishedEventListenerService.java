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
import org.springframework.stereotype.Service;

import com.github.christophersmith.summer.mqtt.core.event.MqttMessageDeliveredEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessagePublishedEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessageStatusEvent;

@Service
public class ApplicationMessagePublishedEventListenerService
    implements ApplicationListener<MqttMessageStatusEvent>
{
    private static final Logger LOG = LoggerFactory
        .getLogger(ApplicationMessagePublishedEventListenerService.class);

    @Override
    public void onApplicationEvent(MqttMessageStatusEvent event)
    {
        if (event instanceof MqttMessageDeliveredEvent)
        {
            MqttMessageDeliveredEvent deliveredEvent = (MqttMessageDeliveredEvent) event;
            LOG.info(String.format("Message Delivered Event - Client ID: %s, Message ID: %s",
                deliveredEvent.getClientId(),
                String.valueOf(deliveredEvent.getMessageIdentifier())));
        }
        else if (event instanceof MqttMessagePublishedEvent)
        {
            MqttMessagePublishedEvent publishedEvent = (MqttMessagePublishedEvent) event;
            LOG.info(String.format(
                "Message Published Event - Client ID: %s, Correlation ID: %s, Message ID: %s",
                event.getClientId(), publishedEvent.getCorrelationId(),
                String.valueOf(publishedEvent.getMessageIdentifier())));
        }
    }
}
