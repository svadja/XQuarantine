/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sasav.xquarantine.domain;

import org.apache.log4j.Logger;

/**
 *
 * @author sasav
 */
public class AccessByStatus {
    
    private static final Logger LOG = Logger.getLogger(AccessByStatus.class.getName());
    
    private static final String DEFAULT_ACCESS = "NNNN";
    /*
     Clean|Spam|Blocked|Virus
     example: "YYNN" - it denied resend Blocked and Virus
     */
    private String access;

    public AccessByStatus() {
        this.access = DEFAULT_ACCESS;
    }

    public AccessByStatus(String access) {
        if (validate(access)) {
            this.access = access;
        } else {
            LOG.error("Server: Not correct access " + access + " Expl. YYNN");
            this.access = DEFAULT_ACCESS;
        }
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    
    
    private boolean validate(String tryStr) {
        if (tryStr.length() == 4) {
            for (int i = 0; i < tryStr.length(); i++) {
                if ((tryStr.charAt(i) != 'Y') && ((tryStr.charAt(i) != 'N'))) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

}
