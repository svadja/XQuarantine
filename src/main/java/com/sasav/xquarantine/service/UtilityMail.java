/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sasav.xquarantine.service;

import com.sasav.xquarantine.domain.DescriptionOfMail;

/**
 *
 * @author sasav
 */
public interface UtilityMail {
    public boolean resendMail(DescriptionOfMail dm);
}
