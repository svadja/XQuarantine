package com.sasav.xquarantine.controller;

import com.sasav.xquarantine.dao.Dao;
import com.sasav.xquarantine.domain.StoreDate;
import com.sasav.xquarantine.service.MailListImpl;
import com.sasav.xquarantine.service.UtilityMailImpl;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
    
    private static final Logger LOG = Logger.getLogger(HomeController.class.getName());

    @Autowired
    Dao daoI;

    @Autowired
    UtilityMailImpl utilityMail;

    @Autowired
    MailListImpl ml;

    @Autowired
    StoreDate storeDate;
    
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:adminspace/aquarantine";
        }
        return "redirect:quarantine";
    }
}
