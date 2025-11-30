package com.example.museo_v2.service;

import com.example.museo_v2.model.Reserva;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

/**
 * Servicio encargado de generar documentos PDF relacionados con reservas.
 * Actualmente permite crear un ticket de entrada en formato A5.
 */
@Service
public class PdfService {

    /**
     * Genera un archivo PDF que representa el ticket correspondiente a una reserva.
     * Incluye información del evento, sala, titular y monto pagado.
     *
     * @param reserva reserva para la cual se generará el ticket
     * @return flujo de bytes del PDF ya generado
     */
    public ByteArrayInputStream generarTicketPdf(Reserva reserva) {
        Document document = new Document(PageSize.A5);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Fuentes utilizadas
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.NORMAL);
            Font subtituloFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
            Font cuerpoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
            Font pieFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Font.NORMAL);

            // Encabezado
            Paragraph titulo = new Paragraph("MUSEO TUMBAS REALES", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            Paragraph subtitulo = new Paragraph("Ticket de Entrada", pieFont);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(20);
            document.add(subtitulo);

            document.add(new Paragraph("----------------------------------------------------------------"));

            // Información del evento
            document.add(new Paragraph("Evento: " + reserva.getEvento().getNombre(), subtituloFont));

            DateTimeFormatter fechaFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm");

            document.add(new Paragraph("Fecha: " + reserva.getEvento().getFechaInicio().format(fechaFmt), cuerpoFont));
            document.add(new Paragraph("Hora: " + reserva.getEvento().getHoraInicio().format(horaFmt), cuerpoFont));

            String sala = (reserva.getEvento().getSala() != null)
                    ? reserva.getEvento().getSala().getNombre()
                    : "Por definir";
            document.add(new Paragraph("Sala: " + sala, cuerpoFont));

            document.add(Chunk.NEWLINE);

            // Información de la reserva
            document.add(new Paragraph("Titular: " + reserva.getNombreCompleto(), cuerpoFont));
            document.add(new Paragraph("DNI/RUC: " + reserva.getDniRuc(), cuerpoFont));
            document.add(new Paragraph("Entradas: " + reserva.getNEntradas(), cuerpoFont));
            document.add(new Paragraph("Total Pagado: S/ " + reserva.getTotalPagar(), subtituloFont));

            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("----------------------------------------------------------------"));

            // Pie de página
            Paragraph footer = new Paragraph(
                    "Código de Reserva: #" + reserva.getId() +
                    "\nPor favor presente este ticket en la entrada.",
                    pieFont
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(10);
            document.add(footer);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
