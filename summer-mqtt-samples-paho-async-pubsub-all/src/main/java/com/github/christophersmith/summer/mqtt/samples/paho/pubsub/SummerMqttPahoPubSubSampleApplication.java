/*******************************************************************************
 * Copyright (c) 2018 Christopher Smith - https://github.com/christophersmith
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
package com.github.christophersmith.summer.mqtt.samples.paho.pubsub;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SummerMqttPahoPubSubSampleApplication
{
    public static void main(String[] args) throws InterruptedException
    {
        ApplicationContext applicationContext = SpringApplication
            .run(SummerMqttPahoPubSubSampleApplication.class, args);
        CountDownLatch closeLatch = applicationContext.getBean(CountDownLatch.class);
        closeLatch.await();
    }

    @Bean
    public CountDownLatch closeLatch()
    {
        return new CountDownLatch(1);
    }
}
