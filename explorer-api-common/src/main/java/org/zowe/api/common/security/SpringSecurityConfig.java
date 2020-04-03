/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2018, 2020
 */
package org.zowe.api.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    CustomAuthenticationProvider authenticationProvider;
    
    @Autowired
    private AuthenticationEntryPoint authEntryPoint;

    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO - re-enable csrf?
        http.csrf().disable().authorizeRequests()
            .antMatchers(AUTH_WHITELIST).permitAll()
            .antMatchers("/api/v2/*").permitAll()
            .antMatchers("/api/v1/*").authenticated().and().httpBasic().authenticationEntryPoint(authEntryPoint);
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }
}