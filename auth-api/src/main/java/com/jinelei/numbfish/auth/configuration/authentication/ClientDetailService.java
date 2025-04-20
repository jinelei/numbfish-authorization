package com.jinelei.numbfish.auth.configuration.authentication;

import com.jinelei.numbfish.auth.dto.ClientResponse;

@SuppressWarnings("unused")
public interface ClientDetailService {

    ClientResponse loadClientByAccessKey(String accessKey);

}
