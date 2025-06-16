package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.request.Registry;

public interface CustomerService {
    void askJoin(Registry registry);
}
