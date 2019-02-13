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
package com.github.christophersmith.summer.mqtt.samples.paho.pubonly.util;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionStatusPublisher;
import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionType;
import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;
import com.github.christophersmith.summer.mqtt.samples.paho.pubonly.domain.ConnectionStatusMessage;

public class ApplicationMqttClientConnectionStatusPublisher
    implements MqttClientConnectionStatusPublisher
{
    private static final Logger       LOG           = LoggerFactory
        .getLogger(ApplicationMqttClientConnectionStatusPublisher.class);
    public static final String        STATUS_TOPIC  = "client/status";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public byte[] getConnectedPayload(String clientId, MqttClientConnectionType connectionType)
    {
        return getConnectionStatusMessageAsBytes(clientId, connectionType.name(), "CONNECTED",
            ZonedDateTime.now());
    }

    @Override
    public byte[] getDisconnectedPayload(String clientId, MqttClientConnectionType connectionType)
    {
        return getConnectionStatusMessageAsBytes(clientId, connectionType.name(), "DISCONNECTED",
            ZonedDateTime.now());
    }

    @Override
    public MqttQualityOfService getStatusMqttQualityOfService()
    {
        return MqttQualityOfService.QOS_0;
    }

    @Override
    public boolean isStatusMessageRetained()
    {
        return true;
    }

    @Override
    public String getStatusTopic()
    {
        return STATUS_TOPIC;
    }

    public static byte[] getConnectionStatusMessageAsBytes(String clientId, String connectionType,
        String connectionStatus, ZonedDateTime eventDateTime)
    {
        byte[] payload = null;
        ConnectionStatusMessage message = new ConnectionStatusMessage();
        message.setClientId(clientId);
        message.setConnectionType(connectionType);
        message.setConnectionStatus(connectionStatus);
        message.setEventDateTime(eventDateTime);
        try
        {
            payload = OBJECT_MAPPER.writeValueAsBytes(message);
        }
        catch (JsonProcessingException ex)
        {
            LOG.error(String.format(
                "Error generating Connection Status Message! Client ID: %s, Connection Type: %s, Connection Status: %s",
                clientId, connectionType, connectionStatus), ex);
        }
        return payload;
    }
}
