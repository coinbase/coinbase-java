/*
 * Copyright 2018 Coinbase, Inc.
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
 */

package com.coinbase.resources.base;

/**
 * Base class for resources with id, resource and resource_path fields.
 */
public abstract class BaseResource {

    private String id;
    private String resource;
    private String resourcePath;

    public void setId(String id) {
        this.id = id;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * Gets resource id.
     *
     * @return resource id.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets type of resource model.
     *
     * @return type of resource model.
     */
    public String getResource() {
        return resource;
    }

    /**
     * Gets the URI path to this resource.
     *
     * @return URI path to this resource.
     */
    public String getResourcePath() {
        return resourcePath;
    }
}