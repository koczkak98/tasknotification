package com.tasknotification.tasknotification.controller.sendemail;

import com.tasknotification.tasknotification.db.SendEmailDao;
import com.tasknotification.tasknotification.db.SubordinateDao;
import com.tasknotification.tasknotification.db.TaskDao;
import com.tasknotification.tasknotification.model.base.*;
import com.tasknotification.tasknotification.model.sendemail.SendEmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SendEmailBc {
    protected List<TaskBase>   notFulfilledTasksMustSendEmail;
    @Autowired
    protected TaskDao          taskRepo                      ;
    @Autowired
    protected SubordinateDao   subordinateRepo               ;
    @Autowired
    protected SendEmailDao     sendEmailRepo                 ;

    public SendEmailBc() {
        this.notFulfilledTasksMustSendEmail = new ArrayList<>()    ;
    }

    @Async
    public void sendEmail()
    {
        List<SendEmailRequest> l;
        SendEmailRequest       r;

        try {
            l = getSendEmailRequest();
            for (int i = 0; i < l.size(); i ++) {
                r = l.get(i);
                MailBc.sendEmail(r.getSender(), r.getAddressee(), r.getLetter());

            }
        } catch (Exception e) {

        }

    }

    protected List<SendEmailRequest> getSendEmailRequest()
            throws ParseException { return createSendEmailRequest();}

    protected List<SendEmailRequest> createSendEmailRequest()
            throws ParseException
    {
        List<SendEmailRequest> l;
        SendEmailRequest       r;

        l = new ArrayList<>();
        r = new SendEmailRequest();

        for (int i = 0; i < findNotFulfilledTasks().size(); i++) {
            r.setSender("sender");
            r.setAddressee(subordinateRepo.findById(findNotFulfilledTasks().get(i).getSubordinateId().getId()).getEmailAddress());
            l.add(r);
        }
        return l;
    }

    protected List<SendEmailBase> findNotFulfilledTasks()
            throws ParseException
    {
        List<TaskBase>   task;
        List<SendEmailBase> e;

        task = taskRepo.findAll();
        e = new ArrayList<>();

        if (task != null) {
            if (!task.isEmpty()) {
                for (TaskBase t : task) {
                    if (t.getSendingEmailIds().isEmpty() || t.getDone() == 1) {
                        if (isLastEmailSendingInGracePeriod(t)) {
                            notFulfilledTasksMustSendEmail.add(t);
                            e = getMustEmailSending(t);
                        }
                    }
                }
            }
        }
        return e;
    }

    protected boolean isLastEmailSendingInGracePeriod(TaskBase t)
            throws ParseException { return getMustEmailSending(t).size() == 0;}

    protected List<SendEmailBase> getMustEmailSending(TaskBase t)
            throws ParseException { return checkGracePeriodOfLastEmailsSending(t);}

    protected List<SendEmailBase> checkGracePeriodOfLastEmailsSending(TaskBase t)
            throws ParseException
    {
        List<SendEmailBase> l;
        List<SendEmailBase> e;

        l = findSendEmailsByTask(t);
        e = new ArrayList<>();

        for (SendEmailBase sendEmailBase : l) {
            if (!isInGracePeriod(sendEmailBase, t)) {
                e.add(sendEmailBase);
            }
        }
        return e;
    }

    protected boolean isInGracePeriod(SendEmailBase e, TaskBase t)
            throws ParseException
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(e.getDateTime()).getTime() < System.currentTimeMillis() - (t.getGracePeriod() * 1000 * 60 * 60);
    }

    protected List<SendEmailBase> findSendEmailsByTask(TaskBase t)
    {
        List<SendEmailBase> l;
        SendEmailBase       e;

        l = new ArrayList<>();

        for (int i = 0; i < t.getSendingEmailIds().size(); i++) {
            e = sendEmailRepo.findById(t.getSendingEmailIds().get(i));
            l.add(e);
        }

        return l;
    }
}
