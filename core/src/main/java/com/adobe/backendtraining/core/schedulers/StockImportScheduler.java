package com.adobe.backendtraining.core.schedulers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

//org.apache.sling.event.*  api available in Sling 9 documentation:
import org.apache.sling.event.jobs.JobBuilder;
import org.apache.sling.event.jobs.JobBuilder.ScheduleBuilder;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.ScheduledJobInfo;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true,
           configurationPid = "com.adobe.backendtraining.core.schedulers.StockImportScheduler",
           configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = StockImportScheduler.StockImportConfiguration.class, factory = true)
public class StockImportScheduler {

    public static final String JOB_TOPIC_STOCKIMPORT = "com/adobe/backendtraining/core/jobs/stockimportjob";
    public static final String JOB_PROP_SYMBOL = "symbol";
    public static final String JOB_PROP_URL = "url";
    public static final String DEFAULT_IMPORT_URL = "https://raw.githubusercontent.com/Adobe-Marketing-Cloud/ADLS-Samples/master/stock-data/";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String searchableLogStr = "******-******-******";

    @ObjectClassDefinition(name = "Training Stock Importer")
    public @interface StockImportConfiguration {

        @AttributeDefinition (
                name = "Stock Symbol",
                description = "Characters representing the stock to be imported.",
                type = AttributeType.STRING
        )
        public String symbol() default "";

        @AttributeDefinition (
                name = "Expression",
                description = "Run every so often as defined in the cron-job expression.",
                type = AttributeType.STRING
        )
        public String cronExpression() default "0 0/2 * * * ?";

        @AttributeDefinition (
                name = "Stock URL",
                description = "URL to request the stock data to be imported",
                type = AttributeType.STRING
        )
        public String stock_url() default DEFAULT_IMPORT_URL;
    }

    @Reference
    private JobManager jobManager;

    private int schedulerID;
    private JobBuilder jobBuilder;
    private ScheduleBuilder scheduleBuilder;
    private ScheduledJobInfo theScheduledJob;

    @Activate
    protected void activate(StockImportConfiguration config) {
        logger.info(searchableLogStr + "StockImport ScheduledJob '{}' with ID: '{}' Activated",
                config.symbol(), schedulerID);
                schedulerID = config.symbol().hashCode();
                startJobScheduler(config);
    }

    @Modified
    protected void modified(StockImportConfiguration config) {
       removeJobScheduler(config) ;
       schedulerID = config.symbol().hashCode() + 1;
       startJobScheduler(config);
    }

    @Deactivate
    protected void deactivate(StockImportConfiguration config) {
        removeJobScheduler(config);
    }

    private void startJobScheduler(StockImportConfiguration config) {
        // Map that contains the configuration we want to pass to the job.
        HashMap<String, Object> jobProps = new HashMap<>(  );
        jobProps.put(JOB_PROP_SYMBOL, config.symbol());
        jobProps.put(JOB_PROP_URL, config.stock_url());

        jobBuilder.properties(jobProps);
        scheduleBuilder = jobBuilder.schedule();

        if(theScheduledJob == null) {
            List<String> errors = new ArrayList<>(  );
            scheduleBuilder.add(errors);
        } else {
            logger.info(searchableLogStr + "ScheduledJob added to the Queue. Topic: " + theScheduledJob.getJobTopic()
            + " " + "Properties: " + theScheduledJob.getJobProperties().toString() + " "
            + "Next Execution: " + theScheduledJob.getNextScheduledExecution().toString());
        }
    }

    private void removeJobScheduler(StockImportConfiguration config) {
        if(theScheduledJob != null) {
            logger.info(searchableLogStr + "Removing '{}' ScheduledJob, with ID ; '{}'", config.symbol(), schedulerID);
            theScheduledJob.unschedule();
        }
    }
}
