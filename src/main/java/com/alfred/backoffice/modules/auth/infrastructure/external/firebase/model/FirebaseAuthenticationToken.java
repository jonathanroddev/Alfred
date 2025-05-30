package com.alfred.backoffice.modules.auth.infrastructure.external.firebase.model;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class FirebaseAuthenticationToken extends AbstractAuthenticationToken {

    private FirebaseToken firebaseToken;
    private String idToken;

    public FirebaseAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public FirebaseAuthenticationToken(
            String idToken, FirebaseToken firebaseToken, List<GrantedAuthority> authorities) {
        super(authorities);
        this.idToken = idToken;
        this.firebaseToken = firebaseToken;
    }

    @Override
    public Object getCredentials() {
        return idToken;
    }

    @Override
    public Object getPrincipal() {
        return firebaseToken.getUid();
    }
}