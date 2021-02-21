package com.proyecto.flowmanagement.backend.commun;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

public class XMLValidaations {

    public String validarXDSMessage( String textoAValidar)
    {
        String error = "";

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            xr.parse(new InputSource(new StringReader(textoAValidar)));
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            error = ex.getMessage();
        }

        return error;
    }
}
