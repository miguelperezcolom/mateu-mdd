package io.mateu.mdd.core.model;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MateuJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        String uuid = dataMap.getString("_commandId");

        BaseAppContext.scheduledCommands.get(uuid).run();
    }
}
