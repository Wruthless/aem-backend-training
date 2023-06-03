package com.adobe.backendtraining.core.services.impl;


import com.adobe.backendtraining.core.services.DevName;
import org.osgi.service.component.annotations.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// The service is the interface.
@Component(service = DevName.class, immediate = true)
public class DevNameImpl implements DevName {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public String getName() {
        return "From: DevNameImpl";
    }

}
