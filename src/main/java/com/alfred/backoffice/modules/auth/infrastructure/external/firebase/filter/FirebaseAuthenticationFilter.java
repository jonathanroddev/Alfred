package com.alfred.backoffice.modules.auth.infrastructure.external.firebase.filter;

import com.alfred.backoffice.modules.auth.domain.model.Role;
import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.domain.service.UserService;
import com.alfred.backoffice.modules.auth.infrastructure.external.firebase.model.FirebaseAuthenticationToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // TODO: Refactor this method due to duplicity in OpenAPIConfig
        List<String> excludes = List.of("public", "signup", "login", "docs", "swagger");
        return excludes.stream().anyMatch(path::contains);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        String idToken = request.getHeader("Authorization");
        String idCommunity = request.getHeader("amg-community");

        if (idToken == null || idToken.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Firebase ID-Token");
            return;
        }

        if (idCommunity == null || idCommunity.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Community ID");
            return;
        }

        try {
            /*
            TODO: Check https://www.baeldung.com/spring-security-firebase-authentication#exchanging-refresh-tokens-for-new-id-tokens
            to see if it's better exchange refresh rather than id token
             */

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseToken token = firebaseAuth.verifyIdToken(idToken.replace("Bearer ", ""));

            // TODO: Handle exception on find user
             User user = userService.getUserByExternalUuidAndCommunity(token.getUid(), idCommunity);
             if(!this.userService.isActive(user)) {
                 // TODO: Throw custom exception. Extend of RuntimeException
                 throw new Exception();
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

        } catch (Exception e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase ID-Token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    // TODO: Check if this is necessary because we don't use authorities from Firebase
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
