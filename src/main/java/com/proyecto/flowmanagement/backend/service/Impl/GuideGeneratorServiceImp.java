package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.def.OperationType;
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

           String esto = "test";
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

        mainStepID.setTextContent(guide.getMainStep());
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

                idStep.setTextContent(step.getText());
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

    private Node configureOperationsXML(Document doc, Operation operation) throws ParserConfigurationException, IOException, SAXException {

        File file = new File(XMLConstants.STEP_XML_LOCATION);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docOperation = dBuilder.parse(file);

        if (operation.getOperationType() == OperationType.simpleOperation){
            SimpleOperation simpleOperation = (SimpleOperation) operation;
            file = new File(XMLConstants.SIMPLE_OPERATION_XML_LOCATION);
        }
        else if(operation.getOperationType() == OperationType.taskOperation){
            TaskOperation taskOperation = (TaskOperation) operation;
            file = new File(XMLConstants.TASK_OPERATION_XML_LOCATION);
        }

        Node nameOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_NAME).item(0);
        nameOperation.setTextContent(operation.getName());
        Node labelOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_LABEL).item(0);
        labelOperation.setTextContent(operation.getLabel());
        if (operation.getVisible() != null) {
            Node visibleOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_VISIBLE).item(0);
            visibleOperation.setTextContent(operation.getVisible().toString());
        }
        if (operation.getPreExecute() != null) {
            Node preExecuteOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_PRE_EXECUTE).item(0);
            preExecuteOperation.setTextContent(operation.getPreExecute().toString());
        }
        if (operation.getComment() != null) {
            Node commentOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_COMMENT).item(0);
            commentOperation.setTextContent(operation.getComment());
        }
        if (operation.getTitle() != null) {
            Node titleOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_TITLE).item(0);
            titleOperation.setTextContent(operation.getTitle());
        }
        if (operation.getAutomatic() != null) {
            Node automaticOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_AUTOMATIC).item(0);
            automaticOperation.setTextContent(operation.getAutomatic().toString());
        }
        if (operation.getPauseExecution() != null) {
            Node pauseExecutionOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_PAUSE_EXECUTION).item(0);
            pauseExecutionOperation.setTextContent(operation.getPauseExecution().toString());
        }
        if (operation.getOperationOrder() != null) {
            Node operationOrderOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_OPERATION_ORDER).item(0);
            operationOrderOperation.setTextContent(operation.getOperationOrder().toString());
        }
        Node operationTypeOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_OPERATION_TYPE).item(0);
        operationTypeOperation.setTextContent(operation.getOperationType().name());

        //inParameters
        //OutParameters
        //Conditions

        if (operation.getNotifyAlternative() != null) {
            Node notifyAlternativeOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_ALTERNATIVE).item(0);
            notifyAlternativeOperation.setTextContent(operation.getNotifyAlternative().toString());

            for (Alternative alternative : operation.getAlternativeIds()){
                Node alternativeIdsOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ALTERNATIVE_IDS).item(0);
                alternativeIdsOperation.setTextContent(alternative.getNextStep());
            }
        }

        if (operation.getNotifyOperation() != null) {
            Node notifyOperationOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_OPERATION).item(0);
            notifyOperationOperation.setTextContent(operation.getNotifyOperation().toString());

            for (Operation operation1 : operation.getOperationNotifyIds()){
                Node operationIdsOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_OPERATION_IDS).item(0);
                operationIdsOperation.setTextContent(operation1.getName());
            }
        }

        if (operation.getNotifyOperationDelay() != null) {
            Node notifyOperationDelayOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_OPERATION_DELAY).item(0);
            notifyOperationDelayOperation.setTextContent(operation.getNotifyOperationDelay().toString());
        }

        if (operation.getOperationType() == OperationType.simpleOperation){
            SimpleOperation simpleOperation = (SimpleOperation) operation;
            Node typeOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_TYPE).item(0);
            typeOperation.setTextContent(simpleOperation.getType().name());
            Node serviceOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_SERVICE).item(0);
            serviceOperation.setTextContent(simpleOperation.getService());
        }
        else if(operation.getOperationType() == OperationType.taskOperation){
            TaskOperation taskOperation = (TaskOperation) operation;
            Node typeOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_TYPE).item(0);
            typeOperation.setTextContent(taskOperation.getType().name());

            if (taskOperation.getTargetSystem() != null) {
                Node targetSystemOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_TARGET_SYSTEM).item(0);
                targetSystemOperation.setTextContent(taskOperation.getTargetSystem());
            }
            if (taskOperation.getCandidateGroups() != null) {
                Node candidateGroupsOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_CANDIDATE_GROUPS).item(0);
                candidateGroupsOperation.setTextContent(taskOperation.getCandidateGroups());
            }
            if (taskOperation.getMailTemplate() != null) {
                Node mailTemplateOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_MAIL_TEMPLATE).item(0);
                mailTemplateOperation.setTextContent(taskOperation.getMailTemplate());
            }
            if (taskOperation.getMailTo() != null) {
                Node mailToOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_MAIL_TO).item(0);
                mailToOperation.setTextContent(taskOperation.getMailTo());
            }
            if (taskOperation.getMailSubjectPrefix() != null) {
                Node mailSubjectPrefixOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_MAIL_SUBJECT_PREFIX).item(0);
                mailSubjectPrefixOperation.setTextContent(taskOperation.getMailSubjectPrefix());
            }
        }

        Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);

        return newOperation;
    }
    private Document configureOperationsXML(Document doc, Guide guide) {
        try
        {
            for (Operation operation: guide.getOperations())
            {
                Node newOperation = configureOperationsXML(doc, operation);
                doc.getElementsByTagName(XMLConstants.GUIDE_ELEMENT).item(0).appendChild(doc.importNode(newOperation,true));
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

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
            Node esto = docBinary.createElement("");
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
