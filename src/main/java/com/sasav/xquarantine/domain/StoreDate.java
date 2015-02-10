/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sasav.xquarantine.domain;

import com.sasav.xquarantine.service.MailListImpl;
import java.text.ParseException;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 *
 * @author Vadim
 */
@Component
public class StoreDate {

    private long startDate;
    private long endDate;

    public StoreDate() {
    }

    public StoreDate(MailListImpl ml) {
        this.startDate = ml.getMinTime().longValue();
        this.endDate = ml.getMaxTime().longValue();
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public Date getStarDateInDate() throws ParseException {
        return new Date(Long.toString(startDate));
    }

    public Date getEndDateInDate() throws ParseException {
        return new Date(Long.toString(endDate));
    }

}
