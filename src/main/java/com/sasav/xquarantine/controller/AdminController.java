/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sasav.xquarantine.controller;

import com.sasav.xquarantine.domain.DescriptionOfMail;
import com.sasav.xquarantine.domain.StoreDate;
import com.sasav.xquarantine.service.MailListImpl;
import com.sasav.xquarantine.service.UtilityMailImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping(value = "/adminspace")
public class AdminController {
    
    private static final Logger LOG = Logger.getLogger(AdminController.class.getName());
    
    @Autowired
    private MailListImpl ml;
    
    @Autowired
    private UtilityMailImpl utilityMail;

    @Autowired
    private StoreDate storeDate;

    @Value("${path.folder_stat}")
    private String folder_stat;

    //load NEW mail list items (same as shedule)
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/loadfromfile", method = RequestMethod.GET)
    public String loadDiscOfMail() {
        ml.addListOfDescriptions(ml.loadDescriptionsFromFile(folder_stat + "SR" + new SimpleDateFormat("yyMMdd").format(new Date()) + ".CSV", storeDate.getEndDate()));
        return "redirect:aquarantine";
    }

    //load all mail list items from file
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/loadfromfile", params = "file", method = RequestMethod.GET)
    public String loadDiscOfMailFFile(@RequestParam("file") String file_name) {
        ml.addListOfDescriptions(ml.loadDescriptionsFromFile(file_name));
        return"redirect:aquarantine";
    }

    //quarantine for admin
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/aquarantine", method = RequestMethod.GET)
    public String quarantineGET(Locale locale, Model model) {
        model.addAttribute("datestart", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(storeDate.getStartDate())));
        model.addAttribute("dateend", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(storeDate.getEndDate())));
        return "aquarantine";
    }

    //quarantine for admin
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/aquarantine", method = RequestMethod.POST)
    public String quarantinePOST(@RequestParam("recipient") String recipient,
            @RequestParam("sender") String sender,
            @RequestParam("datestart") String datestart,
            @RequestParam("dateend") String dateend,
            @RequestParam("status") String status,
            Model model) {
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
        List<DescriptionOfMail> ldm = ml.getDescriptions(recipient, sender, ds, de, status, "");
        model.addAttribute("recipient", recipient);
        model.addAttribute("sender", sender);
        model.addAttribute("datestart", datestart);
        model.addAttribute("dateend", dateend);
        model.addAttribute("maillist", ldm);
        return "aquarantine";
    }
    //resend for admin
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/aquarantine/resend", params = "idmail", method = RequestMethod.POST)
    public void resendMessage(@RequestParam("idmail") long idmail) {
        DescriptionOfMail dm = ml.getDescriptionById(idmail);
        LOG.info("User: "+((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername().toLowerCase()+" "+"Resend: \"" + dm.toString()+"\"");
        utilityMail.resendMail(dm);
    }

}
