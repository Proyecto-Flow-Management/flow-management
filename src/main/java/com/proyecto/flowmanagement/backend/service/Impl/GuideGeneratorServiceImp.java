package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.def.OperationType;
import com.proyecto.flowmanagement.backend.def.TypeOperation;
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
import javax.xml.transform.*;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class GuideGeneratorServiceImp {

    public List<byte[]> guidePrints (Guide mainGuide){
        List<byte[]> docs = new LinkedList<>();
        docs.add(GuidePrint(mainGuide));
        if(mainGuide.getGuides()!= null){
            for(Guide guide : mainGuide.getGuides()){
                docs.add(GuidePrint(guide));
            }
        }
        return docs;
    }

    public byte[] GuidePrint(Guide guide) {
        try {
            File file = new File(XMLConstants.GUIDE_XML_LOCATION);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc = configureGuidXML(doc, guide);

            doc = configureStepsXML(doc, guide);

            //printResult(doc);

            return printResultByte(doc);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    private void printResult(Document doc) throws IOException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(XMLConstants.GUIDE_RESULT));
        transformer.transform(domSource, streamResult);
    }

    private byte[] printResultByte(Document doc) throws IOException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(doc);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        StreamResult result=new StreamResult(bos);
        transformer.transform(domSource, result);
        byte[] array = bos.toByteArray();
        return array;
    }

    private Document configureGuidXML(Document doc, Guide guide) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        Node guideHeader = doc.getElementsByTagName(XMLConstants.GUIDE_ELEMENT).item(0);

        Node mainStepID = doc.getElementsByTagName(XMLConstants.MAIN_STEP_ID).item(0);
        mainStepID.setTextContent(guide.getMainStep());

        NamedNodeMap guideHeaderAttributes = guideHeader.getAttributes();

        Node xmlnsvc = guideHeaderAttributes.getNamedItem("xmlns:vc");
        Node xmlnsttg = guideHeaderAttributes.getNamedItem("xmlns:ttg");
        Node xmlnsxsi = guideHeaderAttributes.getNamedItem("xmlns:xsi");
        Node schemaLocation = guideHeaderAttributes.getNamedItem("xmlns:schemaLocation");

        xmlnsvc.setTextContent(XMLConstants.XMLNS_VC);
        xmlnsttg.setTextContent(XMLConstants.XMLNS_TTG);
        xmlnsxsi.setTextContent(XMLConstants.XMLNS_XSI);
        schemaLocation.setTextContent(XMLConstants.XMLNS_SCHEMALOCATION);

        Node newOperation = doc.getElementsByTagName(XMLConstants.GUIDE_ELEMENT).item(0);
        Node refNode = doc.getElementsByTagName(XMLConstants.AUX).item(0);

        if (guide.getOperations() != null){
            for (Operation operation : guide.getOperations()) {
                Node op = configureOperationsXML(doc, operation, guide.getOperations());
                newOperation.insertBefore(doc.importNode(op,true), refNode);
            }
        }

        Node docOp = doc.getElementsByTagName(XMLConstants.GUIDE_ELEMENT).item(0);
        Node node = doc.getElementsByTagName(XMLConstants.AUX).item(0);
        docOp.removeChild(node.getNextSibling());
        docOp.removeChild(node);

        return doc;
    }

    private Document configureStepsXML(Document doc, Guide guide) {
        try {
            for (Step step : guide.getSteps()) {
                File file = new File(XMLConstants.STEP_XML_LOCATION);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document docStep = dBuilder.parse(file);

                Node idStep = docStep.getElementsByTagName(XMLConstants.STEP_ID).item(0);
                idStep.setTextContent(step.getText());
                Node labelStep = docStep.getElementsByTagName(XMLConstants.STEP_LABEL).item(0);
                labelStep.setTextContent(step.getLabel());
                Node textStep = docStep.getElementsByTagName(XMLConstants.STEP_TEXT).item(0);
                textStep.setTextContent(step.getText());

                Node newStep = docStep.getElementsByTagName(XMLConstants.STEP_ELEMENT).item(0);
                Node refNode = docStep.getElementsByTagName(XMLConstants.AUX).item(0);

                if(step.getAlternatives() != null){
                    for (Alternative alternative : step.getAlternatives()) {
                        Node alt = configureAlternativesXML(doc, alternative);
                        newStep.insertBefore(docStep.importNode(alt,true), refNode);
                    }
                }

                if (step.getOperations() != null){
                    for (Operation operation : step.getOperations()) {
                        Node op = configureOperationsXML(doc, operation, null);
                        newStep.insertBefore(docStep.importNode(op,true), refNode);
                    }
                }

                if(step.getStepDocuments() != null){
                    for (StepDocument referenceDoc : step.getStepDocuments()) {
                        Node refDoc = configureReferenceDocsXML(doc, referenceDoc);
                        newStep.insertBefore(docStep.importNode(refDoc,true), refNode);
                    }
                }

                Node docOp = docStep.getElementsByTagName(XMLConstants.STEP_ELEMENT).item(0);
                Node node = docStep.getElementsByTagName(XMLConstants.AUX).item(0);
                docOp.removeChild(node.getNextSibling());
                docOp.removeChild(node);

                doc.getElementsByTagName(XMLConstants.GUIDE_ELEMENT).item(0).appendChild(doc.importNode(newStep, true));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return doc;
    }

    private Node configureReferenceDocsXML(Document doc, StepDocument referenceDoc) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(XMLConstants.REFERENCE_DOC_XML_LOCATION);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docReferenceDocs = dBuilder.parse(file);

        Node mimeType = docReferenceDocs.getElementsByTagName(XMLConstants.REFERENCE_DOC_MIME_TYPE).item(0);
        Node url = docReferenceDocs.getElementsByTagName(XMLConstants.REFERENCE_DOC_URL).item(0);

        //mimeType.setTextContent(referenceDoc.getMimeType());
        url.setTextContent(referenceDoc.getUrl());

        Node newRefDoc = docReferenceDocs.getElementsByTagName(XMLConstants.REFERENCE_DOC_ELEMENT).item(0);

        return newRefDoc;
    }

    private Node configureAlternativesXML(Document doc, Alternative alternative) throws ParserConfigurationException, IOException, org.xml.sax.SAXException, TransformerException {

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

        Node newAlt = docAlternative.getElementsByTagName(XMLConstants.ALTERNATIVE_ELEMENT).item(0);
        Node refNode = docAlternative.getElementsByTagName(XMLConstants.AUX).item(0);

        if (alternative.getConditions() != null) {
            for (Condition condition : alternative.getConditions()) {
                Node cond = configureConditions(doc, condition, "");
                newAlt.insertBefore(docAlternative.importNode(cond,true), refNode);
            }
        }

        Node docOp = docAlternative.getElementsByTagName(XMLConstants.ALTERNATIVE_ELEMENT).item(0);
        Node node = docAlternative.getElementsByTagName(XMLConstants.AUX).item(0);
        docOp.removeChild(node.getNextSibling());
        docOp.removeChild(node);

        return newAlt;
    }

    private Node configureOperationsXML(Document doc, Operation operation, List<Operation> operations) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        File file = null;
        if (operation.getOperationType() == OperationType.simpleOperation) {
            file = new File(XMLConstants.SIMPLE_OPERATION_XML_LOCATION);
        } else if (operation.getOperationType() == OperationType.taskOperation) {
            file = new File(XMLConstants.TASK_OPERATION_XML_LOCATION);
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docOperation = dBuilder.parse(file);

        Node nameOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_NAME).item(0);
        nameOperation.setTextContent(operation.getName());
        Node labelOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_LABEL).item(0);
        labelOperation.setTextContent(operation.getLabel());

        if (operation.getVisible() != null){
            Node visible = docOperation.getElementsByTagName(XMLConstants.OPERATION_VISIBLE).item(0);
            visible.setTextContent(String.valueOf(operation.getVisible()));
        }else{
            Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_VISIBLE).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (operation.getPreExecute() != null){
            Node preExecute = docOperation.getElementsByTagName(XMLConstants.OPERATION_PRE_EXECUTE).item(0);
            preExecute.setTextContent(String.valueOf(operation.getPreExecute()));
        }else{
            Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_PRE_EXECUTE).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (operation.getComment() != null){
            Node comment = docOperation.getElementsByTagName(XMLConstants.OPERATION_COMMENT).item(0);
            comment.setTextContent(operation.getComment());
        }else{
            Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_COMMENT).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (operation.getTitle() != null){
            Node title = docOperation.getElementsByTagName(XMLConstants.OPERATION_TITLE).item(0);
            title.setTextContent(operation.getTitle());
        }else{
            Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_TITLE).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (operation.getAutomatic() != null){
            Node automatic = docOperation.getElementsByTagName(XMLConstants.OPERATION_AUTOMATIC).item(0);
            automatic.setTextContent(String.valueOf(operation.getAutomatic()));
        }else{
            Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_AUTOMATIC).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (operation.getPauseExecution() != null){
            Node pauseExecution = docOperation.getElementsByTagName(XMLConstants.OPERATION_PAUSE_EXECUTION).item(0);
            pauseExecution.setTextContent(String.valueOf(operation.getPauseExecution()));
        }else{
            Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_PAUSE_EXECUTION).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (operation.getOperationOrder() != null){
            Node operationOrder = docOperation.getElementsByTagName(XMLConstants.OPERATION_OPERATION_ORDER).item(0);
            operationOrder.setTextContent(String.valueOf(operation.getOperationOrder()));
        }else{
            Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_OPERATION_ORDER).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        Node operationTypeOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_OPERATION_TYPE).item(0);
        operationTypeOperation.setTextContent(operation.getOperationType().name());

        if (operation.getOperationType() == OperationType.simpleOperation) {
            SimpleOperation simpleOperation = (SimpleOperation) operation;
            Node typeOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_TYPE).item(0);
            typeOperation.setTextContent(simpleOperation.getType().name());
            Node serviceOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_SERVICE).item(0);
            serviceOperation.setTextContent(simpleOperation.getService());
        } else if (operation.getOperationType() == OperationType.taskOperation) {
            TaskOperation taskOperation = (TaskOperation) operation;
            Node typeOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_TYPE).item(0);
            typeOperation.setTextContent(taskOperation.getType().name());

            if (taskOperation.getTargetSystem() != null){
                Node targetSystem = docOperation.getElementsByTagName(XMLConstants.OPERATION_TARGET_SYSTEM).item(0);
                targetSystem.setTextContent(taskOperation.getTargetSystem());
            }else{
                Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_TARGET_SYSTEM).item(0);
                docOp.removeChild(node.getNextSibling());
                docOp.removeChild(node);
            }

            if (taskOperation.getCandidateGroups() != null){
                Node newCand = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                Node refNode = docOperation.getElementsByTagName(XMLConstants.OPERATION_MAIL_TEMPLATE).item(0);

                /*for (String candidate : taskOperation.getCandidateGroups()) {
                    Node cand = configureCandidate(doc, candidate);
                    newCand.insertBefore(docOperation.importNode(cand, true), refNode);
                }*/
            }else{
                Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_CANDIDATE_GROUPS).item(0);
                docOp.removeChild(node.getNextSibling());
                docOp.removeChild(node);
            }

            if (taskOperation.getMailTemplate() != null){
                Node mailTemplate = docOperation.getElementsByTagName(XMLConstants.OPERATION_MAIL_TEMPLATE).item(0);
                mailTemplate.setTextContent(taskOperation.getMailTemplate());
            }else{
                Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_MAIL_TEMPLATE).item(0);
                docOp.removeChild(node.getNextSibling());
                docOp.removeChild(node);
            }

            if (taskOperation.getMailTo() != null){
                Node mailTo = docOperation.getElementsByTagName(XMLConstants.OPERATION_MAIL_TO).item(0);
                mailTo.setTextContent(taskOperation.getMailTo());
            }else{
                Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_MAIL_TO).item(0);
                docOp.removeChild(node.getNextSibling());
                docOp.removeChild(node);
            }

            if (taskOperation.getMailSubjectPrefix() != null){
                Node mailSubjectPrefix = docOperation.getElementsByTagName(XMLConstants.OPERATION_MAIL_SUBJECT_PREFIX).item(0);
                mailSubjectPrefix.setTextContent(taskOperation.getMailSubjectPrefix());
            }else{
                Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_MAIL_SUBJECT_PREFIX).item(0);
                docOp.removeChild(node.getNextSibling());
                docOp.removeChild(node);
            }
        }

        if (operation.getInParameters() != null){

            Node newPar = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node refNode = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_ALTERNATIVE).item(0);

            for (OperationParameter operationParameter : operation.getInParameters()) {
                Node param = configureParameter(doc, operationParameter, XMLConstants.PARAMETER_IN_ELEMENT);
                newPar.insertBefore(docOperation.importNode(param, true), refNode);
            }
        }

        if (operation.getOutParameters() != null){

            Node newPar = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node refNode = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_ALTERNATIVE).item(0);

            for (OperationParameter operationParameter : operation.getOutParameters()) {
                Node param = configureParameter(doc, operationParameter, XMLConstants.PARAMETER_OUT_ELEMENT);
                newPar.insertBefore(docOperation.importNode(param, true), refNode);
            }
        }

        if (operation.getConditions() != null){

            Node newCond = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node refNode = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_ALTERNATIVE).item(0);

            for (Condition condition : operation.getConditions()) {
                Node cond = configureConditions(doc, condition, "");
                newCond.insertBefore(docOperation.importNode(cond, true), refNode);
            }
        }

        if (operation.getNotifyAlternative() != null){
            Node notifyAlternative = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_ALTERNATIVE).item(0);
            notifyAlternative.setTextContent(String.valueOf(operation.getNotifyAlternative()));
        }else{
            Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_ALTERNATIVE).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (operation.getAlternativeIds() != null){
            Node newAlt = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node refNode = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_OPERATION).item(0);

            for (Alternative alternative : operation.getAlternativeIds()) {
                Node altId = configureAlternativeId(doc, alternative);
                newAlt.insertBefore(docOperation.importNode(altId, true), refNode);
            }
        }

        if (operation.getNotifyOperation() != null){
            Node notifyOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_OPERATION).item(0);
            notifyOperation.setTextContent(String.valueOf(operation.getNotifyOperation()));
        }else{
            Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_OPERATION).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (operation.getOperationNotifyIds() != null){
            Node newOpIds = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node refNode = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_OPERATION_DELAY).item(0);

            for (Operation operationGuide : operations) {
                Node opId = configureOperationId(doc, operationGuide);
                newOpIds.insertBefore(docOperation.importNode(opId, true), refNode);
            }
        }

        if (operation.getNotifyOperationDelay() != null){
            Node notifyOperationDelay = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_OPERATION_DELAY).item(0);
            notifyOperationDelay.setTextContent(String.valueOf(operation.getNotifyOperationDelay()));
        }else{
            Node docOp = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
            Node node = docOperation.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_OPERATION_DELAY).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);

        return newOperation;
    }

    private Node configureCandidate(Document doc, String candidate) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(XMLConstants.CANDIDATE_GROUPS_XML_LOCATION);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docCandidateGroups = dBuilder.parse(file);

        Node groupName = docCandidateGroups.getElementsByTagName(XMLConstants.CANDIDATE_GROUPS_GROUP_NAME).item(0);
        groupName.setTextContent(candidate);

        Node newCand = docCandidateGroups.getElementsByTagName(XMLConstants.OPERATION_CANDIDATE_GROUPS).item(0);

        return newCand;
    }

    private Node configureAlternativeId(Document doc, Alternative alternative) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(XMLConstants.ALTERNATIVE_IDS_XML_LOCATION);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docAlternativeIds = dBuilder.parse(file);

        Node alternativeId = docAlternativeIds.getElementsByTagName(XMLConstants.OPERATION_ALTERNATIVE_IDS).item(0);
        alternativeId.setTextContent(alternative.getNextStep());

        return alternativeId;
    }

    private Node configureOperationId(Document doc, Operation operation) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(XMLConstants.OPERATION_NOTIFY_IDS_XML_LOCATION);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docOperationId = dBuilder.parse(file);

        Node operationId = docOperationId.getElementsByTagName(XMLConstants.OPERATION_OPERATION_NOTIFY_IDS).item(0);
        operationId.setTextContent(operation.getName());

        return operationId;
    }

    private Node configureParameter(Document doc, OperationParameter parameter, String constantTypeParameter) throws ParserConfigurationException, IOException, TransformerException, org.xml.sax.SAXException {

        File file = null;

        switch (constantTypeParameter) {

            case XMLConstants.OPERATION_IN_PARAMETERS :
                file = new File(XMLConstants.IN_PARAMETER_XML_LOCATION);
                break;

            case XMLConstants.OPERATION_OUT_PARAMETERS :
                file = new File(XMLConstants.OUT_PARAMETER_XML_LOCATION);
                break;

            default :
                file = new File(XMLConstants.PARAMETER_PROPERTIES);
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docParameter = dBuilder.parse(file);

        Node name = docParameter.getElementsByTagName(XMLConstants.PARAMETER_NAME).item(0);
        name.setTextContent(parameter.getName());

        if (parameter.getLabel() != null){
            Node label = docParameter.getElementsByTagName(XMLConstants.PARAMETER_LABEL).item(0);
            label.setTextContent(String.valueOf(parameter.getVisible()));
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_LABEL).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getVisible() != null){
            Node visible = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VISIBLE).item(0);
            visible.setTextContent(String.valueOf(parameter.getVisible()));
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VISIBLE).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getVisibleWhenInParameterEqualsCondition() != null){
            Node visibleWhenInParameterEqualsCondition = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VISIBLE_WHEN_IN_PARAMETER_EQUALS_CONDITION).item(0);
            visibleWhenInParameterEqualsCondition.setTextContent(parameter.getVisibleWhenInParameterEqualsCondition());
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VISIBLE_WHEN_IN_PARAMETER_EQUALS_CONDITION).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        Node type = docParameter.getElementsByTagName(XMLConstants.PARAMETER_TYPE).item(0);
        type.setTextContent(parameter.getType());

        Node description = docParameter.getElementsByTagName(XMLConstants.PARAMETER_DESCRIPTION).item(0);
        description.setTextContent(parameter.getDescription());

        if (parameter.getValue() != null){
            Node value = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VALUE).item(0);
            value.setTextContent(String.valueOf(parameter.getVisible()));
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VALUE).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getEnable() != null){
            Node enable = docParameter.getElementsByTagName(XMLConstants.PARAMETER_ENABLE).item(0);
            enable.setTextContent(String.valueOf(parameter.getEnable()));
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_ENABLE).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getRequired() != null){
            Node required = docParameter.getElementsByTagName(XMLConstants.PARAMETER_REQUIRED).item(0);
            required.setTextContent(String.valueOf(parameter.getRequired()));
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_REQUIRED).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getValidateExpression() != null){
            Node validateExpression = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VALIDATE_EXPRESSION).item(0);
            validateExpression.setTextContent(parameter.getValidateExpression());
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VALIDATE_EXPRESSION).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getValidateExpressionErrorDescription() != null){
            Node validateExpressionErrorDescription = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VALIDATE_EXPRESSION_ERROR_DESCRIPTION).item(0);
            validateExpressionErrorDescription.setTextContent(parameter.getValidateExpressionErrorDescription());
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VALIDATE_EXPRESSION_ERROR_DESCRIPTION).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getOptionValue() != null){
            Node optionValue = docParameter.getElementsByTagName(XMLConstants.PARAMETER_OPTION_VALUE).item(0);
            optionValue.setTextContent(parameter.getOptionValue());
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_OPTION_VALUE).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getDateFormat() != null){
            Node dateFormat = docParameter.getElementsByTagName(XMLConstants.PARAMETER_DATE_FORMAT).item(0);
            dateFormat.setTextContent(parameter.getDateFormat());
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_DATE_FORMAT).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getDateFormatRangeEnd() != null){
            Node dateFormatRangeEnd = docParameter.getElementsByTagName(XMLConstants.PARAMETER_DTE_FORMAT_RANGE_END).item(0);
            dateFormatRangeEnd.setTextContent(parameter.getDateFormatRangeEnd());
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_DTE_FORMAT_RANGE_END).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getDateFormatFinal() != null){
            Node dateFormatFinal = docParameter.getElementsByTagName(XMLConstants.PARAMETER_DATE_FORMAT_FINAL).item(0);
            dateFormatFinal.setTextContent(parameter.getDateFormatFinal());
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_DATE_FORMAT_FINAL).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getSourceValueEntity() != null){
            Node sourceValueEntity = docParameter.getElementsByTagName(XMLConstants.PARAMETER_SOURCE_VALUE_ENTITY).item(0);
            sourceValueEntity.setTextContent(String.valueOf(parameter.getSourceValueEntity()));
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_SOURCE_VALUE_ENTITY).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getSourceValueEntityProperty() != null){
            Node sourceValueEntityProperty = docParameter.getElementsByTagName(XMLConstants.PARAMETER_SOURCE_VALUE_ENTITY_PROPERTY).item(0);
            sourceValueEntityProperty.setTextContent(parameter.getSourceValueEntityProperty());
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_SOURCE_VALUE_ENTITY_PROPERTY).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getProperties() != null){
            Node newProp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node refNode = docParameter.getElementsByTagName(XMLConstants.PARAMETER_CONVERT).item(0);

            for (OperationParameter properties : parameter.getProperties()) {
                Node property = configureParameter(doc, properties, XMLConstants.PARAMETER_PROPERTIES);
                newProp.insertBefore(docParameter.importNode(property, true), refNode);
            }
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_PROPERTIES).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getConvert() != null){
            Node convert = docParameter.getElementsByTagName(XMLConstants.PARAMETER_CONVERT).item(0);
            convert.setTextContent(String.valueOf(parameter.getConvert()));
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_CONVERT).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getConvertCondition() != null){
            Node newConv = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node refNode = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VALIDATE_CROSS_FIELD_CONDITION).item(0);

            for (Convertion convertion : parameter.getConvertCondition()) {
                Node convertCondition = configureConvertCondition(doc, convertion);
                newConv.insertBefore(docParameter.importNode(convertCondition, true), refNode);
            }
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_CONVERT_CONDITION).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getValidateCrossFieldCondition() != null){
            Node newVal = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node refNode = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VALUE_WHEN_IN_PARAMETER_EQUALS).item(0);

            for (ValidateCrossFieldCondition validateCrossFieldCondition : parameter.getValidateCrossFieldCondition()) {
                Node validateCondition = configureValidateCrossFieldCondition(doc, validateCrossFieldCondition);
                newVal.insertBefore(docParameter.importNode(validateCondition, true), refNode);
            }
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VALIDATE_CROSS_FIELD_CONDITION).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        if (parameter.getValueWhenInParameterEquals() != null){
            Node valueWhenInParameterEquals = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VALUE_WHEN_IN_PARAMETER_EQUALS).item(0);
            valueWhenInParameterEquals.setTextContent(parameter.getValueWhenInParameterEquals());
        }else{
            Node docOp = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node node = docParameter.getElementsByTagName(XMLConstants.PARAMETER_VALUE_WHEN_IN_PARAMETER_EQUALS).item(0);
            docOp.removeChild(node.getNextSibling());
            docOp.removeChild(node);
        }

        Node newPar = docParameter.getElementsByTagName(constantTypeParameter).item(0);

        return  newPar;
    }

    private Node configureValidateCrossFieldCondition(Document doc, ValidateCrossFieldCondition validateCrossFieldCondition) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(XMLConstants.VALIDATE_CROSS_FIELD_CONDITION_XML_LOCATION);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docValidate = dBuilder.parse(file);

        /*Node fieldName = docValidate.getElementsByTagName(XMLConstants.VALIDATE_CROSS_FIELD_CONDITION_FIELD_NAME_).item(0);
        fieldName.setTextContent(validateCrossFieldCondition.getFieldName());
        Node condition = docValidate.getElementsByTagName(XMLConstants.VALIDATE_CROSS_FIELD_CONDITION_CONDITION_).item(0);
        condition.setTextContent(validateCrossFieldCondition.getCondition());
        Node messageError = docValidate.getElementsByTagName(XMLConstants.VALIDATE_CROSS_FIELD_CONDITION_MESSAGE_ERROR).item(0);
        messageError.setTextContent(validateCrossFieldCondition.getMessageError());*/

        Node newVal = docValidate.getElementsByTagName(XMLConstants.PARAMETER_VALIDATE_CROSS_FIELD_CONDITION).item(0);

        return newVal;
    }

    private Node configureConvertCondition(Document doc, Convertion convertion) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(XMLConstants.CONVERTION_XML_LOCATION);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docConvertCondition = dBuilder.parse(file);

        Node condition = docConvertCondition.getElementsByTagName(XMLConstants.CONVERTION_CONDITION).item(0);
        condition.setTextContent(convertion.getCondition());
        Node sourceUnit = docConvertCondition.getElementsByTagName(XMLConstants.CONVERTION_SOURCE_UNIT).item(0);
        sourceUnit.setTextContent(convertion.getSourceUnit());
        Node destinationUnit = docConvertCondition.getElementsByTagName(XMLConstants.CONVERTION_DESTINATION_UNIT).item(0);
        destinationUnit.setTextContent(convertion.getDestinationUnit());

        Node newConv = docConvertCondition.getElementsByTagName(XMLConstants.PARAMETER_CONVERT_CONDITION).item(0);

        return newConv;
    }

    private Node configureConditions(Document doc, Condition condition, String operator) throws ParserConfigurationException, TransformerException, SAXException, IOException {

        Node cond = null;

        if (condition.getType() == TypeOperation.unaryCondition) {
            cond = configureUnaryCondition(doc, condition, operator);
        } else {
            if (condition.getType() == TypeOperation.binaryCondition) {
                cond = configureBinaryCondition(doc, condition, operator);
            }
        }

        return cond;
    }

    private Node configureUnaryCondition(Document doc, Condition condition, String operator) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        File file = null;

        switch (operator) {

            case "1" :
                file = new File(XMLConstants.OPERATOR_UNO_UNARY_CONDITION_XML_LOCATION);
                break;

            case "2" :
                file = new File(XMLConstants.OPERATOR_DOS_UNARY_CONDITION_XML_LOCATION);
                break;

            default :
                file = new File(XMLConstants.UNARY_CONDITION_XML_LOCATION);
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docUnaryCondition = dBuilder.parse(file);

        Node newUnary = null;
        Node conditionName = null;

        switch (operator) {

            case "1" :
                conditionName = docUnaryCondition.getElementsByTagName(XMLConstants.UNARY_CONDITION_OPERATION_NAME).item(0);
                conditionName.setTextContent(condition.getOperation());
                newUnary = docUnaryCondition.getElementsByTagName(XMLConstants.UNARY_CONDITION_OPERATOR_UNO).item(0);
                break;

            case "2" :
                conditionName = docUnaryCondition.getElementsByTagName(XMLConstants.UNARY_CONDITION_OPERATION_NAME).item(0);
                conditionName.setTextContent(condition.getOperation());
                newUnary = docUnaryCondition.getElementsByTagName(XMLConstants.UNARY_CONDITION_OPERATOR_DOS).item(0);
                break;

            default :
                conditionName = docUnaryCondition.getElementsByTagName(XMLConstants.UNARY_CONDITION_OPERATION_NAME).item(0);
                conditionName.setTextContent(condition.getOperation());
                newUnary = docUnaryCondition.getElementsByTagName(XMLConstants.UNARY_CONDITION_ELEMENT).item(0);
        }

        if (condition.getConditionParameter() != null) {
            for (ConditionParameter conditionParameter : condition.getConditionParameter()) {
                Node condParam = getConditionParameter(conditionParameter);
                newUnary.appendChild(docUnaryCondition.importNode(condParam, true));
            }
        }

        return newUnary;
    }

    private Node configureBinaryCondition(Document doc, Condition condition, String operator) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        File file = null;

        switch (operator) {

            case "1" :
                file = new File(XMLConstants.OPERATOR_UNO_BINARY_CONDITION_XML_LOCATION);
                break;

            case "2" :
                file = new File(XMLConstants.OPERATOR_DOS_BINARY_CONDITION_XML_LOCATION);
                break;

            default :
                file = new File(XMLConstants.BINARY_CONDITION_XML_LOCATION);
        }


        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docBinaryCondition = dBuilder.parse(file);

        Node newBinary = null;
        Node operation = null;

        switch (operator) {

            case "1" :
                operation = docBinaryCondition.getElementsByTagName(XMLConstants.BINARY_CONDITION_OPERATION).item(0);
                operation.setTextContent(condition.getOperation());
                newBinary = docBinaryCondition.getElementsByTagName(XMLConstants.BINARY_CONDITION_OPERATOR_UNO).item(0);
                break;

            case "2" :
                operation = docBinaryCondition.getElementsByTagName(XMLConstants.BINARY_CONDITION_OPERATION).item(0);
                operation.setTextContent(condition.getOperation());
                newBinary = docBinaryCondition.getElementsByTagName(XMLConstants.BINARY_CONDITION_OPERATOR_DOS).item(0);
                break;

            default :
                operation = docBinaryCondition.getElementsByTagName(XMLConstants.BINARY_CONDITION_OPERATION).item(0);
                operation.setTextContent(condition.getOperation());
                newBinary = docBinaryCondition.getElementsByTagName(XMLConstants.BINARY_CONDITION_ELEMENT).item(0);
        }

        Node hijoIzq = configureConditions(doc, condition.getHijoIzquierdo(), "1");
        newBinary.appendChild(docBinaryCondition.importNode(hijoIzq, true));

        Node hijoDer = configureConditions(doc, condition.getHijoDerecho(), "2");
        newBinary.appendChild(docBinaryCondition.importNode(hijoDer, true));

        return newBinary;
    }

    private Node getConditionParameter(ConditionParameter conditionParameter) throws ParserConfigurationException, IOException, TransformerException, org.xml.sax.SAXException {

        File file = new File(XMLConstants.CONDITION_ENABLE_ALTERNATIVE_LOCATION);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docConditionParameter = dBuilder.parse(file);

        Node field = docConditionParameter.getElementsByTagName(XMLConstants.UNARY_CONDITION_FIELD).item(0);
        field.setTextContent(conditionParameter.getField());

        Node fieldType = docConditionParameter.getElementsByTagName(XMLConstants.UNARY_CONDITION_FIELD_TYPE).item(0);
        fieldType.setTextContent(conditionParameter.getFieldType());

        Node operator = docConditionParameter.getElementsByTagName(XMLConstants.UNARY_CONDITION_OPERATOR).item(0);
        operator.setTextContent(conditionParameter.getOperator());

        Node value = docConditionParameter.getElementsByTagName(XMLConstants.UNARY_CONDITION_VALUE).item(0);
        value.setTextContent(conditionParameter.getValue());

        Node newCondPar = docConditionParameter.getElementsByTagName(XMLConstants.CONDITION_ENABLE_ALTERNATIVE).item(0);

        return newCondPar;
    }
}
