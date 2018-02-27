/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BE.paginadoBE;
import UTIL.constantes;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mcotrina
 */
public class basePaginadoDAO extends ConexionDAO {

    public List<paginadoBE> obtenerRegistros(String ordenPreso) throws FileNotFoundException, IOException {

        String cadena = "";
        String[] cadenaSpliteada;

        List<paginadoBE> listadoPaginas = new ArrayList<paginadoBE>();
        FileReader fichero = new FileReader(new constantes().RUTA + "\\" + ordenPreso + "\\listaPDF_" + ordenPreso + ".txt");
        BufferedReader buffer = new BufferedReader(fichero);
        while ((cadena = buffer.readLine()) != null) {
            cadenaSpliteada = cadena.split("\t");
            paginadoBE linea = new paginadoBE();
            linea.setCodigoBarras(cadenaSpliteada[0]);
            linea.setPaginaInicio(Integer.parseInt(cadenaSpliteada[1]));
            linea.setPaginaFin(Integer.parseInt(cadenaSpliteada[2]));
            linea.setPassword(cadenaSpliteada[3]);
            linea.setNombrePDF(cadenaSpliteada[4]);
            if (cadenaSpliteada[3].equals("-")) {
                linea.setEstado(0);
            } else {
                linea.setEstado(1);
            }
            //System.out.println(cadenaSpliteada[0]+" "+cadenaSpliteada[1]+" "+cadenaSpliteada[2]+" "+cadenaSpliteada[3]);
            listadoPaginas.add(linea);
        }

        return listadoPaginas;

    }

    public String actualizoFlagProcesoSplitPDF(int op, int estado, String descripcion) {

        String error = new constantes().CORRECTO;

        String sql1 = "INSERT INTO DE_PROCESAMIENTO_FASE_ESTADO (orden_proceso,id_fase,id_estado,correlativo,descripcion,fecha)"
                + " VALUES (" + op + ",5," + estado + ",1,'" + descripcion + "',GETDATE())";

        try {

            abrirConexionDDEE();
            scripUpdate(sql1);
            cerrarConexionDDEE();

        } catch (Exception e) {
            System.err.println("error: " + e);
            error = new constantes().ERROR + "." + e;
        }

        return error;

    }

}
