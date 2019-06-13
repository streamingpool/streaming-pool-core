// @formatter:off
/**
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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.streamingpool.core.service.TypedStreamFactory;
import org.streamingpool.core.service.streamfactory.*;

/**
 * Configuration for including the {@link TypedStreamFactory}s provided in the core project.
 * 
 * @author acalia
 */
@Configuration
@Import({ StreamCreatorFactoryConfiguration.class })
public class DefaultStreamFactories {

    @Bean
    public MergedErrorStreamFactory mergedErrorStreamFactory() {
        return new MergedErrorStreamFactory();
    }

    @Bean
    public CompositionStreamFactory compositionStreamFactory() {
        return new CompositionStreamFactory();
    }

    @Bean
    public CombineWithLatestStreamFactory combineWithLatestStreamIdStreamFactory() {
        return new CombineWithLatestStreamFactory();
    }

    @Bean
    public DelayedStreamFactory delayedStreamIdStreamFactory() {
        return new DelayedStreamFactory();
    }

    @Bean
    public ZippedStreamFactory zippedStreamFactory() {
        return new ZippedStreamFactory();
    }

    @Bean
    public DerivedStreamFactory derivedStreamIdStreamFactory() {
        return new DerivedStreamFactory();
    }

    @Bean
    public OverlapBufferStreamFactory overlapBufferStreamFactory() {
        return new OverlapBufferStreamFactory();
    }

    @Bean
    public FilteredStreamFactory filteredStreamFactory() {
        return new FilteredStreamFactory();
    }

    @Bean
    public  IntervalStreamFactory intervalStreamFactory() {
        return new IntervalStreamFactory();
    }
}
