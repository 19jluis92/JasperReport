/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JasperPDF;

/**
 *
 * @author jluis
 */
//import com.newrelic.api.agent.NewRelic;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import net.sf.jasperreports.components.headertoolbar.resources.images.format.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.commons.lang.RandomStringUtils;
//import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.util.IOUtils;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;

import mx.udg.mxc.commons.model.implementacion.ActaEntregaRecepcion;
import mx.udg.mxc.jake.security.SessionManager;;


public class PDF {

//    private final String SubReportDir = "/com/facturasi/template/";
//    private final String rootTemplate;
//    private final String templateName = "default.jasper";
//    private final String urlFiles;
//    private String uuid;
//    private ActaEntregaRecepcion actaEntregaRecepcion;
//    private static JAXBContext contenido;
//    private TimbreFiscalDigital timbre;
//    private Pdf p;
//    private com.facturasi.models.PDF templatePDF;
    private BufferedImage image;
    private static BufferedImage footerImage;

    static {
        try {
//            contenido = JAXBContext.newInstance(ActaEntregaRecepcion.class);
            footerImage = ImageIO.read(PDF.class.getResource("logo.svg"));
        } catch (Exception ex) {
            System.out.println("Error " + ex);
        } 
    }

    public PDF() {
       
    }

    /**
     * Metodo  pdf
     *
     * @param jasperFile
     * @throws javax.xml.bind.JAXBException
     */
    public synchronized byte[] getReport( String jasperFile, Object data,Connection con,String hashId) throws JRException, JAXBException, IOException {


    	HashMap<String, Object> parametros = new HashMap<String, Object>();
        File pdf;
        pdf = new File( RandomStringUtils.randomAlphanumeric(15) + ".pdf");
       
        
        parametros.put("logo", image);
        parametros.put("hashId", hashId);
        setParameters(data,parametros);
		try {
			
		
        JasperPrint jasperPrint;
        jasperPrint = JasperFillManager.fillReport(new FileInputStream(jasperFile), parametros,con);
        
        JRExporter exporter = new JRPdfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, pdf);
        exporter.exportReport();

        FileInputStream inputStream = new FileInputStream(pdf);
        byte[] bytePDF = IOUtils.toByteArray(inputStream);
        inputStream.close();
        pdf.delete();

        return bytePDF;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    @SuppressWarnings("unused")
	private void setParameters(Object data, HashMap<String,Object> parametros){
    	try{
    		HashMap<String,Object> params=  (HashMap<String, Object>) data;
    		for(String key    : params.keySet())
    		{
    			parametros.put(key, params.get(key));
    		}
    	}catch(Exception e){

    	}	
    }

 
}
