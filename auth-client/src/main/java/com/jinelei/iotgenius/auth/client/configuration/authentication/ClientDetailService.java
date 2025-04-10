package com.jinelei.iotgenius.auth.client.configuration.authentication;

import com.jinelei.iotgenius.auth.dto.client.ClientResponse;

@SuppressWarnings("unused")
public interface ClientDetailService {

    ClientResponse loadClientByAccessKey(String accessKey);

}
