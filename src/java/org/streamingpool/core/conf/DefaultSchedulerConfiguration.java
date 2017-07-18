// @formatter:off
/*
*
* This file is part of streaming pool (http://www.streamingpool.org).
*
* Copyright (c) 2017-present, CERN. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
*/
// @formatter:on

package org.streamingpool.core.conf;

import static java.util.Arrays.stream;
import static org.streamingpool.core.conf.TestSchedulerConfiguration.STREAMINGPOOL_TEST_SCHEDULER;

import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration
public class DefaultSchedulerConfiguration {

    public static final String STREAMINGPOOL_THREAD_POOL_SIZE = "streamingpool.threadPoolSize";


    @Value("${" + STREAMINGPOOL_THREAD_POOL_SIZE + ":100}")
    private int threadPoolSize;

    @Bean
    @Conditional(NoTestSchedulerPresent.class)
    public Scheduler scheduler(){
        return Schedulers.from(Executors.newFixedThreadPool(threadPoolSize));
    }

    private static class NoTestSchedulerPresent implements Condition {
        @Override
        public boolean matches(ConditionContext context,
                AnnotatedTypeMetadata metadata) {
            Environment env = context.getEnvironment();
            return !stream(env.getActiveProfiles()).anyMatch(STREAMINGPOOL_TEST_SCHEDULER::equals);
        }
    }


}
