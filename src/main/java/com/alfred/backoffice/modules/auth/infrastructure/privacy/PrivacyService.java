package com.alfred.backoffice.modules.auth.infrastructure.privacy;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PrivacyService {

    @Value("#{'${unrestricted.paths}'.split(',')}")
    private final List<String> unrestrictedPaths;

    public boolean isUnrestrictedPath(String path) {
        return unrestrictedPaths.stream().anyMatch(path::contains);
    }
}
