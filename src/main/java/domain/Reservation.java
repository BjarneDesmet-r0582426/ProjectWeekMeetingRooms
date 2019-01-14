package domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation {
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("HH:mm");
    private final Room room;
    private final String registar;
    private final Date start;
    private final Date end;

    public Reservation(Room room, String registar, Date start, Date end) {
        this.room = room;
        this.registar = registar;
        this.start = start;
        this.end = end;
    }

    public Room getRoom() {
        return room;
    }

    public String getRegistrar() {
        return registar;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getStartString() {
        return DATE_FORMATTER.format(start);
    }

    public String getEndString() {
        return DATE_FORMATTER.format(end);
    }
}
