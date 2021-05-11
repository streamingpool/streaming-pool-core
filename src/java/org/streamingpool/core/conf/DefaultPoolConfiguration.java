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

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.concurrent.*;

import static java.util.Arrays.stream;
import static org.streamingpool.core.conf.TestPoolConfiguration.STREAMINGPOOL_TEST_SCHEDULER;

@Configuration
public class DefaultPoolConfiguration {

    public static final String STREAMINGPOOL_THREAD_POOL_SIZE = "streamingpool.threadPoolSize";
    public static final String STREAMINGPOOL_THREAD_POOL_KEEP_ALIVE_SECONDS = "streamingpool.threadPoolKeepAliveSeconds";
    public static final String STREAMINGPOOL_THREAD_POOL_USE_DAEMON_THREADS = "streamingpool.threadPoolUseDaemonThreads";
    public static final String STREAMINGPOOL_OBSERVE_ON_CAPACITY = "streamingpool.observeOnCapacity";

    @Value("${" + STREAMINGPOOL_THREAD_POOL_SIZE + ":100}")
    private int threadPoolSize;

    @Value("${" + STREAMINGPOOL_OBSERVE_ON_CAPACITY + ":128}")
    private int observeOnCapacity;

    @Value("${" + STREAMINGPOOL_THREAD_POOL_KEEP_ALIVE_SECONDS + ":60}")
    private long keepAliveTimeSeconds;

    @Value("${" + STREAMINGPOOL_THREAD_POOL_USE_DAEMON_THREADS + ":true}")
    private boolean useDaemonThreads;

    @Bean(destroyMethod = "shutdown")
    @Conditional(NoTestSchedulerPresent.class)
    public ExecutorService localPoolExecutorService() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setDaemon(useDaemonThreads)
                .setNameFormat("streamingpool-thread-%d")
                .build();
        return new ThreadPoolExecutor(threadPoolSize, threadPoolSize,
                keepAliveTimeSeconds, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), threadFactory);
    }

    @Bean
    @Conditional(NoTestSchedulerPresent.class)
    public PoolConfiguration localPoolConfiguration(ExecutorService localPoolExecutorService) {
        return new PoolConfiguration(Schedulers.from(localPoolExecutorService), observeOnCapacity);
    }

    private static class NoTestSchedulerPresent implements Condition {
        @Override
        public boolean matches(ConditionContext context,
                               AnnotatedTypeMetadata metadata) {
            Environment env = context.getEnvironment();
            return stream(env.getActiveProfiles()).noneMatch(STREAMINGPOOL_TEST_SCHEDULER::equals);
        }
    }

}
