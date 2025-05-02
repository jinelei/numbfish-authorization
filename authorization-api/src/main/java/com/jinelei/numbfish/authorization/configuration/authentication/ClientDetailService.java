package com.jinelei.numbfish.authorization.configuration.authentication;

import com.jinelei.numbfish.authorization.dto.ClientResponse;

@SuppressWarnings("unused")
public interface ClientDetailService {

    ClientResponse loadClientByAccessKey(String accessKey);

}
