package db;

import domain.Reservation;
import domain.Room;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ConnectingIdType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.ImpersonatedUserId;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.Mailbox;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;

import java.util.*;

public class ReservationsDBExchange extends ReservationsDB {
    private static final WebCredentials credentials = new WebCredentials("sa_uurrooster", "JLxkK4BDUre3");
    private final ExchangeService service;
    private final Map<String, CalendarFolder> folders;

    public ReservationsDBExchange() {
        service = new ExchangeService();
        service.setCredentials(credentials);

        folders = new HashMap<>();
    }

    @Override
    public synchronized List<Reservation> getAllReservations(Room room) {
        try {
            setRoom(room.getEmail());

            // Read calendar of room
            CalendarFolder calendarFolder = getCalendarFolder(room.getEmail());

            Calendar weekAgo = Calendar.getInstance();
            weekAgo.add(Calendar.WEEK_OF_YEAR, -1);
            Calendar twoWeeksAhead = Calendar.getInstance();
            twoWeeksAhead.add(Calendar.WEEK_OF_YEAR, 2);

            CalendarView calendarView = new CalendarView(weekAgo.getTime(), twoWeeksAhead.getTime());
            FindItemsResults<Appointment> findResults = calendarFolder.findAppointments(calendarView);

            List<Reservation> reservations = new ArrayList<>();
            for (Appointment appt : findResults.getItems()) {
                appt.load(PropertySet.FirstClassProperties);
                reservations.add(new Reservation(room, appt.getSubject(), appt.getStart(), appt.getEnd()));
            }

            return reservations;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get reservations", e);
        }
    }

    @Override
    public void close() {
        service.close();
    }

    private void setRoom(String email) throws Exception {
        ImpersonatedUserId impersonatedUserId = new ImpersonatedUserId(ConnectingIdType.SmtpAddress, email);
        service.setImpersonatedUserId(impersonatedUserId);
        service.autodiscoverUrl(email);
    }

    private CalendarFolder getCalendarFolder(String email) throws Exception {
        if (folders.containsKey(email)) {
            return folders.get(email);
        } else {
            Mailbox mailbox = new Mailbox(email);
            FolderId folderId = new FolderId(WellKnownFolderName.Calendar, mailbox);
            CalendarFolder calendarFolder = CalendarFolder.bind(service, folderId);
            folders.put(email, calendarFolder);

            return calendarFolder;
        }
    }
}