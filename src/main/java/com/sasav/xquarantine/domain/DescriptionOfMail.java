package com.sasav.xquarantine.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DescriptionOfMail")
public class DescriptionOfMail {

    
    //add policy
    public enum Status {

        CLEAN, SPAM, BLOCKED ,VIRUS
    };
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    //time in ms
    private long timeMS;
    
    private String type;
    @Column(columnDefinition="TEXT")
    private String sender;
    @Column(columnDefinition="TEXT")
    private String recipient;
    @Column(columnDefinition="TEXT")
    private String subject;
    private String file;
    private String sizeMsg;
    
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER, targetClass = Status.class)
    private List<Status> status;

    public DescriptionOfMail() {
        super();
    }

    public DescriptionOfMail(long time, String type,
            String sender, String recipient, String subject, String file,
            String size, List<Status> status) {
        super();
        this.timeMS = time;
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.file = file;
        this.sizeMsg = size;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeMS() {
        return timeMS;
    }

    public void setTimeMS(long time) {
        this.timeMS = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSizeMsg() {
        return sizeMsg;
    }

    public void setSizeMsg(String size) {
        this.sizeMsg = size;
    }

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }
    
    @Override
    public boolean equals(Object other){
        if ((other!=null) && (getClass()==other.getClass())){
        DescriptionOfMail dm = (DescriptionOfMail) other;
        return (this.timeMS==dm.getTimeMS())&(this.sender.equals(dm.getSender()))&(this.recipient.equals(dm.getRecipient()))&(this.file.equals(dm.getFile()));
        }else{
            return false;
        }
        
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 37 * hash + Objects.hashCode(this.subject);
        return hash;
    }
    
    @Override
    public String toString(){
        String result = "ID: " +id + ", TIME: " + timeMS+", TYPE: "+type +", SENDER: "+sender+", RECIPIENT: " +recipient+", SUBJECT: "+subject+" FILE: "+file+" SIZE: " +sizeMsg + " STATUS: ";
        for(Status st:status){
            result = result + " " + st.toString();
        }
        return result;
    }
    
}
