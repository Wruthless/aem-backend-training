package com.adobe.backendtraining.core.services.impl;

import com.adobe.backendtraining.core.services.DeveloperInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Component( service = DeveloperInfo.class, immediate = true )
@Designate( ocd = DeveloperInfoConfiguration.class ) // The interface for OSGi configuration.
public class DeveloperInfoImpl implements DeveloperInfo {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // Hold OSGi configuration values.
    private boolean showDeveloper;
    private String developerName;
    private String[] developerHobbiesList;
    private String langPreference;

    @Activate
    @Modified
    protected void activate(DeveloperInfoConfiguration config) {
        showDeveloper = config.developerinfo_showinfo();
        developerName = config.developerinfo_name();
        developerHobbiesList = config.developerinfo_hobbies();
        langPreference = config.developerinfo_language();
        logger.info("[+] Info Component config saved.");
    }

    @Deactivate
    protected void deactivate(DeveloperInfoConfiguration config) {
        logger.info("[+] Developer Info Component Deactivated, Good-bye " + developerName);
    }

    // Show how OSGi configurations can be brought into an OSGi component.
    @Override
    public String getDeveloperInfo( ) {
        String developerHobbies = Arrays.toString(developerHobbiesList);

        if ( showDeveloper ) {
            return "Created by " + developerName + ". <br>Hobbies include: " + developerHobbies
                    + ". <br>Preferred programming language in AEM is " + langPreference;
        }

        return "";
    }

    // A method to show an OSGi service/component relationship
    // public String getDeveloperInfo() {
    //    return "Hello! Wruthless is my developer! I am no product of random development.";
    // }

}
