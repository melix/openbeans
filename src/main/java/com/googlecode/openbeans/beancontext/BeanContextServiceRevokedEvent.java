/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.googlecode.openbeans.beancontext;

import com.googlecode.openbeans.beancontext.BeanContextEvent;
import com.googlecode.openbeans.beancontext.BeanContextServices;

@SuppressWarnings("unchecked")
public class BeanContextServiceRevokedEvent extends BeanContextEvent {

    private static final long serialVersionUID = -1295543154724961754L;

    /**
     * @serial
     */
    protected Class serviceClass;

    /**
     * @serial
     */
    private boolean invalidateRefs;

    public BeanContextServiceRevokedEvent(BeanContextServices bcs, Class sc,
            boolean invalidate) {

        super(bcs);
        this.serviceClass = sc;
        this.invalidateRefs = invalidate;        
    }

    public Class getServiceClass() {
        return this.serviceClass;
    }

    public BeanContextServices getSourceAsBeanContextServices() {
        return (BeanContextServices) super.getBeanContext();
    }

    public boolean isCurrentServiceInvalidNow() {
        return this.invalidateRefs;
    }

    public boolean isServiceClass(Class service) {        
        return serviceClass.equals(service);
    }
}
