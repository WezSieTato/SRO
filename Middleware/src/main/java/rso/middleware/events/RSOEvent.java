package rso.middleware.events;

import java.util.Date;

/**
 * Created by modz on 2015-04-29.
 */
public class RSOEvent {

    private long id;

    private final long timestamp;

    private String sender;

    private String event;

    private Object object;

    public RSOEvent(String sender, String event, Object object) {
        id = id++;
        timestamp = new Date().getTime();
        this.sender = sender;
        this.event = event;
        this.object = object;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RSOEvent) {
            RSOEvent that = (RSOEvent) o;
            return (sender == null || that.sender == null || sender.equals(that.sender))
                    && (event == null || that.event == null || event.equals(that.event));
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (event == null ? 0 : event.hashCode());
        result = prime * result + (sender == null ? 0 : sender.hashCode());
        return result;
    }
}
