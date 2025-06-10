package com.alfred.backoffice.modules.mail.infrastructure.external.gmail.service;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.UserCredentials;

import java.io.IOException;

public class GmailTokenProvider {

    public static String getAccessToken(String clientId, String clientSecret, String refreshToken) throws IOException {
        UserCredentials credentials = UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(refreshToken)
                .build();
        AccessToken token = credentials.refreshAccessToken();
        if (token == null || token.getTokenValue() == null) {
            throw new IllegalStateException("Error getting access token");
        }
        return token.getTokenValue();
    }
}