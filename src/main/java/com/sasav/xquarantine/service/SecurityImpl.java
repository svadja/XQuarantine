/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sasav.xquarantine.service;

import com.sasav.xquarantine.domain.AccessByStatus;
import com.sasav.xquarantine.domain.DescriptionOfMail;

/**
 *
 * @author sasav
 */
public class SecurityImpl implements Security {
    
    @Override
    public boolean hasAccess(DescriptionOfMail dm, AccessByStatus abs) {
        for (DescriptionOfMail.Status status : dm.getStatus()) {
            if ((status.toString().equals("CLEAN")) && (abs.getAccess().charAt(0) != 'Y')) {
                return false;
            }
            if ((status.toString().equals("SPAM")) && (abs.getAccess().charAt(1) != 'Y')) {
                return false;
            }
            if ((status.toString().equals("BLOCKED")) && (abs.getAccess().charAt(2) != 'Y')) {
                return false;
            }
            if ((status.toString().equals("VIRUS")) && (abs.getAccess().charAt(3) != 'Y')) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean compareRecipientToUser(DescriptionOfMail dm, String user) {
        return user.toLowerCase().equals(dm.getRecipient().toLowerCase());
    }

}
