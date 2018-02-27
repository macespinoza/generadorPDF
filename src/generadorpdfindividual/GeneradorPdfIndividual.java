/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadorpdfindividual;

import BE.paginadoBE;
import BL.paginadoBL;
import UTIL.constantes;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author mcotrina
 */
public class GeneradorPdfIndividual {

    /**
     * @param args the command line arguments+
     */
    public static void main(String[] args) throws IOException, DocumentException {
        
        if (args.length > 0) {
            if (args[0] != null) {
                escribirArchivo("Bien: Obtuve un argumento y es este: " + args[0], args[0]);
                String error = generarLista(args[0]);
                String error2 = "";

                if (error.equalsIgnoreCase(new constantes().CORRECTO)) {
                    error2 = new paginadoBL().actualizarEstadoSplitPdf(Integer.parseInt(args[0]), new constantes().TERMINADO_I, new constantes().TERMINADO);
                } else {
                    int primero = error.indexOf(".") + 1;
                    error2 = new paginadoBL().actualizarEstadoSplitPdf(Integer.parseInt(args[0]), new constantes().ERROR_I, new constantes().ERROR + ": " + error.substring(primero, error.length()));
                }

                if (!error2.equalsIgnoreCase(new constantes().CORRECTO)) {
                    int primero = error2.indexOf(".") + 1;
                    escribirArchivo("Ocurrio un erro al actualizar proceso, pero todo salio bien, la op es: " + args[0] + ", fue el error fue: " + error2.substring(primero, error2.length()), args[0]);
                }

            } else {
                escribirArchivo("Error: Se recibió el argumento en null", args[0]);
            }
        } else {
            escribirArchivo("Error: No se recibió ningun argumento", args[0]);
        }
    }

