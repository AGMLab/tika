/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.config;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * Bundle activator that adjust the class loading mechanism of the
 * {@link ServiceLoader} class to work correctly in an OSGi environment.
 * <p>
 * Note that you should <strong>not</strong> access this class directly.
 * Instead the OSGi environment (if present) will automatically invoke the
 * methods of this class based on the Bundle-Activator setting in the bundle
 * manifest.
 *
 * @since Apache Tika 0.9
 */
public class TikaActivator
        implements BundleActivator, ServiceListener {

    private BundleContext bundleContext;

    //-----------------------------------------------------< BundleActivator >

    public void start(BundleContext context) throws Exception {
        bundleContext = context;
        bundleContext.addServiceListener(this,
                "(|(objectClass=org.apache.tika.detect.Detector)"
                + "(objectClass=org.apache.tika.parser.Parser))");
    }

    public void stop(BundleContext context) throws Exception {
        bundleContext.removeServiceListener(this);
    }

    //-----------------------------------------------------< ServiceListener >

    public synchronized void serviceChanged(ServiceEvent event) {
        if (event.getType() == ServiceEvent.REGISTERED) {
            ServiceReference reference = event.getServiceReference();
            Object service = bundleContext.getService(reference);
            ServiceLoader.addService(reference, service);
        } else if (event.getType() == ServiceEvent.UNREGISTERING) {
            ServiceReference reference = event.getServiceReference();
            ServiceLoader.removeService(reference);
            bundleContext.ungetService(reference);
        }
    }

}
