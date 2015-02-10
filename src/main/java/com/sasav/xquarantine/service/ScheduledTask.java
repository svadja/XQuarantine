package com.sasav.xquarantine.service;

import com.sasav.xquarantine.dao.Dao;
import com.sasav.xquarantine.domain.DescriptionOfMail;
import com.sasav.xquarantine.domain.StoreDate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

public class ScheduledTask {
    //http://howtodoinjava.com/2013/04/23/4-ways-to-schedule-tasks-in-spring-3-scheduled-example/

    @Autowired
    private MailListImpl ml;

    @Autowired
    private StoreDate storeDate;

    @Autowired
    private Dao daoI;

    @Value("${path.folder_stat}")
    private String folder_stat;

    @Value("${maillist.keepdays}")
    private short keepdays;

    public ScheduledTask() {
    }

    public ScheduledTask(MailListImpl ml, StoreDate storeDate, Dao daoI, String folder_stat, short keepdays) {
        this.ml = ml;
        this.storeDate = storeDate;
        this.daoI = daoI;
        this.folder_stat = folder_stat;
        this.keepdays = keepdays;
    }

    
    
    
    // @Scheduled(cron="0 */5 * * * *")
    public void updateMailList() {
        ml.addListOfDescriptions(ml.loadDescriptionsFromFile(folder_stat + "SR" + new SimpleDateFormat("yyMMdd").format(new Date()) + ".CSV", storeDate.getEndDate()));
    }

    public void deleteOldMLItems() {
       ml.deleteListOfDescriptions((List<DescriptionOfMail>) ml.getDescriptions("", "", "", Long.toString(new Date().getTime() - keepdays * 86400000), "", ""));
       
    }

}
