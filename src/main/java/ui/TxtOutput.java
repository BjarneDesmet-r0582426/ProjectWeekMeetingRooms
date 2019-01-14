package ui;

import domain.Reservation;
import domain.Room;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class TxtOutput {
    private final PrintWriter writer;

    public TxtOutput(String filePath) {
        try {
            writer = new PrintWriter(filePath, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new UiException(e.getMessage());
        }

        writer.println("Lokaal     | start      | einde");
        writer.println("----------------------------------------------");
    }

    public void write(Room room, List<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            writer.printf("%10.10s | %10.10s | %s\n", room.getName(), reservation.getStartString(), reservation.getEndString());
        }
    }

    public void close() {
        writer.close();
    }
}
