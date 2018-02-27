/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BL;

import BE.paginadoBE;
import DAO.basePaginadoDAO;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author mcotrina
 */
public class paginadoBL {

    public List<paginadoBE> obtenerRegistros(String ordenPreso) throws IOException {
        return new basePaginadoDAO().obtenerRegistros(ordenPreso);
    }
    
    public String actualizarEstadoSplitPdf(int ordenPreso, int estado, String descripcion) throws IOException {        
        return new basePaginadoDAO().actualizoFlagProcesoSplitPDF(ordenPreso, estado, descripcion);
    }
}
