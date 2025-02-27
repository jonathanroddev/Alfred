package com.alfred.backoffice.modules.auth.infrastructure.external.firebase.filter;

import com.alfred.backoffice.modules.auth.infrastructure.external.firebase.model.FirebaseAuthenticationToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String idToken = request.getHeader("Authorization");

        if (idToken == null || idToken.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Firebase ID-Token");
            return;
        }

        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseToken token = firebaseAuth.verifyIdToken(idToken.replace("Bearer ", ""));
            List<GrantedAuthority> fireBaseAuthorities = this.getAuthoritiesFromToken(token);

            // TODO: Add our permissions
            // It's necessary to add the prefix in order to use it with Spring Security hasRole
            List<String> permissions = Stream.of("RESOURCE_PERMISSION")
                    .map(role -> "ROLE_" + role)
                    .toList();

            List<GrantedAuthority> alfredPermissionsAuthorities = AuthorityUtils.createAuthorityList(permissions.toArray(new String[0]));

            alfredPermissionsAuthorities.addAll(fireBaseAuthorities);

            SecurityContextHolder.getContext()
                    .setAuthentication(
                            new FirebaseAuthenticationToken(idToken, token, alfredPermissionsAuthorities));


            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);

        } catch (Exception e) {
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
