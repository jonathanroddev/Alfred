package com.alfred.backoffice.modules.auth.infrastructure.external.firebase.filter;

import com.alfred.backoffice.modules.auth.domain.exception.ApiException;
import com.alfred.backoffice.modules.auth.domain.exception.NotFoundException;
import com.alfred.backoffice.modules.auth.domain.exception.UnauthorizedException;
import com.alfred.backoffice.modules.auth.domain.model.Role;
import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.domain.service.UserService;
import com.alfred.backoffice.modules.auth.infrastructure.configuration.ErrorMessageProperties;
import com.alfred.backoffice.modules.auth.infrastructure.privacy.PrivacyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final ErrorMessageProperties errorMessageProperties;
    private final PrivacyService privacyService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return this.privacyService.isUnrestrictedPath(path);
    }

    private String getBodyException(String code, String message) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(Map.of(
                "code", code,
                "message", message
        ));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String idToken = request.getHeader("Authorization");
            String userID = request.getHeader("amg-user");

            FirebaseToken token = getToken(idToken, userID);

            try {
                User user = userService.getUserByUuidAndExternalUuid(userID, token.getUid());
                if(!this.userService.isActive(user)) {
                    throw new UnauthorizedException("amg-401_3");
                }
                Set<Role> roles = user.getRoles();

                List<GrantedAuthority> alfredPermissionsAuthorities = AuthorityUtils.createAuthorityList(
                        roles.stream()
                                .flatMap(role -> role.getPermissionRoles().stream()
                                        .map(permissionRole -> String.format("%s_%s",
                                                permissionRole.getPermission().getResource().getName(),
                                                permissionRole.getPermission().getOperation().getName()))
                                ).distinct().toArray(String[]::new));
                alfredPermissionsAuthorities.add(new SimpleGrantedAuthority("AUTHENTICATED"));

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, alfredPermissionsAuthorities);

                SecurityContextHolder.getContext()
                        .setAuthentication(
                                authenticationToken
                        );
            } catch (NotFoundException nfe) {
                throw new UnauthorizedException(nfe.getCode());
            }

        } catch (ApiException ex) {
            logger.error(ex);
            String code = ex.getCode();
            String message = errorMessageProperties.getMessage(code);

            response.setStatus(ex.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(this.getBodyException(code, message));
            return;
        } catch (FirebaseAuthException fbe) {
            logger.error(fbe);
            String code = "amg-401_4";
            String message = errorMessageProperties.getMessage(code);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(this.getBodyException(code, message));
            return;
        } catch (Exception e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Please, contact the support");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static FirebaseToken getToken(String idToken, String idCommunity) throws FirebaseAuthException {
        if (idToken == null || idToken.isEmpty()) {
            throw new UnauthorizedException("amg-401_1");
        }

        if (idCommunity == null || idCommunity.isEmpty()) {
            throw new UnauthorizedException("amg-401_2");
        }

        /*
        TODO: Check https://www.baeldung.com/spring-security-firebase-authentication#exchanging-refresh-tokens-for-new-id-tokens
        to see if it's better exchange refresh rather than id token
         */

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseToken token = firebaseAuth.verifyIdToken(idToken.replace("Bearer ", ""));
        return token;
    }

    private static List<GrantedAuthority> getAuthoritiesFromToken(FirebaseToken token) {
        Object claims = token.getClaims().get("authorities");
        List<String> permissions = (List<String>) claims;
        List<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;
        if (permissions != null && !permissions.isEmpty()) {
            authorities = AuthorityUtils.createAuthorityList(permissions);
        }
        return authorities;
    }
}
