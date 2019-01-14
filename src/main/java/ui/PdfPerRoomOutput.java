package ui;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import domain.Reservation;
import domain.Room;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PdfPerRoomOutput {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);

    private Document doc = new Document();

    public PdfPerRoomOutput(String filePath) {
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
        } catch (DocumentException | FileNotFoundException e) {
            throw new UiException(e.getMessage());
        }
        doc.open();
    }

    public void write(Room room, List<Reservation> reservations) {
        Paragraph title = new Paragraph("Lokaal " + room.getNumber() + " : " + room.getName(), TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        Paragraph date = new Paragraph(new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()), HEADER_FONT);
        date.setAlignment(Element.ALIGN_CENTER);
        Paragraph info = new Paragraph(room.getCapacity() + " chairs, " + room.getPlugCount() + " plugs", HEADER_FONT);
        info.setAlignment(Element.ALIGN_CENTER);
        try {
            doc.add(title);
            addEmptyLine(doc, 1);
            doc.add(date);
            addEmptyLine(doc, 1);
            doc.add(info);
            addEmptyLine(doc, 2);
        } catch (DocumentException e) {
            throw new UiException(e.getMessage());
        }

        PdfPTable table = new PdfPTable(new float[]{2, 2, 6});
        table.setHeaderRows(1);
        table.addCell(new Phrase("Start", HEADER_FONT));
        table.addCell(new Phrase("Einde", HEADER_FONT));
        table.addCell(new Phrase("Naam", HEADER_FONT));

        for (Reservation reservation : reservations) {
            table.addCell(reservation.getStartString());
            table.addCell(reservation.getEndString());
            table.addCell(reservation.getRegistrar());
        }

        try {
            doc.add(table);
        } catch (DocumentException e) {
            throw new UiException(e.getMessage());
        }

        doc.newPage();
    }

    public void close() {
        doc.close();
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static void addEmptyLine(Paragraph p) {
        addEmptyLine(p, 1);
    }

    private static void addEmptyLine(Document doc, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            doc.add(new Paragraph(" "));
        }
    }

    private static void addEmptyLine(Document doc) throws DocumentException {
        addEmptyLine(doc, 1);
    }
}
