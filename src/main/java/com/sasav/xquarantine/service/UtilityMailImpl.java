/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sasav.xquarantine.service;

import com.sasav.xquarantine.dao.Dao;
import com.sasav.xquarantine.domain.DescriptionOfMail;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @author sasav
 */
@Service("utilityMail")
public class UtilityMailImpl implements UtilityMail {

    private static final Logger LOG = Logger.getLogger(UtilityMailImpl.class.getName());
    
    @Value("${path.path_archive}")
    private String path_archive;

    @Value("${mail.markresend}")
    private String markresend;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private Dao daoI;

    public UtilityMailImpl() {
    }

    public UtilityMailImpl(String path_archive, String markresend, JavaMailSenderImpl mailSender, Dao daoI) {
        this.path_archive = path_archive;
        this.markresend = markresend;
        this.mailSender = mailSender;
        this.daoI = daoI;
    }
    

    @Override
    public boolean resendMail(DescriptionOfMail dm) {
        String path="";
        if (dm != null) {
            try {
                String date = new SimpleDateFormat("yyyyMMdd").format(new Date(dm.getTimeMS()));
                String type = "";
                switch (dm.getType()) {
                    case "I":
                        type = "HIST-IN";
                        break;
                    case "O":
                        type = "HIST-OUT";
                        break;
                }

                path = path_archive + type + "\\" + date.substring(0, 4) + "\\" + date.substring(4, 6) + "\\" + date.substring(6, 8) + "\\";
                MimeMessage msg = mailSender.createMimeMessage(new FileInputStream(path + dm.getFile()));
                try {
                    StringBuilder origRecipients = new StringBuilder();
                    for (Address adr : msg.getAllRecipients()) {
                        origRecipients.append(adr.toString()).append(", ");
                    }
                    origRecipients.delete(origRecipients.length() - 2, origRecipients.length());
                    msg.addHeader("XWall-OriginalRecipients", origRecipients.toString());

                    msg.setRecipients(Message.RecipientType.TO, dm.getRecipient());
                    msg.setRecipients(Message.RecipientType.CC, "");
                    msg.setRecipients(Message.RecipientType.BCC, "");
                    String subj = msg.getSubject();
                    if (subj==null){
                        subj="";
                    }
                    msg.setSubject(markresend + subj);
                    mailSender.send(msg);

                } catch (MessagingException ex) {
                    LOG.error("Server: MessagingException "+ ex);
                }
            } catch (FileNotFoundException ex) {
                LOG.error("Server: File " +path + dm.getFile()+" not found");
            }
            return true;
        } else {
            return false;
        }
    }
}
