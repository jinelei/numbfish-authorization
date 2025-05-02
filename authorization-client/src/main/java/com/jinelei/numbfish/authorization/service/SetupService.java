package com.jinelei.numbfish.authorization.service;

import com.jinelei.numbfish.authorization.dto.SetupRequest;

@SuppressWarnings("unused")
public interface SetupService {
    Boolean checkIsSetup();

    Boolean init(SetupRequest request);
}
