package com.adobe.backendtraining.core.services.impl;

import com.adobe.backendtraining.core.services.DeveloperInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Component(service = DeveloperInfo.class, immediate = true)
public class DeveloperInfoImpl implements DeveloperInfo {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Activate
    @Modified
    protected void activate(Map<String, Object> config) {
        logger.info("[+] Component config saved.");
    }

    @Deactivate
    protected void deactivate() {
        logger.info("[!] Component deactivated.");
    }

    // A method to show an OSGi service/component relationship
    public String getDeveloperInfo() {
        return "Hello! Wruthless is my developer! I am no product of random development.";
    }

}
