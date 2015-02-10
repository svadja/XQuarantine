/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sasav.xquarantine.controller;

import com.sasav.xquarantine.domain.AccessByStatus;
import com.sasav.xquarantine.domain.DescriptionOfMail;
import com.sasav.xquarantine.domain.StoreDate;
import com.sasav.xquarantine.service.MailListImpl;
import com.sasav.xquarantine.service.SecurityImpl;
import com.sasav.xquarantine.service.UtilityMailImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author sasav
 */
@Controller
public class UserController {

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    
    @Autowired
    private MailListImpl ml;

    @Autowired
    private UtilityMailImpl utilityMail;

    @Autowired
    private SecurityImpl securityImpl;

    @Autowired
    private AccessByStatus accessResendUser;

    @Autowired
    private StoreDate storeDate;
    

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(value = "/quarantine", method = RequestMethod.GET)
    public String quarantineGET(Locale locale, Model model) {
        String userName = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername().toLowerCase();
        model.addAttribute("recipient", userName);
        model.addAttribute("datestart", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(storeDate.getStartDate())));
        model.addAttribute("dateend", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(storeDate.getEndDate())));
        //model.addAttribute("maillist", ml.getDescriptions(userName, "", Long.toString(new Date().getTime() - 86400000), "", "", ""));
        return "quarantine";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(value = "/quarantine", method = RequestMethod.POST)
    public String quarantinePOST(@RequestParam("sender") String sender,
            @RequestParam("datestart") String datestart,
            @RequestParam("dateend") String dateend,
            @RequestParam("status") String status,
            Model model) {
        String userName = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername().toLowerCase();
        String ds = "";
        String de = "";
        try {
            if ((datestart != null) && (datestart.length() == 16)) {
                ds = Long.toString(new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(datestart).getTime());
            }
            if ((dateend != null) && (dateend.length() == 16)) {
                de = Long.toString(new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(dateend).getTime());
            }
        } catch (ParseException ex) {
            LOG.error("User: "+((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername().toLowerCase()+" "+"The input time is not correct");
        }
        List<DescriptionOfMail> ldm = ml.getDescriptions(userName, sender, ds, de, status, "");
        model.addAttribute("recipient", userName);
        model.addAttribute("sender", sender);
        model.addAttribute("datestart", datestart);
        model.addAttribute("dateend", dateend);
        model.addAttribute("accessResendUser", accessResendUser);
        model.addAttribute("maillist", ldm);
        return "quarantine";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(value = "/quarantine/resend", params = "idmail", method = RequestMethod.POST)
    public void resendMessage(@RequestParam("idmail") long idmail) {
        String user = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        DescriptionOfMail dm = ml.getDescriptionById(idmail);
        if (dm != null) {
            if (securityImpl.hasAccess(dm, accessResendUser) && securityImpl.compareRecipientToUser(dm, user)) {
                 LOG.info("User: "+((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername().toLowerCase()+" "+"Resend: \"" + dm.toString()+"\"");
                utilityMail.resendMail(dm);
            }
                 LOG.error("User: "+((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername().toLowerCase()+" "+"Try to resend not own message: \"" + dm.toString()+"\"");
        }
    }

}
