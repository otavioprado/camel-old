/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.dynamicrouter;

import java.util.function.Consumer;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.dynamicrouter.control.DynamicRouterControlComponent;
import org.apache.camel.component.dynamicrouter.routing.DynamicRouterComponent;

import static org.apache.camel.component.dynamicrouter.control.DynamicRouterControlConstants.COMPONENT_SCHEME_CONTROL;
import static org.apache.camel.component.dynamicrouter.routing.DynamicRouterConstants.COMPONENT_SCHEME_ROUTING;

public abstract class DynamicRouterTestConstants {

    public static final Consumer<CamelContext> addRoutes = ctx -> {
        try {
            ctx.addComponent(COMPONENT_SCHEME_CONTROL, new DynamicRouterControlComponent());
            ctx.addComponent(COMPONENT_SCHEME_ROUTING, new DynamicRouterComponent());
            ctx.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:start")
                            .routeId("directToDynamicRouter")
                            .toD("dynamic-router://test?synchronous=true");
                    from("direct:list")
                            .routeId("directToControlList")
                            .toD("dynamic-router-control://list" +
                                 "?subscribeChannel=${header.subscribeChannel}");
                    from("direct://subscribe")
                            .routeId("subscribeRoute")
                            .toD("dynamic-router-control://subscribe" +
                                 "?subscribeChannel=${header.subscribeChannel}" +
                                 "&subscriptionId=${header.subscriptionId}" +
                                 "&destinationUri=${header.destinationUri}" +
                                 "&priority=${header.priority}" +
                                 "&predicateBean=${header.predicateBean}");
                    from("direct://subscribe-bean-expression")
                            .routeId("subscribeRouteBeanExpression")
                            .toD("dynamic-router-control://subscribe" +
                                 "?subscribeChannel=${header.subscribeChannel}" +
                                 "&subscriptionId=${header.subscriptionId}" +
                                 "&destinationUri=${header.destinationUri}" +
                                 "&priority=${header.priority}" +
                                 "&predicate=${header.predicate}");
                    from("direct://subscribe-no-url-predicate")
                            .routeId("subscribeRouteNoUrlPredicate")
                            .toD("dynamic-router-control://subscribe" +
                                 "?subscribeChannel=${header.subscribeChannel}" +
                                 "&subscriptionId=${header.subscriptionId}" +
                                 "&destinationUri=${header.destinationUri}" +
                                 "&priority=${header.priority}");
                    from("direct://unsubscribe")
                            .routeId("unsubscribeRoute")
                            .toD("dynamic-router-control://unsubscribe" +
                                 "?subscribeChannel=${header.subscribeChannel}" +
                                 "&subscriptionId=${header.subscriptionId}");
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ctx.start();
    };
}
