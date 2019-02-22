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
package com.github.christophersmith.summer.mqtt.samples.paho.pubsub.service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.christophersmith.summer.mqtt.core.service.ReconnectService;

public class ApplicationReconnectService implements ReconnectService
{
    private static final int MAXIMUM_WAIT_TIME_MINUTES = 20;
    private AtomicInteger    minuteCounter             = new AtomicInteger(1);

    @Override
    public void connected(boolean successful)
    {
        if (successful)
        {
            minuteCounter.set(1);
        }
        else
        {
            minuteCounter.incrementAndGet();
        }
    }

    @Override
    public Date getNextReconnectionDate()
    {
        int minutesToWait = minuteCounter.get();
        if (minutesToWait > MAXIMUM_WAIT_TIME_MINUTES)
        {
            minutesToWait = MAXIMUM_WAIT_TIME_MINUTES;
        }
        return Date.from(ZonedDateTime.now().plusMinutes(minutesToWait).toInstant());
    }
}