    public static String generarLista(String op) throws IOException, DocumentException {

        String error = new constantes().CORRECTO;

        Date d = new Date();
        System.out.println("Hora de Inicio: " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
        escribirArchivo("Hora de Inicio: " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds(), op);
        List<paginadoBE> listapaginas = new paginadoBL().obtenerRegistros(op);
        System.out.println("Obtener Fichero PDF");
        String pdf = new constantes().RUTA + "\\" + op + "\\adjunto\\Global_Opt.pdf";

        File folder = new File(new constantes().RUTA + "\\" + op + "\\adjunto\\Global");
        folder.mkdirs();

        System.out.println("Leer PDF Global");
        PdfReader pdfreader = new PdfReader(pdf);

        //TextExtractionStrategy strategy;
        System.out.println("Iniciar Spliteo PDF de " + listapaginas.size());
        d = new Date();
        System.out.println("Hora de fin: " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());

        int paginaInicio = 0;
        int paginaFin = listapaginas.size() - 1;

        for (int i = paginaInicio; paginaFin >= i; i++) {

            try {

                Document document = new Document(pdfreader.getPageSizeWithRotation(1));
                PdfSmartCopy writer = null;

                writer = new PdfSmartCopy(document, new FileOutputStream(new constantes().RUTA + "\\" + op + "\\adjunto\\Global\\" + listapaginas.get(i).getNombrePDF()));
                writer.setPdfVersion(PdfWriter.PDF_VERSION_1_6);
                //writer.setFullCompression();

                if (!listapaginas.get(i).getPassword().equals("-")) {
                    writer.setEncryption(listapaginas.get(i).getPassword().getBytes(),
                            listapaginas.get(i).getPassword().getBytes(),
                            PdfWriter.ALLOW_SCREENREADERS, PdfWriter.DO_NOT_ENCRYPT_METADATA);
                }

                document.addAuthor("DATAIMAGENES S.A.C.");
                document.addCreator("DATAIMAGENES S.A.C.");
                writer.setPdfVersion(PdfCopy.PDF_VERSION_1_6);
                document.open();
                PdfImportedPage page;

                for (int j = listapaginas.get(i).getPaginaInicio(); listapaginas.get(i).getPaginaFin() >= j; j++) {
                    page = writer.getImportedPage(pdfreader, j);
                    writer.addPage(page);

                }

                writer.close();
                writer = null;
                document.close();
                document = null;

            } catch (FileNotFoundException | DocumentException ex) {
                error = new constantes().ERROR + "." + ex;
                Logger.getLogger(ThreadPdfGenerador.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        d = new Date();
        System.out.println("Hora de fin: " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
        escribirArchivo("Hora de fin: " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds(), op);

        return error;

    }

    public static void generarLista2(String op) throws IOException, DocumentException {

        Date d = new Date();
        System.out.println("Hora de Inicio: " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
        List<paginadoBE> listapaginas = new paginadoBL().obtenerRegistros(op);
        System.out.println("Obtener Fichero PDF");
        String pdf = new constantes().RUTA + "\\" + op + "\\adjunto\\Global_Opt.pdf";

        File folder = new File(new constantes().RUTA + "\\" + op + "\\adjunto\\Global");
        folder.mkdirs();

        System.out.println("Generar PDF Individuales");

        //Tiempo de optimizar
        //numero de hilos
        int listadePaginas = listapaginas.size() + 1;
        int tamanioDeHilo = 2;
        int PrimerHilo = (int) Math.floor(listadePaginas / tamanioDeHilo);
        int segundoHilo = (int) Math.floor(listadePaginas / tamanioDeHilo) + 1;
        System.out.println("Generando:  " + listapaginas.size() + " Pdf's");

        int paginaInicio = 0;
        int paginaFin = listapaginas.size() - 1;

        File origen = new File(new constantes().RUTA + "\\" + op + "\\adjunto\\Global_Opt.pdf");
        File destino = new File(new constantes().RUTA + "\\" + op + "\\adjunto\\Global_Opt1.pdf");
        InputStream in = new FileInputStream(origen);
        OutputStream out = new FileOutputStream(destino);
        byte[] buf = new byte[1024];
        int len;

        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

        File origen1 = new File(new constantes().RUTA + "\\" + op + "\\adjunto\\Global_Opt.pdf");
        File destino1 = new File(new constantes().RUTA + "\\" + op + "\\adjunto\\Global_Opt2.pdf");
        InputStream in1 = new FileInputStream(origen1);
        OutputStream out1 = new FileOutputStream(destino1);
        byte[] buf1 = new byte[1024];
        int len1;

        while ((len1 = in1.read(buf1)) > 0) {
            out1.write(buf1, 0, len1);
        }
        in1.close();
        out1.close();

        //Hilo
        ThreadPdfGenerador hiloGenerador = new ThreadPdfGenerador(listapaginas, 0, PrimerHilo - 1, pdf, op, 1);
        hiloGenerador.start();

        ThreadPdfGenerador segundoGenerador = new ThreadPdfGenerador(listapaginas, PrimerHilo, paginaFin, pdf, op, 2);
        segundoGenerador.start();
        //hilo

        d = new Date();
        System.out.println("Hora de fin: " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());

    }

    public static void compresionPDF(String origen) throws IOException, DocumentException {

        PdfReader reader = new PdfReader(origen);
        int n = reader.getXrefSize();
        System.out.println("valorN: " + n);
        PdfObject object;
        PRStream stream;
        for (int i = 0; i < n; i++) {
            object = reader.getPdfObject(i);
            if (object == null || !object.isStream()) {
                continue;
            }

            stream = (PRStream) object;
            System.out.println("F1 " + PdfName.DCTDECODE.toString() + " | " + PdfName.IMAGE.toString() + " | " + stream.getAsName(PdfName.SUBTYPE) + " | " + stream.getAsName(PdfName.FILTER));
            if (!PdfName.IMAGE.equals(stream.getAsName(PdfName.SUBTYPE))) {
                continue;
            }
            System.out.println("F2 " + PdfName.DCTDECODE + " | " + PdfName.FILTER);
            if (!PdfName.DCTDECODE.equals(stream.getAsName(PdfName.FILTER))) {
                continue;
            }
            System.out.println("F3 ");
            PdfImageObject image = new PdfImageObject(stream);
            BufferedImage bi = image.getBufferedImage();
            if (bi == null) {
                continue;
            }
            System.out.println("Ancho: " + bi.getWidth() + " alto: " + bi.getHeight());
            int width = (int) (bi.getWidth() * 1f);
            int height = (int) (bi.getHeight() * 1f);
            System.out.println("XAncho: " + width + " Xalto: " + height);
            if (width <= 0 || height <= 0) {
                continue;
            }
            System.out.println("F5 ");
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            AffineTransform at = AffineTransform.getScaleInstance(1f, 1f);
            Graphics2D g = img.createGraphics();
            g.drawRenderedImage(bi, at);
            ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
            ImageIO.write(img, "JPG", imgBytes);
            stream.clear();
            stream.setData(imgBytes.toByteArray(), false, PRStream.NO_COMPRESSION);
            stream.put(PdfName.TYPE, PdfName.XOBJECT);
            stream.put(PdfName.SUBTYPE, PdfName.IMAGE);
            stream.put(PdfName.FILTER, PdfName.DCTDECODE);
            stream.put(PdfName.WIDTH, new PdfNumber(width));
            stream.put(PdfName.HEIGHT, new PdfNumber(height));
            stream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
            stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
        }
        reader.removeUnusedObjects();
        // Guardamos el documento modificado
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(origen.replace("Global", "Global_")));
        Map<String, String> info = reader.getInfo();
        info.put("Title", "");
        info.put("Subject", "");
        info.put("Keywords", "");
        info.put("Creator", "DATAIMAGENES S.A.C.");
        info.put("Author", "DATAIMAGENES S.A.C.");
        stamper.setMoreInfo(info);
        stamper.setFullCompression();
        stamper.close();
        reader.close();

    }

    public static void escribirArchivo(String texto, String op) {

        File reporte = null;
        FileWriter fw = null;
        PrintWriter pw = null;

        try {

            reporte = new File(new constantes().RUTA + File.separator + op + File.separator + "archivo_log.txt");

            if (reporte.exists()) {
                fw = new FileWriter(reporte, true);
            } else {
                reporte.createNewFile();
                fw = new FileWriter(reporte);
            }

            pw = new PrintWriter(fw);
            pw.println(texto);

            pw.close();

        } catch (FileNotFoundException ex) {
            System.err.println("ERROR: " + ex);
        } catch (UnsupportedEncodingException ex) {
            System.err.println("ERROR: " + ex);
        } catch (IOException ex) {
            Logger.getLogger(GeneradorPdfIndividual.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                if (null != fw) {
                    fw.close();
                }

            } catch (Exception e) {
            }

        }

    }

}
