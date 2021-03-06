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
package org.streamingpool.core.service.streamid;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.reactivestreams.Publisher;
import org.streamingpool.core.service.StreamId;

/**
 * Generic implementation of {@link StreamId} which in conjunction with the
 * {@link org.streamingpool.core.service.streamfactory.CompositionStreamFactory} allows for the easy creation of
 * general purpose streams based on composition of streams. This class is experimental.
 *
 * @param <X> The type of objects emitted by the source {@link org.reactivestreams.Publisher}s.
 * @param <T> The type of objects emitted by the new created {@link org.reactivestreams.Publisher}.
 * @author timartin
 */
@Deprecated
public final class CompositionStreamId<X, T> implements StreamId<T>, Serializable {
    private static final long serialVersionUID = 1L;

    private final List<StreamId<X>> sourceStreamIds;
    private final Function<List<Publisher<X>>, Publisher<T>> transformation;

    /**
     * Creates a {@link CompositionStreamId} with the provided sourceStreamId and function.
     *
     * @param sourceStreamId A {@link StreamId} that identifies the {@link org.reactivestreams.Publisher} passed to the
     *                       transformation function.
     * @param transformation The transformation {@link Function} to be used on the {@link org.reactivestreams.Publisher} identified by
     *                       the provided {@link StreamId}.
     */
    public CompositionStreamId(StreamId<X> sourceStreamId, Function<List<Publisher<X>>, Publisher<T>> transformation) {
        this(Collections.singletonList(sourceStreamId), transformation);
    }

    /**
     * Creates a {@link CompositionStreamId} with the provided sourceStreamIds and function.
     *
     * @param sourceStreamIds A {@link List} of {@link StreamId}s that will identifies the {@link org.reactivestreams.Publisher} passed
     *                        to the transformation function.
     * @param transformation  The transformation {@link Function} to be used on the {@link org.reactivestreams.Publisher}s identified by
     *                        the provided {@link List} of {@link StreamId}s.
     */
    public CompositionStreamId(List<StreamId<X>> sourceStreamIds,
                               Function<List<Publisher<X>>, Publisher<T>> transformation) {
        this.sourceStreamIds = sourceStreamIds;
        this.transformation = transformation;
    }

    public List<StreamId<X>> sourceStreamIds() {
        return sourceStreamIds;
    }

    public Function<List<Publisher<X>>, Publisher<T>> transformation() {
        return transformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompositionStreamId<?, ?> that = (CompositionStreamId<?, ?>) o;

        if (sourceStreamIds != null ? !sourceStreamIds.equals(that.sourceStreamIds) : that.sourceStreamIds != null)
            return false;
        return transformation != null ? transformation.equals(that.transformation) : that.transformation == null;

    }

    @Override
    public int hashCode() {
        int result = sourceStreamIds != null ? sourceStreamIds.hashCode() : 0;
        result = 31 * result + (transformation != null ? transformation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CompositionStreamId{" +
                "sourceStreamIds=" + sourceStreamIds +
                ", transformation=" + transformation +
                '}';
    }
}
