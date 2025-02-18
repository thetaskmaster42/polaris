/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.polaris.service.context;

import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Map;
import org.apache.polaris.core.context.RealmId;

@ApplicationScoped
@Identifier("default")
public class DefaultRealmIdResolver implements RealmIdResolver {

  private final RealmContextConfiguration configuration;

  @Inject
  public DefaultRealmIdResolver(RealmContextConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public RealmId resolveRealmContext(
      String requestURL, String method, String path, Map<String, String> headers) {

    String realm;

    if (headers.containsKey(configuration.headerName())) {
      realm = headers.get(configuration.headerName());
      if (!configuration.realms().contains(realm)) {
        throw new IllegalArgumentException("Unknown realm: " + realm);
      }
    } else {
      realm = configuration.defaultRealm();
    }

    return RealmId.newRealmId(realm);
  }
}
