/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadorpdfindividual;

import BE.paginadoBE;
import UTIL.constantes;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mcotrina
 */
public class ThreadPdfGenerador extends Thread {
    private List<paginadoBE> listapaginas;
    private int paginaInicio;
    private int paginaFin;
    //private PdfReader pdfReader;
    private String op;
    private String pdf;
    private int h;

    public ThreadPdfGenerador(List<paginadoBE> listapaginas, int paginaInicio, int paginaFin, String pdf,String Op,int h) {
        this.listapaginas = listapaginas;
        this.paginaInicio = paginaInicio;
        this.paginaFin = paginaFin;
        this.pdf = pdf;
        this.h =h;
        //this.pdfReader =pdfreader;
        this.op =Op;
    }

    @Override
    public void run(){
        Date d = new Date();
        System.out.println("Hilo "+paginaInicio+" "+paginaFin+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds());
        for(int i=paginaInicio;paginaFin>=i;i++){
            try 
            {
            /*    RandomAccessSource rd = new RandomAccessSourceFactory().setForceRead(false).setUsePlainRandomAccess(true).createBestSource(pdf);
        RandomAccessFileOrArray ra = new RandomAccessFileOrArray(rd);
        PdfReader pdfreader = new PdfReader(ra,null);
              */
                  
                
                
                PdfReader pdfreader = new PdfReader(new constantes().RUTA+"\\"+op+"\\adjunto\\Global_Opt"+this.h+".pdf");
                //PdfReader pdfreader = new PdfReader(new RandomAccessFileOrArray(pdf),null);    
                Document document = new Document(pdfreader.getPageSizeWithRotation(1));
                PdfSmartCopy writer = null;
                writer = new PdfSmartCopy(document, new FileOutputStream(new constantes().RUTA+"\\"+op+"\\adjunto\\Global\\"+listapaginas.get(i).getNombrePDF()));
                //writer = new PdfSmartCopy(document, new FileOutputStream("D:\\adjunto\\Global\\"+listapaginas.get(i).getNombrePDF()));
                writer.setPdfVersion(PdfWriter.PDF_VERSION_1_6);
                //writer.setFullCompression();
                if(!listapaginas.get(i).getPassword().equals("-")){
                        writer.setEncryption(listapaginas.get(i).getPassword().getBytes(),
                        listapaginas.get(i).getPassword().getBytes(),
                        PdfWriter.ALLOW_SCREENREADERS,PdfWriter.DO_NOT_ENCRYPT_METADATA);
                }
                    
                document.addAuthor("DATAIMAGENES S.A.C.");
                document.addCreator("DATAIMAGENES S.A.C.");
                writer.setPdfVersion(PdfCopy.PDF_VERSION_1_6);
                document.open();
                PdfImportedPage page;
                for(int j=listapaginas.get(i).getPaginaInicio();listapaginas.get(i).getPaginaFin()>=j;j++){
                        //System.out.println("sub  "+listapaginas.get(i).getPaginaInicio()+" -  "+listapaginas.get(i).getPaginaFin());
                    page = writer.getImportedPage(pdfreader, j);
                    writer.addPage(page);
                }    
                writer.close();
                writer=null;
                document.close();
                document =null;
            } catch (FileNotFoundException | DocumentException ex ) {
                Logger.getLogger(ThreadPdfGenerador.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ThreadPdfGenerador.class.getName()).log(Level.SEVERE, null, ex);
            }  
            
        }
        
        d = new Date();
        System.out.println("Fin de proceso: "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds());
        //d = new Date();
        //System.out.println("Hora"+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds());
    }
}
