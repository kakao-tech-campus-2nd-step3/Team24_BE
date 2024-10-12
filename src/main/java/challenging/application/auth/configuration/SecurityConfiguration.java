package challenging.application.auth.configuration;

import challenging.application.auth.filter.JWTAccessFilter;
import challenging.application.auth.filter.JWTLogoutFilter;
import challenging.application.auth.filter.JWTRefreshFilter;
import challenging.application.auth.jwt.JWTUtils;
import challenging.application.auth.oauth.OAuth2SuccessHandler;
import challenging.application.auth.oauth.OAuth2UserServiceImpl;
import challenging.application.auth.repository.RefreshTokenRepository;
import challenging.application.auth.utils.servletUtils.jwtUtils.FilterResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final OAuth2UserServiceImpl oAuth2UserService;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final JWTUtils jwtUtil;

    private final RefreshTokenRepository refreshTokenRepository;

    private final FilterResponseUtils filterResponseUtils;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf((auth) -> auth.disable());

        http
            .formLogin((auth) -> auth.disable());

        http
            .httpBasic((auth) -> auth.disable());

        http
            .addFilterAfter(new JWTAccessFilter(jwtUtil, filterResponseUtils), OAuth2LoginAuthenticationFilter.class)
            .addFilterAfter(new JWTRefreshFilter(filterResponseUtils), OAuth2LoginAuthenticationFilter.class)
            .addFilterBefore(new JWTLogoutFilter(refreshTokenRepository, filterResponseUtils), LogoutFilter.class);

        http
            .oauth2Login((oauth2) -> oauth2
                    .loginPage("/login")
                    .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig.userService(oAuth2UserService))
                    .successHandler(oAuth2SuccessHandler)
            );

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/reissue", "/h2-console/**").permitAll()
                        .requestMatchers("my").hasRole("USER")
                        .anyRequest().authenticated());

        http
                .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/favicon.ico")
                .requestMatchers("/error")
                .requestMatchers(toH2Console());
    }

}
