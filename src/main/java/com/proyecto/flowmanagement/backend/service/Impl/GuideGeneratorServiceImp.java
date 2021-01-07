package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.def.OperationType;
import com.proyecto.flowmanagement.backend.def.XMLConstants;
import com.proyecto.flowmanagement.backend.persistence.entity.*;
import org.springframework.stereotype.Service;
import com.google.common.collect.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.List;

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

            doc = configureOperationsXML(doc, guide.getOperations());

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

                String esto = "";
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        return doc;
    }

    private Document configureOperationsXML(Document doc, List<Operation> operations) {
        try
        {
            for (Operation operation: operations)
            {
                File file = null;
                if (operation.getOperationType() == OperationType.simpleOperation){
                    SimpleOperation simpleOperation = (SimpleOperation) operation;
                    file = new File(XMLConstants.SIMPLE_OPERATION_XML_LOCATION);
                }
                else if(operation.getOperationType() == OperationType.taskOperation){
                    TaskOperation taskOperation = (TaskOperation) operation;
                    file = new File(XMLConstants.TASK_OPERATION_XML_LOCATION);
                }

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document docOperation = dBuilder.parse(file);

                Node nameOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_NAME).item(0);
                nameOperation.setTextContent(operation.getName());
                Node labelOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_LABEL).item(0);
                labelOperation.setTextContent(operation.getLabel());

                if (operation.getOperationOrder() > 0) {
                    Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                    Node operationOrderOperation = docOperation.createElement(XMLConstants.OPERATION_OPERATION_ORDER);
                    operationOrderOperation.setTextContent(String.valueOf(operation.getOperationOrder()));
                    newOperation.insertBefore(operationOrderOperation,labelOperation.getNextSibling());
                }
                if (operation.getPauseExecution() != null) {
                    Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                    Node pauseExecutionOperation = docOperation.createElement(XMLConstants.OPERATION_PAUSE_EXECUTION);
                    pauseExecutionOperation.setTextContent(operation.getPauseExecution().toString());
                    newOperation.insertBefore(pauseExecutionOperation,labelOperation.getNextSibling());
                }
                if (operation.getAutomatic() != null) {
                    Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                    Node automaticOperation = docOperation.createElement(XMLConstants.OPERATION_AUTOMATIC);
                    automaticOperation.setTextContent(operation.getAutomatic().toString());
                    newOperation.insertBefore(automaticOperation,labelOperation.getNextSibling());
                }
                if (operation.getTitle() != null) {
                    Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                    Node titleOperation = docOperation.createElement(XMLConstants.OPERATION_TITLE);
                    titleOperation.setTextContent(operation.getTitle());
                    newOperation.insertBefore(titleOperation,labelOperation.getNextSibling());
                }
                if (operation.getComment() != null) {
                    Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                    Node commentOperation = docOperation.createElement(XMLConstants.OPERATION_COMMENT);
                    commentOperation.setTextContent(operation.getComment());
                    newOperation.insertBefore(commentOperation,labelOperation.getNextSibling());
                }
                if (operation.getPreExecute() != null) {
                    Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                    Node preExecuteOperation = docOperation.createElement(XMLConstants.OPERATION_PRE_EXECUTE);
                    preExecuteOperation.setTextContent(operation.getPreExecute().toString());
                    newOperation.insertBefore(preExecuteOperation,labelOperation.getNextSibling());
                }
                if (operation.getVisible() != null) {
                    Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                    Node visibleOperation = docOperation.createElement(XMLConstants.OPERATION_VISIBLE);
                    visibleOperation.setTextContent(operation.getVisible().toString());
                    newOperation.insertBefore(visibleOperation,labelOperation.getNextSibling());
                }

                Node operationTypeOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_OPERATION_TYPE).item(0);
                operationTypeOperation.setTextContent(operation.getOperationType().name());

                if (operation.getNotifyOperationDelay() != null) {
                    Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                    Node notifyOperationDelayOperation = docOperation.createElement(XMLConstants.OPERATION_NOTIFY_OPERATION_DELAY);
                    notifyOperationDelayOperation.setTextContent(operation.getNotifyOperationDelay().toString());
                    newOperation.insertBefore(notifyOperationDelayOperation,operationTypeOperation.getNextSibling());
                }

                if (operation.getNotifyOperation() != null) {
                    Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                    Node notifyOperationOperation = docOperation.createElement(XMLConstants.OPERATION_NOTIFY_OPERATION);
                    notifyOperationOperation.setTextContent(operation.getNotifyOperation().toString());
                    newOperation.insertBefore(notifyOperationOperation,operationTypeOperation.getNextSibling());

                    for (Operation operation1 : operation.getOperationNotifyIds()){
                        Node operationIdsOperation = docOperation.createElement(XMLConstants.OPERATION_OPERATION_IDS);
                        operationIdsOperation.setTextContent(operation1.getName());
                        newOperation.insertBefore(operationIdsOperation,notifyOperationOperation.getNextSibling());
                    }
                }

                if (operation.getNotifyAlternative() != null) {
                    Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                    Node notifyAlternativeOperation = docOperation.createElement(XMLConstants.OPERATION_NOTIFY_ALTERNATIVE);
                    notifyAlternativeOperation.setTextContent(operation.getNotifyAlternative().toString());
                    newOperation.insertBefore(notifyAlternativeOperation,operationTypeOperation.getNextSibling());

                    for (Alternative alternative : operation.getAlternativeIds()){
                        Node alternativeIdsOperation = docOperation.createElement(XMLConstants.OPERATION_ALTERNATIVE_IDS);
                        alternativeIdsOperation.setTextContent(alternative.getNextStep());
                        newOperation.insertBefore(alternativeIdsOperation,notifyAlternativeOperation.getNextSibling());
                    }
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

                    if (taskOperation.getMailSubjectPrefix() != null) {
                        Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                        Node mailSubjectPrefixOperation = docOperation.createElement(XMLConstants.OPERATION_MAIL_SUBJECT_PREFIX);
                        mailSubjectPrefixOperation.setTextContent(taskOperation.getMailSubjectPrefix());
                        newOperation.insertBefore(mailSubjectPrefixOperation,typeOperation.getNextSibling());
                    }
                    if (taskOperation.getMailTo() != null) {
                        Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                        Node mailToOperation = docOperation.createElement(XMLConstants.OPERATION_MAIL_TO);
                        mailToOperation.setTextContent(taskOperation.getMailTo());
                        newOperation.insertBefore(mailToOperation,typeOperation.getNextSibling());
                    }
                    if (taskOperation.getMailTemplate() != null) {
                        Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                        Node mailTemplateOperation = docOperation.createElement(XMLConstants.OPERATION_MAIL_TEMPLATE);
                        mailTemplateOperation.setTextContent(taskOperation.getMailTemplate());
                        newOperation.insertBefore(mailTemplateOperation,typeOperation.getNextSibling());
                    }
                    if (taskOperation.getCandidateGroups() != null) {
                        Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                        Node candidateGroupsOperation = docOperation.createElement(XMLConstants.OPERATION_CANDIDATE_GROUPS);
                        candidateGroupsOperation.setTextContent(taskOperation.getCandidateGroups());
                        newOperation.insertBefore(candidateGroupsOperation,typeOperation.getNextSibling());
                    }
                    if (taskOperation.getTargetSystem() != null) {
                        Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                        Node targetSystemOperation = docOperation.createElement(XMLConstants.OPERATION_TARGET_SYSTEM);
                        targetSystemOperation.setTextContent(taskOperation.getTargetSystem());
                        newOperation.insertBefore(targetSystemOperation,typeOperation.getNextSibling());
                    }
                }

                if (operation.getInParameters() != null) {
                    int max = operation.getInParameters().size();
                    for(int i=max-1; i>=0; i--){
                        Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                        Node inParametersOperation = docOperation.importNode(getParameter(operation.getInParameters().get(i), XMLConstants.OPERATION_IN_PARAMETERS),true);
                        newOperation.insertBefore(inParametersOperation,operationTypeOperation.getNextSibling());
                    }
                }

                if (operation.getOutParameters() != null) {
                    int max = operation.getOutParameters().size();
                    for(int i=max-1; i>=0; i--){
                        Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                        Node outParametersOperation = docOperation.importNode(getParameter(operation.getInParameters().get(i),XMLConstants.OPERATION_OUT_PARAMETERS),true);
                        newOperation.insertBefore(outParametersOperation,operationTypeOperation.getNextSibling());
                    }
                }

                Node newOperation = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);

                doc.getElementsByTagName(XMLConstants.GUIDE_ELEMENT).item(0).appendChild(doc.importNode(newOperation,true));
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return doc;
    }

    private Node getParameter(OperationParameter parameter, String constantTypeParameter) throws ParserConfigurationException, IOException, TransformerException, org.xml.sax.SAXException {
        Node parameterNode = null;
        File file = null;
        if (constantTypeParameter == XMLConstants.OPERATION_IN_PARAMETERS){
            file = new File(XMLConstants.IN_PARAMETER_XML_LOCATION);
        }
        else if(constantTypeParameter == XMLConstants.OPERATION_OUT_PARAMETERS){
            file = new File(XMLConstants.OUT_PARAMETER_XML_LOCATION);
        }
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docParameter = dBuilder.parse(file);

        Node nameParameter = docParameter.getElementsByTagName(XMLConstants.PARAMETER_NAME).item(0);
        nameParameter.setTextContent(parameter.getName());

        if (parameter.getVisibleWhenInParameterEqualsCondition() != null) {
            Node newOperation = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node visibleWhenInParameterEqualsConditionParameter = docParameter.createElement(XMLConstants.PARAMETER_VISIBLE_WHEN_IN_PARAMETER_EQUALS_CONDITION);
            visibleWhenInParameterEqualsConditionParameter.setTextContent(parameter.getVisibleWhenInParameterEqualsCondition());
            newOperation.insertBefore(visibleWhenInParameterEqualsConditionParameter,nameParameter.getNextSibling());
        }
        if (parameter.getVisibleWhenInParameterEqualsCondition() != null) {
            Node newOperation = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node visibleParameter = docParameter.createElement(XMLConstants.PARAMETER_VISIBLE);
            visibleParameter.setTextContent(parameter.getVisible().toString());
            newOperation.insertBefore(visibleParameter,nameParameter.getNextSibling());
        }
        if (parameter.getVisibleWhenInParameterEqualsCondition() != null) {
            Node newOperation = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node labelParameter = docParameter.createElement(XMLConstants.PARAMETER_LABEL);
            labelParameter.setTextContent(parameter.getLabel());
            newOperation.insertBefore(labelParameter,nameParameter.getNextSibling());
        }

        Node typeParameter = docParameter.getElementsByTagName(XMLConstants.PARAMETER_TYPE).item(0);
        typeParameter.setTextContent(parameter.getType());

        Node descriptionParameter = docParameter.getElementsByTagName(XMLConstants.PARAMETER_DESCRIPTION).item(0);
        descriptionParameter.setTextContent(parameter.getDescription());

        if (parameter.getValueWhenInParameterEquals() != null) {
            Node newOperation = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node valueWhenInParameterEqualsParameter = docParameter.createElement(XMLConstants.PARAMETER_VALUE_WHEN_IN_PARAMETER_EQUALS);
            valueWhenInParameterEqualsParameter.setTextContent(parameter.getValueWhenInParameterEquals());
            newOperation.insertBefore(valueWhenInParameterEqualsParameter,descriptionParameter.getNextSibling());
        }
        if (parameter.getValidateCrossFieldCondition() != null) {
            Node newOperation = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node validateCrossFieldConditionParameter = docParameter.createElement(XMLConstants.PARAMETER_VALIDATE_CROSS_FIELD_CONDITION);
            validateCrossFieldConditionParameter.setTextContent(parameter.getValidateCrossFieldCondition().toString());
            newOperation.insertBefore(validateCrossFieldConditionParameter,descriptionParameter.getNextSibling());
        }
        if (parameter.getConvertCondition() != null) {
            Node newOperation = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node convertConditionParameter = docParameter.createElement(XMLConstants.PARAMETER_CONVERT_CONDITION);
            convertConditionParameter.setTextContent(parameter.getConvertCondition().toString());
            newOperation.insertBefore(convertConditionParameter,descriptionParameter.getNextSibling());
        }
        if (parameter.getConvert() != null) {
            Node newOperation = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node convertParameter = docParameter.createElement(XMLConstants.PARAMETER_CONVERT);
            convertParameter.setTextContent(parameter.getConvert().toString());
            newOperation.insertBefore(convertParameter,descriptionParameter.getNextSibling());
        }
        if (parameter.getProperties() != null) {
            Node newOperation = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node propertiesParameter = docParameter.createElement(XMLConstants.PARAMETER_PROPERTIES);
            propertiesParameter.setTextContent(parameter.getProperties().toString());
            newOperation.insertBefore(propertiesParameter,descriptionParameter.getNextSibling());
        }
        if (parameter.getSourceValueEntityProperty() != null) {
            Node newOperation = docParameter.getElementsByTagName(constantTypeParameter).item(0);
            Node sourceValueEntityPropertyParameter = docParameter.createElement(XMLConstants.PARAMETER_SOURCE_VALUE_ENTITY_PROPERTY);
            sourceValueEntityPropertyParameter.setTextContent(parameter.getSourceValueEntityProperty());
            newOperation.insertBefore(sourceValueEntityPropertyParameter,descriptionParameter.getNextSibling());
        }
        if (constantTypeParameter == XMLConstants.OPERATION_IN_PARAMETERS){
            parameterNode = docParameter.getElementsByTagName(XMLConstants.PARAMETER_IN_ELEMENT).item(0);
        }
        else if(constantTypeParameter == XMLConstants.OPERATION_OUT_PARAMETERS){
            parameterNode = docParameter.getElementsByTagName(XMLConstants.PARAMETER_OUT_ELEMENT).item(0);
        }
        return parameterNode;
    }

    private Node configureStepAlternativesXML(Document doc, Alternative alternative) throws ParserConfigurationException, IOException, org.xml.sax.SAXException {

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
            Node unaryNode = getUnaryCondition(alternative.getConditions().get(0));
            Node locationAlternative = docAlternative.getElementsByTagName(XMLConstants.ALTERNATIVE).item(0);
            Node unaryImported = docAlternative.importNode(unaryNode,true);
            locationAlternative.appendChild(unaryImported);
        }

        if(alternative.getBinaryConditions() != null)
        {
            Node binaryCondition = getBinaryCondition(alternative.getBinaryConditions().get(0));
            Node locationAlternative = docAlternative.getElementsByTagName(XMLConstants.ALTERNATIVE).item(0);
            Node binaryImported = docAlternative.importNode(binaryCondition,true);
            locationAlternative.appendChild(binaryImported);
        }

        return docAlternative.getElementsByTagName(XMLConstants.ALTERNATIVE).item(0);
    }

    private Node getBinaryCondition(BinaryCondition binaryConditions) throws ParserConfigurationException, IOException, org.xml.sax.SAXException {
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

    private Node getUnaryCondition(UnaryCondition condition) throws IOException, ParserConfigurationException, org.xml.sax.SAXException {
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
