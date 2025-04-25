package com.jinelei.numbfish.auth.service;

import com.jinelei.numbfish.auth.dto.SetupRequest;

@SuppressWarnings("unused")
public interface SetupService {
    Boolean checkIsSetup();

    Boolean init(SetupRequest request);
}
