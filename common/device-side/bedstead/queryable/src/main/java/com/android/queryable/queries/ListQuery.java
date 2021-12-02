/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.queryable.queries;

import com.android.queryable.Queryable;

import java.util.List;
import java.util.Set;

/** Query for a {@link java.util.List}. */
public interface ListQuery<E extends Queryable, F, G extends Query<F>> extends Query<List<F>> {

    static ListQuery<ListQuery<?, ?, ?>, ?, ?> list() {
        return new ListQueryHelper<>();
    }

    IntegerQuery<E> size();

    E contains(G... objects);
    E doesNotContain(G... objects);
}