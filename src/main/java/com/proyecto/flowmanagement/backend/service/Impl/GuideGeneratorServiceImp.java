package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.def.XMLConstants;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Service
public class GuideGeneratorServiceImp {


    public void GuidePrint(Guide guide){

        try
        {
            File file = new File(XMLConstants.GUIDE_XML_LOCATION);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc = configureGuidXML(doc,guide);

            doc = configureStepsXML(doc, guide);
            
            doc = configureAlternativesXML(doc, guide);

            doc = configureOperationsXML(doc, guide);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    private Document configureGuidXML(Document doc, Guide guide) {
        Node mainStepID = doc.getElementsByTagName(XMLConstants.MAIN_STEP_ID).item(0);
        mainStepID.setTextContent(guide.getSteps().get(0).getId().toString());
        return doc;
    }

    private Document configureStepsXML(Document doc, Guide guide) {
        try
        {

            for (Step step: guide.getSteps())
            {
                File file = new File(XMLConstants.STEP_XML_LOCATION);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document docStep = dBuilder.parse(file);

                Node idStep = doc.getElementsByTagName(XMLConstants.STEP_ID).item(0);
                Node labelStep = doc.getElementsByTagName(XMLConstants.STEP_LABEL).item(0);
                Node textStep = doc.getElementsByTagName(XMLConstants.STEP_TEXT).item(0);

                idStep.setTextContent(step.getId().toString());
                labelStep.setTextContent(step.getLabel());
                textStep.setTextContent(step.getText());

                Node newStep = doc.getElementsByTagName(XMLConstants.STEP_ELEMENT).item(0);
                doc.getElementsByTagName(XMLConstants.GUIDE_ELEMENT).item(0).appendChild(newStep);
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        return doc;
    }

    private Document configureOperationsXML(Document doc, Guide guide) {
        return doc;
    }

    private Document configureAlternativesXML(Document doc, Guide guide) {
        return doc;
    }

}
