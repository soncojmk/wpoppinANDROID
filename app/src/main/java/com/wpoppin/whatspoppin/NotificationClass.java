package com.wpoppin.whatspoppin;

/**
 * Created by joseph on 6/3/2017.
 * Notifications class
 */

public class NotificationClass {

    public String actor;
    public String verb;
    public EventClass action_object_event;
    public User actor_account;
    public boolean unread;


    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public EventClass getAction_object_event() {
        return action_object_event;
    }

    public void setAction_object_event(EventClass action_object_event) {
        this.action_object_event = action_object_event;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public User getActor_account() {
        return actor_account;
    }

    public void setActor_account(User actor_account) {
        this.actor_account = actor_account;
    }


}
