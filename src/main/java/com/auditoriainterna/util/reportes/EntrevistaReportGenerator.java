package com.auditoriainterna.util.reportes;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.auditoriainterna.model.Entrevista;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;


@Service
public class EntrevistaReportGenerator {
	public byte[] exportToPdf(List<Entrevista> list,Map<String, Object> params) throws JRException, FileNotFoundException {
        return JasperExportManager.exportReportToPdf(getReport(list,params));
    }

    public byte[] exportToXls(List<Entrevista> list,Map<String, Object> params) throws JRException, FileNotFoundException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        SimpleOutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput(byteArray);
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(getReport(list,params)));
        exporter.setExporterOutput(output);
        exporter.exportReport();
        output.close();
        return byteArray.toByteArray();
    }

    private JasperPrint getReport(List<Entrevista> list,Map<String, Object> params) throws FileNotFoundException, JRException {
    	// Crear la lista de entrevistas directamente en el método
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        // Ruta al archivo JRXML
        String jrxmlFile = "classpath:entrevista.jrxml";

        // Llenar el informe sin necesidad de un parámetro
        JasperPrint report = JasperFillManager.fillReport(
            JasperCompileManager.compileReport(
                ResourceUtils.getFile(jrxmlFile).getAbsolutePath()),
            params, // No se necesitan parámetros en este caso
            dataSource
        );
        
        return report;
    }
}
