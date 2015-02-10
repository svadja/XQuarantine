package com.sasav.xquarantine.service;

import com.sasav.xquarantine.domain.DescriptionOfMail;
import java.util.List;

public interface MailList {

    public void addOneDescription(DescriptionOfMail dm);

    public void addListOfDescriptions(List<DescriptionOfMail> ldm);

    public void updateDescription(DescriptionOfMail dm);
    
    public void deleteOneDescription(DescriptionOfMail dm);

    public void deleteListOfDescriptions(List<DescriptionOfMail> ldm);

    //load all data from file
    public List<DescriptionOfMail> loadDescriptionsFromFile(String fileName);

    // load from file with time from lastTime
    public List<DescriptionOfMail> loadDescriptionsFromFile(String fileName, long lastTime);

    //load from file and compare description in last time
    public List<DescriptionOfMail> loadDescriptionsFromFile(String fileName, List<DescriptionOfMail> mailInLastTime);

    public List<DescriptionOfMail> getDescriptions(String recipient, String sender, String dateStart, String dateEnd, String Status, String type);

    public DescriptionOfMail getDescriptionById(long id);

}
