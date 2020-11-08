package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.def.XMLConstants;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Scanner;

@Service
public class GuideGeneratorServiceImp {


    public void GuidePrint(Guide guide){

        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(XMLConstants.GUIDE_XML_LOCATION);

            Node mainStepID = doc.getElementsByTagName("mainStepId").item(0);

            mainStepID.setTextContent(guide.getSteps().get(0).getId().toString());
        }
        catch(Exception ex)
        {
            File guideXML = new File(XMLConstants.GUIDE_XML_LOCATION);
        }
    }
}
