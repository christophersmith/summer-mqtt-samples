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
package com.github.christophersmith.summer.mqtt.samples.paho.pubsub.domain;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.christophersmith.summer.mqtt.samples.paho.pubsub.util.CustomZonedDateTimeSerializer;

@JsonInclude(content = Include.NON_NULL)
public class ConnectionStatusMessage
{
    private String        clientId;
    private String        connectionType;
    private String        connectionStatus;
    @JsonSerialize(using = CustomZonedDateTimeSerializer.class)
    private ZonedDateTime eventDateTime;

    public ConnectionStatusMessage()
    {
        super();
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getConnectionType()
    {
        return connectionType;
    }

    public void setConnectionType(String connectionType)
    {
        this.connectionType = connectionType;
    }

    public String getConnectionStatus()
    {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus)
    {
        this.connectionStatus = connectionStatus;
    }

    public ZonedDateTime getEventDateTime()
    {
        return eventDateTime;
    }

    public void setEventDateTime(ZonedDateTime eventDateTime)
    {
        this.eventDateTime = eventDateTime;
    }
}
