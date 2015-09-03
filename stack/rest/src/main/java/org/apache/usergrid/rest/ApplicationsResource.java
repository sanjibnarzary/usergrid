/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one or more
 *  *  contributor license agreements.  The ASF licenses this file to You
 *  * under the Apache License, Version 2.0 (the "License"); you may not
 *  * use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.  For additional information regarding
 *  * copyright in this work, please see the NOTICE file in the top level
 *  * directory of this distribution.
 *
 */
package org.apache.usergrid.rest;

import com.sun.jersey.api.json.JSONWithPadding;
import org.apache.usergrid.rest.security.annotations.RequireSystemAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classy class class.
 */
@Component
@Scope( "singleton" )
@Produces( {
    MediaType.APPLICATION_JSON, "application/javascript", "application/x-javascript", "text/ecmascript",
    "application/ecmascript", "text/jscript"
} )
public class ApplicationsResource extends AbstractContextResource {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationsResource.class);


    public ApplicationsResource() {

        logger.info( "ApplicationsResource initialized" );
    } {

    }

    @RequireSystemAccess
    @DELETE
    @Path( "{applicationId}" )
    public JSONWithPadding clearApplication( @Context UriInfo ui,
                                             @PathParam("applicationId") UUID applicationId,
                                             @QueryParam( "confirmApplicationId" ) UUID confirmApplicationId,
                                             @QueryParam( "callback" ) @DefaultValue( "callback" ) String callback )
        throws Exception {

        if(confirmApplicationId == null || !confirmApplicationId.equals(applicationId)){
            throw new IllegalArgumentException("please make confirmApplicationId equal to applicationId");
        }

        ApiResponse response = createApiResponse();
        response.setAction( "clear application" );

        logger.info( "clearing up application" );
        AtomicInteger itemsDeleted = new AtomicInteger(0);
        try {
            management.deleteAllEntities(applicationId)
                .count()
                .doOnNext(count -> itemsDeleted.set(count))
                .toBlocking().lastOrDefault(0);
        }
        catch ( Exception e ) {
            logger.error( "Unable to delete all items, deleted: " + itemsDeleted.get(), e );
        }
        Map<String,Object> data = new HashMap<>();
        data.put("count",itemsDeleted.get());
        response.setData(data);
        response.setSuccess();
        return new JSONWithPadding( response, callback );
    }

}
