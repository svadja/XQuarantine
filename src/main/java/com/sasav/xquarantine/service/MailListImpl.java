package com.sasav.xquarantine.service;

import com.sasav.xquarantine.dao.Dao;
import com.sasav.xquarantine.domain.DescriptionOfMail;
import com.sasav.xquarantine.domain.StoreDate;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class MailListImpl implements MailList {

    private static final Logger LOG = Logger.getLogger(MailListImpl.class.getName());
    
    private static final Pattern pdate = Pattern.compile("\"([0-9]{4})-([0-9]{2})-([0-9]{2})\"");
    private static final Pattern ptime = Pattern.compile("\"([0-9]{2}):([0-9]{2}):([0-9]{2})\"");

    @Autowired
    private Dao daoI;

    @Autowired
    private StoreDate storeDate;

    
    //What is the status the blocked?
    @Value("${maillist.strBlokedType}")
    private String strBlokedType;

    public MailListImpl() {
    }

    public MailListImpl(Dao daoI, StoreDate storeDate, String strBlokedType) {
        this.daoI = daoI;
        this.storeDate = storeDate;
        this.strBlokedType = strBlokedType;
    }

    
    @Override
    public void addListOfDescriptions(List<DescriptionOfMail> ldm) {
        daoI.savaOrUpdateAll(ldm);
        storeDate.setEndDate(getMaxTime().longValue());
        storeDate.setStartDate(getMinTime().longValue());
        LOG.info("Server: added " + ldm.size() +" descriptions to database" );
    }

    @Override
    public List<DescriptionOfMail> loadDescriptionsFromFile(String fileName) {
        List<DescriptionOfMail> resultList = new ArrayList<DescriptionOfMail>();
        String[] blokedTypes = strBlokedType.split(";");
        try {
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            try {
                String currentLine = null;
                while ((currentLine = input.readLine()) != null) {
                    String[] arrayString = currentLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                    if (verificationString(arrayString)) {
                        long time = convertDTimeToLong(arrayString[0], arrayString[1]);
                        for (int i = 0; i < arrayString.length; i++) {
                            arrayString[i] = arrayString[i].replace("\"", "");
                        }
                        List<DescriptionOfMail.Status> status = new ArrayList<DescriptionOfMail.Status>();
                        if (!arrayString[17].isEmpty()) {
                            status.add(DescriptionOfMail.Status.VIRUS);
                        }
                        if (!arrayString[19].isEmpty()) {
                            boolean isBlocked = false;
                            for (String itemBT : blokedTypes) {
                                if (arrayString[19].toLowerCase().equals(itemBT.toLowerCase())) {
                                    isBlocked = true;
                                }
                            }

                            if (isBlocked) {
                                status.add(DescriptionOfMail.Status.BLOCKED);
                            } else {
                                status.add(DescriptionOfMail.Status.SPAM);
                            }
                        }

                        if ((arrayString[17].isEmpty()) && (arrayString[19].isEmpty())) {
                            status.add(DescriptionOfMail.Status.CLEAN);
                        }
                        DescriptionOfMail tmpDM = new DescriptionOfMail(time, arrayString[2], arrayString[4].toLowerCase(), arrayString[6].toLowerCase(), arrayString[9], arrayString[16], arrayString[14], status);
                        resultList.add(tmpDM);

                    }

                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return resultList;
    }

    @Override
    public List<DescriptionOfMail> loadDescriptionsFromFile(String fileName, long lastTime) {
        List<DescriptionOfMail> lDM = (List<DescriptionOfMail>) daoI.getByHQLQuery("from DescriptionOfMail\n"
                + "where timeMS=" + lastTime);
        return loadDescriptionsFromFile(fileName, lDM);
    }

    @Override

    public List<DescriptionOfMail> loadDescriptionsFromFile(String fileName, List<DescriptionOfMail> mailInLastTime) {

        List<DescriptionOfMail> resultList = new ArrayList<DescriptionOfMail>();
        //String[] blokedTypes = strBlokedType.split(";");
        if ((mailInLastTime == null) | (mailInLastTime.size() == 0)) {
            resultList = loadDescriptionsFromFile(fileName);
        } else {

            try {
                BufferedReader input = new BufferedReader(new FileReader(fileName));
                try {
                    String currentLine = null;
                    while ((currentLine = input.readLine()) != null) {
                        String[] arrayString = currentLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                        if (verificationString(arrayString)) {
                            long time = convertDTimeToLong(arrayString[0], arrayString[1]);
                            /*ugly code :( */
                            if (time > mailInLastTime.get(0).getTimeMS()) {
                                for (int i = 0; i < arrayString.length; i++) {
                                    arrayString[i] = arrayString[i].replace("\"", "");
                                }
                                List<DescriptionOfMail.Status> status = new ArrayList<DescriptionOfMail.Status>();
                                if (!arrayString[17].isEmpty()) {
                                    status.add(DescriptionOfMail.Status.VIRUS);
                                }
                                if (!arrayString[19].isEmpty()) {
                                    if (isBlocked(arrayString[19])) {
                                        status.add(DescriptionOfMail.Status.BLOCKED);
                                    } else {
                                        status.add(DescriptionOfMail.Status.SPAM);
                                    }
                                }

                                if ((arrayString[17].isEmpty()) && (arrayString[19].isEmpty())) {
                                    status.add(DescriptionOfMail.Status.CLEAN);
                                }
                                DescriptionOfMail tmpDM = new DescriptionOfMail(time, arrayString[2], arrayString[4].toLowerCase(), arrayString[6].toLowerCase(), arrayString[9], arrayString[16], arrayString[14], status);
                                resultList.add(tmpDM);
                            } else if (time == mailInLastTime.get(0).getTimeMS()) {

                                for (int i = 0; i < arrayString.length; i++) {
                                    arrayString[i] = arrayString[i].replace("\"", "");
                                }
                                List<DescriptionOfMail.Status> status = new ArrayList<DescriptionOfMail.Status>();
                                if (!arrayString[17].isEmpty()) {
                                    status.add(DescriptionOfMail.Status.VIRUS);
                                }
                                if (!arrayString[19].isEmpty()) {
                                    if (isBlocked(arrayString[19])) {
                                        status.add(DescriptionOfMail.Status.BLOCKED);
                                    } else {
                                        status.add(DescriptionOfMail.Status.SPAM);
                                    }
                                }

                                if ((arrayString[17].isEmpty()) && (arrayString[19].isEmpty())) {
                                    status.add(DescriptionOfMail.Status.CLEAN);
                                }
                                DescriptionOfMail tmpDM = new DescriptionOfMail(time, arrayString[2], arrayString[4].toLowerCase(), arrayString[6].toLowerCase(), arrayString[9], arrayString[16], arrayString[14], status);
                                if (!isIncluded(tmpDM, mailInLastTime)) {
                                    resultList.add(tmpDM);
                                }

                            }

                        }
                    }
                } finally {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return resultList;
    }

    private boolean verificationString(String[] str) {
        if (str.length >= 19) {
            if ((pdate.matcher(str[0]).matches()) & (ptime.matcher(str[1]).matches())) {
                return true;
            }
        }

        return false;
    }

    private long convertDTimeToLong(String date, String time) {
        long result = 0;
        try {
            result = new SimpleDateFormat("\"yyyy-MM-dd\" \"HH:mm:ss\"").parse(date + " " + time).getTime();
        } catch (ParseException ex) {
           LOG.error("Server: Cannot convert " +date+ " "+time+" to long");
        }
        return result;
    }

    public boolean isIncluded(DescriptionOfMail thismail, List<DescriptionOfMail> mailInLastTime) {
        for (DescriptionOfMail mail : mailInLastTime) {
            if (mail.equals(thismail)) {
                return true;
            }
        }
        return false;

    }

    /*
     Для користувачів ресіпіент жостко забивається користувач з креденшенелів
     */
    @Override
    public List<DescriptionOfMail> getDescriptions(String recipient, String sender, String dateStart, String dateEnd, String status, String type) {

        StringBuilder query = new StringBuilder("from DescriptionOfMail ");
        query.append("where recipient like \'%").append(recipient).append("%\'");
        if ((sender != null) && (!sender.isEmpty())) {
            query.append(" AND sender like \'%").append(sender).append("%\'");
        }
        if ((dateStart != null) && (!dateStart.isEmpty())) {
            query.append(" AND timeMS > ").append(dateStart);
        }
        if ((dateEnd != null) && (!dateEnd.isEmpty())) {
            query.append(" AND timeMS < ").append(dateEnd);
        }
        if ((type != null) && (!type.isEmpty())) {
            query.append(" AND type like \'%").append(type).append("%\'");
        }
        if ((status != null) && (!status.isEmpty())) {
            query.append(" AND \'").append(status).append("\' in elements(status)");
        }

        query.append(" order by timeMS desc");
        // http://www.acnenomor.com/3298079p2/nhibernate-hql-query-for-collection-of-values-with-multiple-match-values

        List<DescriptionOfMail> ldm = daoI.getByHQLQuery(query.toString());
        return ldm;
    }

    public BigInteger getMaxTime() {
        BigInteger result = new BigInteger("0");
        Object resultQ = daoI.getOneValue("select max(timeMS) from descriptionofmail");
        if (resultQ != null) {
            result = (BigInteger) resultQ;
        }
        return result;
    }

    public BigInteger getMinTime() {
        BigInteger result = new BigInteger("0");
        Object resultQ = daoI.getOneValue("select min(timeMS) from descriptionofmail");
        if (resultQ != null) {
            result = (BigInteger) resultQ;
        }
        return result;
    }
    
    private boolean isBlocked(String strSpam){
        String[] blokedTypes = strBlokedType.split(";");
        for(String blItem:blokedTypes){
            if (strSpam.toLowerCase().contains(blItem.trim().toLowerCase())){
                return true;
            }
        }
        
        return false;
    }

    @Override
    public DescriptionOfMail getDescriptionById(long id) {
        return (DescriptionOfMail) daoI.getById(DescriptionOfMail.class.getName(), id);
    }

    @Override
    public void addOneDescription(DescriptionOfMail dm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateDescription(DescriptionOfMail dm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteOneDescription(DescriptionOfMail dm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteListOfDescriptions(List<DescriptionOfMail> ldm) {
        daoI.deleteAll(ldm);
        storeDate.setEndDate(getMaxTime().longValue());
        storeDate.setStartDate(getMinTime().longValue());
        LOG.info("Server: deleted " + ldm.size() +" descriptions from database" );
    }

}
