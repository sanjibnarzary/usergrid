/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.usergrid.persistence.index.guice;

import org.apache.usergrid.persistence.index.IndexFig;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.apache.usergrid.persistence.collection.guice.CollectionModule;
import org.apache.usergrid.persistence.index.EntityCollectionIndex;
import org.apache.usergrid.persistence.index.EntityCollectionIndexFactory;
import org.apache.usergrid.persistence.index.impl.EsEntityCollectionIndex;
import org.safehaus.guicyfig.GuicyFigModule;


public class IndexModule extends AbstractModule {

    @Override
    protected void configure() {

        // configure collections and our core astyanax framework
        install(new CollectionModule());

        // install our configuration
        install (new GuicyFigModule( IndexFig.class ));

        install( new FactoryModuleBuilder()
            .implement( EntityCollectionIndex.class, EsEntityCollectionIndex.class )
            .build( EntityCollectionIndexFactory.class ) );
    }
}
