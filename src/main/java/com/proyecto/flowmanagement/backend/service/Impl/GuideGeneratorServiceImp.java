package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.def.XMLConstants;
import com.proyecto.flowmanagement.backend.persistence.entity.*;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

            doc = configureOperationsXML(doc, guide);

            printResult(doc);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    private void printResult(Document doc) throws IOException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(XMLConstants.GUIDE_RESULT));
        transformer.transform(domSource, streamResult);
    }

    private Document configureGuidXML(Document doc, Guide guide) {
        Node guideHeader = doc.getElementsByTagName(XMLConstants.GUIDE_ELEMENT).item(0);
        Node mainStepID = doc.getElementsByTagName(XMLConstants.MAIN_STEP_ID).item(0);

        NamedNodeMap guideHeaderAttributes = guideHeader.getAttributes();

        Node xmlnsvc = guideHeaderAttributes.getNamedItem("xmlns:vc");
        Node xmlnsttg = guideHeaderAttributes.getNamedItem("xmlns:ttg");
        Node xmlnsxsi = guideHeaderAttributes.getNamedItem("xmlns:xsi");
        Node schemaLocation = guideHeaderAttributes.getNamedItem("xmlns:schemaLocation");

        xmlnsvc.setTextContent(XMLConstants.XMLNS_VC);
        xmlnsttg.setTextContent(XMLConstants.XMLNS_TTG);
        xmlnsxsi.setTextContent(XMLConstants.XMLNS_XSI);
        schemaLocation.setTextContent(XMLConstants.XMLNS_SCHEMALOCATION);

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

                Node idStep = docStep.getElementsByTagName(XMLConstants.STEP_ID).item(0);
                Node labelStep = docStep.getElementsByTagName(XMLConstants.STEP_LABEL).item(0);
                Node textStep = docStep.getElementsByTagName(XMLConstants.STEP_TEXT).item(0);

                idStep.setTextContent(step.getId().toString());
                labelStep.setTextContent(step.getLabel());
                textStep.setTextContent(step.getText());

                Node newStep = docStep.getElementsByTagName(XMLConstants.STEP_ELEMENT).item(0);

                for (Alternative alternative : step.getAlternatives()) {
                    Node alt = configureStepAlternativesXML(doc, alternative);
                    newStep.appendChild(docStep.importNode(alt,true));
                }

                doc.getElementsByTagName(XMLConstants.GUIDE_ELEMENT).item(0).appendChild(doc.importNode(newStep,true));
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

    private Node configureStepAlternativesXML(Document doc, Alternative alternative) throws ParserConfigurationException, IOException, SAXException {

        File file = new File(XMLConstants.ALTERNATIVE_XML_LOCATION);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docAlternative = dBuilder.parse(file);

        Node idStep = docAlternative.getElementsByTagName(XMLConstants.ALTERNATIVE_STEP_ID).item(0);
        Node guideName = docAlternative.getElementsByTagName(XMLConstants.ALTERNATIVE_GUIDE_NAME).item(0);
        Node label = docAlternative.getElementsByTagName(XMLConstants.ALTERNATIVE_LABEL).item(0);

        idStep.setTextContent(alternative.getNextStep());
        guideName.setTextContent(alternative.getGuideName());
        label.setTextContent(alternative.getLabel());

        if(alternative.getConditions() != null)
        {
            Node unaryNode = getUnaryCondition(alternative.getConditions());
            Node locationAlternative = docAlternative.getElementsByTagName(XMLConstants.ALTERNATIVE).item(0);
            Node unaryImported = docAlternative.importNode(unaryNode,true);
            locationAlternative.appendChild(unaryImported);
        }

        if(alternative.getBinaryConditions() != null)
        {
            Node binaryCondition = getBinaryCondition(alternative.getBinaryConditions());
            Node locationAlternative = docAlternative.getElementsByTagName(XMLConstants.ALTERNATIVE).item(0);
            Node binaryImported = docAlternative.importNode(binaryCondition,true);
            locationAlternative.appendChild(binaryImported);
        }

        return docAlternative.getElementsByTagName(XMLConstants.ALTERNATIVE).item(0);
    }

    private Node getBinaryCondition(BinaryCondition binaryConditions) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(XMLConstants.BINARY_CONDITION_XML);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docBinary = dBuilder.parse(file);

        Node operator = docBinary.getElementsByTagName(XMLConstants.BINARY_OPERATOR).item(0);

        operator.setTextContent(binaryConditions.getOperator());

        int i = 1;

        for (UnaryCondition unaryCondition : binaryConditions.getConditions()) {
            Node unaryConditionNode = getUnaryCondition(unaryCondition);
            Node locationAlternative = docBinary.getElementsByTagName(XMLConstants.BINARY_CONDITION).item(0);
            Node unaryNodeImported = docBinary.importNode(unaryConditionNode,true);
            locationAlternative.appendChild(unaryNodeImported);
            docBinary.renameNode(unaryNodeImported, unaryNodeImported.getNamespaceURI(),XMLConstants.BINARY_OPERATORS);
            i++;
        }

        return docBinary.getElementsByTagName(XMLConstants.BINARY_CONDITION).item(0);
    }

    private Node getUnaryCondition(UnaryCondition condition) throws IOException, SAXException, ParserConfigurationException {
        File file = new File(XMLConstants.UNARY_CONDITION_XML);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docUnary = dBuilder.parse(file);

        Node name = docUnary.getElementsByTagName(XMLConstants.UNARY_CONDITION_OPERATION_NAME).item(0);
        Node field = docUnary.getElementsByTagName(XMLConstants.UNARY_CONDITION_FIELD).item(0);
        Node fieldType = docUnary.getElementsByTagName(XMLConstants.UNARY_CONDITION_FIELD_TYPE).item(0);
        Node operator = docUnary.getElementsByTagName(XMLConstants.UNARY_CONDITION_OPERATOR).item(0);
        Node value = docUnary.getElementsByTagName(XMLConstants.UNARY_CONDITION_VALUE).item(0);

        name.setTextContent(condition.getOperationName());
        field.setTextContent(condition.getConditionParameter().getField());
        fieldType.setTextContent(condition.getConditionParameter().getFieldType());
        operator.setTextContent(condition.getConditionParameter().getOperator());
        value.setTextContent(condition.getConditionParameter().getValue());

        Node conditionNode = docUnary.getElementsByTagName(XMLConstants.ALTERNATIVE_CONDITION).item(0);

        return conditionNode;
    }

}
