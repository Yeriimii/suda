package yelly.suda.global.config;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("#{environment['allowOrigin']}")
    private String allowOrigin;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        /* session 정책 */
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /* cors 설정 */
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
            CorsConfiguration corsConfig = new CorsConfiguration();
            corsConfig.setAllowedOrigins(Collections.singletonList(allowOrigin));
            corsConfig.setAllowedMethods(Collections.singletonList("*"));
            corsConfig.setAllowedHeaders(Collections.singletonList("*"));
            corsConfig.setAllowCredentials(true);
            corsConfig.setExposedHeaders(Collections.singletonList("Authorization"));
            corsConfig.setMaxAge(3600L);
            return corsConfig;
        }));

        /* csrf 설정 */
        http.csrf(AbstractHttpConfigurer::disable);

        /* request filter */
        http.authorizeHttpRequests(request ->
                request.requestMatchers("/api/v1/**").permitAll()
                        .anyRequest().authenticated());

        /* OAuth2 */
        http.oauth2Login(oauth2Config ->
                oauth2Config.loginPage(allowOrigin + "/login")
//                        .successHandler(null)
//                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(null))
        );

        return http.build();
    }
}
