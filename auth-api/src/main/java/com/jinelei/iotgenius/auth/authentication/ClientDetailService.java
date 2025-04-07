package com.jinelei.iotgenius.auth.authentication;

import com.jinelei.iotgenius.auth.dto.client.ClientResponse;

@SuppressWarnings("unused")
public interface ClientDetailService {

    ClientResponse loadClientByAccessKey(String accessKey);

}
