package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.def.*;
import com.proyecto.flowmanagement.backend.persistence.entity.*;
import com.vaadin.flow.internal.Pair;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
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
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

@Service
public class GuideGeneratorServiceImp {

    public Guide importGuide (Document doc) throws IOException, SAXException, ParserConfigurationException {
        Guide guide = new Guide();
        guide.setGuiaPropia(true);
        if(doc != null){
            NodeList guideNodeList = doc.getElementsByTagName(XMLConstants.GUIDE_ELEMENT);
            if (guideNodeList != null){
                for (int i = 0; i < guideNodeList.getLength(); i++)
                {
                    Node guideNode = guideNodeList.item(i);
                    Element guideElement = (Element) guideNode;

                    Node mainStep = guideElement.getElementsByTagName(XMLConstants.MAIN_STEP_ID).item(0);
                    if (mainStep != null && mainStep.getFirstChild() != null) {
                        guide.setMainStep(mainStep.getFirstChild().getNodeValue());
                    }

                    guide.setSteps(importStep(guideNode));
                    guide.setOperations(importOperation(guideNode));
                }
            }
        }
        return guide;
    }

    private List<Step> importStep(Node node) throws ParserConfigurationException {
        List<Step> steps = new LinkedList<>();

        if(node != null){
            NodeList stepNodeList = node.getChildNodes();
            if(stepNodeList != null){

                for (int i = 0; i < stepNodeList.getLength(); i++){
                    if (stepNodeList.item(i).getNodeName() == XMLConstants.STEP_ELEMENT) {
                        Step step = new Step();

                        Node stepNode = stepNodeList.item(i);
                        Element stepElement = (Element) stepNode;

                        Node stepId = stepElement.getElementsByTagName(XMLConstants.STEP_ID).item(0);
                        if (stepId != null && stepId.getFirstChild() != null) {
                            step.setTextId(stepId.getFirstChild().getNodeValue());
                        }

                        Node label = stepElement.getElementsByTagName(XMLConstants.STEP_LABEL).item(0);
                        if (label != null && label.getFirstChild() != null) {
                            step.setLabel(label.getFirstChild().getNodeValue());
                        }

                        Node text = stepElement.getElementsByTagName(XMLConstants.STEP_TEXT).item(0);
                        if (text != null && text.getFirstChild() != null) {
                            step.setText(text.getFirstChild().getNodeValue());
                        }

                        List<Operation> operations = importOperation(stepNode);
                        step.setOperations(operations);
                        step.setAlternatives(importAlternatives(stepNode));
                        step.setStepDocuments(importStepDocuments(stepNode));

                        steps.add(step);
                    }
                }
            }
        }
        return steps;
    }

    private List<StepDocument> importStepDocuments(Node node) {
        List<StepDocument> documents = new LinkedList<>();

        if(node != null){
            NodeList documentNodeList = node.getChildNodes();
            if(documentNodeList != null){

                for (int i = 0; i < documentNodeList.getLength(); i++){
                    if (documentNodeList.item(i).getNodeName() == XMLConstants.REFERENCE_DOC_ELEMENT) {
                        StepDocument document = new StepDocument();

                        Node documentNode = documentNodeList.item(i);
                        Element documentElement = (Element) documentNode;

                        Node mimeType = documentElement.getElementsByTagName(XMLConstants.REFERENCE_DOC_MIME_TYPE).item(0);
                        if (mimeType != null && mimeType.getFirstChild() != null) {
                            document.setMimeType(mimeType.getFirstChild().getNodeValue());
                        }

                        Node url = documentElement.getElementsByTagName(XMLConstants.REFERENCE_DOC_URL).item(0);
                        if (url != null && url.getFirstChild() != null) {
                            document.setUrl(url.getFirstChild().getNodeValue());
                        }

                        documents.add(document);
                    }
                }
            }
        }
        return documents;
    }

    private List<Alternative> importAlternatives(Node node) throws ParserConfigurationException {
        List<Alternative> alternatives = new LinkedList<>();

        if(node != null){
            NodeList alternativeNodeList = node.getChildNodes();
            if(alternativeNodeList != null){

                for (int i = 0; i < alternativeNodeList.getLength(); i++){
                    if (alternativeNodeList.item(i).getNodeName() == XMLConstants.ALTERNATIVE_ELEMENT) {
                        Alternative alternative = new Alternative();

                        Node alternativeNode = alternativeNodeList.item(i);
                        Element alternativeElement = (Element) alternativeNode;

                        Node guideName = alternativeElement.getElementsByTagName(XMLConstants.ALTERNATIVE_GUIDE_NAME).item(0);
                        if (guideName != null && guideName.getFirstChild() != null) {
                            alternative.setGuideName(guideName.getFirstChild().getNodeValue());
                        }

                        Node stepId = alternativeElement.getElementsByTagName(XMLConstants.ALTERNATIVE_STEP_ID).item(0);
                        if (stepId != null && stepId.getFirstChild() != null) {
                            alternative.setNextStep(stepId.getFirstChild().getNodeValue());
                        }

                        Node label = alternativeElement.getElementsByTagName(XMLConstants.ALTERNATIVE_LABEL).item(0);
                        if (label != null && label.getFirstChild() != null) {
                            alternative.setLabel(label.getFirstChild().getNodeValue());
                        }

                        alternative.setConditions(importConditions(alternativeNode));

                        alternatives.add(alternative);
                    }
                }
            }
        }
        return alternatives;
    }

    private List<Operation> importOperation(Node node) throws ParserConfigurationException {
        List<Operation> operations = new LinkedList<>();

        if(node != null){
            NodeList operationNodeList = node.getChildNodes();
            if(operationNodeList != null){

                for (int i = 0; i < operationNodeList.getLength(); i++){
                    if (operationNodeList.item(i).getNodeName() == XMLConstants.OPERATION_ELEMENT) {
                        Operation operation = new Operation();

                        Node operationNode = operationNodeList.item(i);
                        Element operationElement = (Element) operationNode;

                        Node operationType = operationElement.getElementsByTagName(XMLConstants.OPERATION_OPERATION_TYPE).item(0);
                        if (operationType != null && operationType.getFirstChild() != null) {
                            switch (operationType.getFirstChild().getNodeValue()) {

                                case "simpleOperation" :
                                    operation = importSimpleOperation(operationNode);
                                    break;

                                case "taskOperation" :
                                    operation = importTaskOperation(operationNode);
                                    break;
                            }
                        }
                        if (operation.getName() != null){
                            operations.add(operation);
                        }
                    }
                }
            }
        }
        return operations;
    }

    private Operation importSimpleOperation(Node node) throws ParserConfigurationException {
        SimpleOperation operation = new SimpleOperation();
        operation.setOperationType(OperationType.simpleOperation);

        Element operationElement = (Element) node;

        Node name = operationElement.getElementsByTagName(XMLConstants.OPERATION_NAME).item(0);
        if (name != null && name.getFirstChild() != null){
            operation.setName(name.getFirstChild().getNodeValue());
        }

        Node label = operationElement.getElementsByTagName(XMLConstants.OPERATION_LABEL).item(0);
        if (label != null && label.getFirstChild() != null) {
            operation.setLabel(label.getFirstChild().getNodeValue());
        }

        Node visible = operationElement.getElementsByTagName(XMLConstants.OPERATION_VISIBLE).item(0);
        if (visible != null && visible.getFirstChild() != null) {
            operation.setVisible(Boolean.valueOf(visible.getFirstChild().getNodeValue()));
        }

        Node preExecute = operationElement.getElementsByTagName(XMLConstants.OPERATION_PRE_EXECUTE).item(0);
        if (preExecute != null && preExecute.getFirstChild() != null) {
            operation.setPreExecute(Boolean.valueOf(preExecute.getFirstChild().getNodeValue()));
        }

        Node comment = operationElement.getElementsByTagName(XMLConstants.OPERATION_COMMENT).item(0);
        if (comment != null && comment.getFirstChild() != null) {
            operation.setComment(comment.getFirstChild().getNodeValue());
        }

        Node title = operationElement.getElementsByTagName(XMLConstants.OPERATION_TITLE).item(0);
        if (title != null && title.getFirstChild() != null) {
            operation.setTitle(title.getFirstChild().getNodeValue());
        }

        Node automatic = operationElement.getElementsByTagName(XMLConstants.OPERATION_AUTOMATIC).item(0);
        if (automatic != null && automatic.getFirstChild() != null) {
            operation.setAutomatic(Boolean.valueOf(automatic.getFirstChild().getNodeValue()));
        }

        Node pauseExecution = operationElement.getElementsByTagName(XMLConstants.OPERATION_PAUSE_EXECUTION).item(0);
        if (pauseExecution != null && pauseExecution.getFirstChild() != null) {
            operation.setPauseExecution(Boolean.valueOf(pauseExecution.getFirstChild().getNodeValue()));
        }

        Node operationOrder = operationElement.getElementsByTagName(XMLConstants.OPERATION_OPERATION_ORDER).item(0);
        if (operationOrder != null && operationOrder.getFirstChild() != null) {
            operation.setOperationOrder(Integer.parseInt(operationOrder.getFirstChild().getNodeValue()));
        }

        Node notifyAlternative = operationElement.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_ALTERNATIVE).item(0);
        if (notifyAlternative != null && notifyAlternative.getFirstChild() != null) {
            operation.setNotifyAlternative(Boolean.valueOf(notifyAlternative.getFirstChild().getNodeValue()));
        }

        Node notifyOperation = operationElement.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_OPERATION).item(0);
        if (notifyOperation != null && notifyOperation.getFirstChild() != null) {
            operation.setNotifyOperation(Boolean.valueOf(notifyOperation.getFirstChild().getNodeValue()));
        }

        Node notifyOperationDelay = operationElement.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_OPERATION_DELAY).item(0);
        if (notifyOperationDelay != null && notifyOperationDelay.getFirstChild() != null) {
            operation.setNotifyOperationDelay(Integer.parseInt(notifyOperationDelay.getFirstChild().getNodeValue()));
        }

        Node type = operationElement.getElementsByTagName(XMLConstants.OPERATION_TYPE).item(0);
        if (type != null && type.getFirstChild() != null) {
            switch (type.getFirstChild().getNodeValue()) {

                case "configuration" :
                    operation.setType(SimpleOperationType.configuration);
                    break;

                case "diagnostic" :
                    operation.setType(SimpleOperationType.diagnostic);
                    break;

                case "query" :
                    operation.setType(SimpleOperationType.query);
                    break;
            }
        }

        Node servicio = operationElement.getElementsByTagName(XMLConstants.OPERATION_SERVICE).item(0);
        if (servicio != null && servicio.getFirstChild() != null) {
            operation.setService(servicio.getFirstChild().getNodeValue());
        }

        operation.setAlternativeIds(importAlternativesIds(node));
        operation.setOperationNotifyIds(importOperationIds(node));
        operation.setInParameters(importParameters(node, XMLConstants.PARAMETER_IN_ELEMENT, false,false,false));
        operation.setOutParameters(importParameters(node, XMLConstants.PARAMETER_OUT_ELEMENT, false,true,true));
        operation.setConditions(importConditions(node));

        return operation;
    }

    private Operation importTaskOperation(Node node) throws ParserConfigurationException {
        TaskOperation operation = new TaskOperation();
        operation.setOperationType(OperationType.taskOperation);

        Element operationElement = (Element) node;

        Node name = operationElement.getElementsByTagName(XMLConstants.OPERATION_NAME).item(0);
        if (name != null && name.getFirstChild() != null){
            operation.setName(name.getFirstChild().getNodeValue());
        }

        Node label = operationElement.getElementsByTagName(XMLConstants.OPERATION_LABEL).item(0);
        if (label != null && label.getFirstChild() != null) {
            operation.setLabel(label.getFirstChild().getNodeValue());
        }

        Node visible = operationElement.getElementsByTagName(XMLConstants.OPERATION_VISIBLE).item(0);
        if (visible != null && visible.getFirstChild() != null) {
            operation.setVisible(Boolean.valueOf(visible.getFirstChild().getNodeValue()));
        }

        Node preExecute = operationElement.getElementsByTagName(XMLConstants.OPERATION_PRE_EXECUTE).item(0);
        if (preExecute != null && preExecute.getFirstChild() != null) {
            operation.setPreExecute(Boolean.valueOf(preExecute.getFirstChild().getNodeValue()));
        }

        Node comment = operationElement.getElementsByTagName(XMLConstants.OPERATION_COMMENT).item(0);
        if (comment != null && comment.getFirstChild() != null) {
            operation.setComment(comment.getFirstChild().getNodeValue());
        }

        Node title = operationElement.getElementsByTagName(XMLConstants.OPERATION_TITLE).item(0);
        if (title != null && title.getFirstChild() != null) {
            operation.setTitle(title.getFirstChild().getNodeValue());
        }

        Node automatic = operationElement.getElementsByTagName(XMLConstants.OPERATION_AUTOMATIC).item(0);
        if (automatic != null && automatic.getFirstChild() != null) {
            operation.setAutomatic(Boolean.valueOf(automatic.getFirstChild().getNodeValue()));
        }

        Node pauseExecution = operationElement.getElementsByTagName(XMLConstants.OPERATION_PAUSE_EXECUTION).item(0);
        if (pauseExecution != null && pauseExecution.getFirstChild() != null) {
            operation.setPauseExecution(Boolean.valueOf(pauseExecution.getFirstChild().getNodeValue()));
        }

        Node operationOrder = operationElement.getElementsByTagName(XMLConstants.OPERATION_OPERATION_ORDER).item(0);
        if (operationOrder != null && operationOrder.getFirstChild() != null) {
            operation.setOperationOrder(Integer.parseInt(operationOrder.getFirstChild().getNodeValue()));
        }

        Node notifyAlternative = operationElement.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_ALTERNATIVE).item(0);
        if (notifyAlternative != null && notifyAlternative.getFirstChild() != null) {
            operation.setNotifyAlternative(Boolean.valueOf(notifyAlternative.getFirstChild().getNodeValue()));
        }

        Node notifyOperation = operationElement.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_OPERATION).item(0);
        if (notifyOperation != null && notifyOperation.getFirstChild() != null) {
            operation.setNotifyOperation(Boolean.valueOf(notifyOperation.getFirstChild().getNodeValue()));
        }

        Node notifyOperationDelay = operationElement.getElementsByTagName(XMLConstants.OPERATION_NOTIFY_OPERATION_DELAY).item(0);
        if (notifyOperationDelay != null && notifyOperationDelay.getFirstChild() != null) {
            operation.setNotifyOperationDelay(Integer.parseInt(notifyOperationDelay.getFirstChild().getNodeValue()));
        }

        Node targetSystem = operationElement.getElementsByTagName(XMLConstants.OPERATION_TARGET_SYSTEM).item(0);
        if (targetSystem != null && targetSystem.getFirstChild() != null) {
            operation.setTargetSystem(targetSystem.getFirstChild().getNodeValue());
        }

        Node mailTemplate = operationElement.getElementsByTagName(XMLConstants.OPERATION_MAIL_TEMPLATE).item(0);
        if (mailTemplate != null && mailTemplate.getFirstChild() != null) {
            operation.setMailTemplate(mailTemplate.getFirstChild().getNodeValue());
        }

        Node mailTo = operationElement.getElementsByTagName(XMLConstants.OPERATION_MAIL_TO).item(0);
        if (mailTo != null && mailTo.getFirstChild() != null) {
            operation.setMailTo(mailTo.getFirstChild().getNodeValue());
        }

        Node mailSubjectPrefix = operationElement.getElementsByTagName(XMLConstants.OPERATION_MAIL_SUBJECT_PREFIX).item(0);
        if (mailSubjectPrefix != null && mailSubjectPrefix.getFirstChild() != null) {
            operation.setMailSubjectPrefix(mailSubjectPrefix.getFirstChild().getNodeValue());
        }

        NodeList childs = node.getChildNodes();
        for(int i = 0; i < childs.getLength(); i++){
            if (childs.item(i).getNodeName() == XMLConstants.OPERATION_TYPE && childs.item(i).getFirstChild() != null){
                switch (childs.item(i).getFirstChild().getNodeValue()) {

                    case "delegate" :
                        operation.setType(TaskOperationType.delegate);
                        break;

                    case "close" :
                        operation.setType(TaskOperationType.close);
                        break;

                    case "schedule" :
                        operation.setType(TaskOperationType.schedule);
                        break;

                    case "internalDerivation" :
                        operation.setType(TaskOperationType.internalDerivation);
                        break;
                }
            }
        }

        operation.setAlternativeIds(importAlternativesIds(node));
        operation.setOperationNotifyIds(importOperationIds(node));
        operation.setInParameters(importParameters(node, XMLConstants.PARAMETER_IN_ELEMENT, false,false,false));
        operation.setOutParameters(importParameters(node, XMLConstants.PARAMETER_OUT_ELEMENT, false,true,false));
        operation.setConditions(importConditions(node));
        operation.setGroupsIds(importCandidateGroups(node));

        return operation;
    }

    private List<Alternative> importAlternativesIds(Node node) {
        List<Alternative> alternatives = new LinkedList<>();

        if(node != null){
            NodeList alternativeNodeList = node.getChildNodes();
            if(alternativeNodeList != null){

                for (int i = 0; i < alternativeNodeList.getLength(); i++){
                    if (alternativeNodeList.item(i).getNodeName() == XMLConstants.OPERATION_ALTERNATIVE_IDS) {
                        Alternative alternative = new Alternative();

                        Node alternativeNode = alternativeNodeList.item(i);
                        Element alternativeElement = (Element) alternativeNode;

                        if (alternativeElement.getFirstChild().getNodeValue() != null){
                            alternative.setNextStep(alternativeElement.getFirstChild().getNodeValue());
                        }

                        alternatives.add(alternative);
                    }
                }
            }
        }
        return alternatives;
    }

    private List<OperationNotifyId> importOperationIds(Node node) {
        List<OperationNotifyId> operations = new LinkedList<>();

        if(node != null){
            NodeList operationNodeList = node.getChildNodes();
            if(operationNodeList != null){

                for (int i = 0; i < operationNodeList.getLength(); i++){
                    if (operationNodeList.item(i).getNodeName() == XMLConstants.OPERATION_OPERATION_NOTIFY_IDS) {
                        OperationNotifyId operation = new OperationNotifyId();

                        Node operationNode = operationNodeList.item(i);
                        Element operationElement = (Element) operationNode;

                        if (operationElement.getFirstChild().getNodeValue() != null){
                            operation.setName(operationElement.getFirstChild().getNodeValue());
                        }

                        operations.add(operation);
                    }
                }
            }
        }
        return operations;
    }

    private List<OperationParameter> importParameters(Node node, String in, Boolean property, Boolean isOutParameter, boolean isproperty) {
        List<OperationParameter> parameters = new LinkedList<>();

        if(node != null){
            NodeList parameterNodeList = node.getChildNodes();
            if(parameterNodeList != null){

                for (int i = 0; i < parameterNodeList.getLength(); i++){
                    if (parameterNodeList.item(i).getNodeName() == in) {

                        OperationParameter parameter = new OperationParameter();

                        parameter.setOutParameter(isOutParameter);
                        parameter.setProperty(isproperty);

                        Node parameterNode = parameterNodeList.item(i);
                        Element parameterElement = (Element) parameterNode;

                        Node name = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_NAME).item(0);
                        if (name != null && name.getFirstChild() != null){
                            parameter.setName(name.getFirstChild().getNodeValue());
                        }

                        Node label = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_LABEL).item(0);
                        if (label != null && label.getFirstChild() != null) {
                            parameter.setLabel(label.getFirstChild().getNodeValue());
                        }

                        Node visible = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_VISIBLE).item(0);
                        if (visible != null && visible.getFirstChild() != null) {
                            parameter.setVisible(Boolean.valueOf(visible.getFirstChild().getNodeValue()));
                        }

                        Node visibleWhenIn = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_VISIBLE_WHEN_IN_PARAMETER_EQUALS_CONDITION).item(0);
                        if (visibleWhenIn != null && (visibleWhenIn.getFirstChild()!=null)){
                            parameter.setVisibleWhenInParameterEqualsCondition(visibleWhenIn.getFirstChild().getNodeValue());
                        }

                        Node type = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_TYPE).item(0);
                        if (type != null && type.getFirstChild() != null) {
                            parameter.setType(type.getFirstChild().getNodeValue());
                        }

                        Node description = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_DESCRIPTION).item(0);
                        if (description != null && description.getFirstChild() != null){
                            parameter.setDescription(description.getFirstChild().getNodeValue());
                        }

                        Node value = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_VALUE).item(0);
                        if (value != null && (value.getFirstChild()!=null)) {
                            parameter.setValue(value.getFirstChild().getNodeValue());
                        }

                        Node enable = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_ENABLE).item(0);
                        if (enable != null && enable.getFirstChild() != null) {
                            parameter.setEnable(Boolean.valueOf(enable.getFirstChild().getNodeValue()));
                        }

                        Node required = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_REQUIRED).item(0);
                        if (required != null && required.getFirstChild() != null) {
                            parameter.setRequired(Boolean.valueOf(required.getFirstChild().getNodeValue()));
                        }

                        Node validateExpression = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_VALIDATE_EXPRESSION).item(0);
                        if (validateExpression != null && validateExpression.getFirstChild() != null){
                            parameter.setValidateExpression(validateExpression.getFirstChild().getNodeValue());
                        }

                        Node validateExpressionErrorDescription = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_VALIDATE_EXPRESSION_ERROR_DESCRIPTION).item(0);
                        if (validateExpressionErrorDescription != null && validateExpression.getFirstChild() != null) {
                            parameter.setValidateExpressionErrorDescription(validateExpressionErrorDescription.getFirstChild().getNodeValue());
                        }

                        Node optionValue = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_OPTION_VALUE).item(0);
                        if (optionValue != null && optionValue.getFirstChild() != null) {
                            parameter.setOptionValue(optionValue.getFirstChild().getNodeValue());
                        }

                        Node dateFormat = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_DATE_FORMAT).item(0);
                        if (dateFormat != null && dateFormat.getFirstChild() != null) {
                            parameter.setDateFormat(dateFormat.getFirstChild().getNodeValue());
                        }

                        Node dateFormatRangeEnd = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_DTE_FORMAT_RANGE_END).item(0);
                        if (dateFormatRangeEnd != null && dateFormatRangeEnd.getFirstChild() != null) {
                            parameter.setDateFormatRangeEnd(dateFormatRangeEnd.getFirstChild().getNodeValue());
                        }

                        Node dateFormatFinal = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_DATE_FORMAT_FINAL).item(0);
                        if (dateFormatFinal != null && dateFormatFinal.getFirstChild() != null) {
                            parameter.setDateFormatFinal(dateFormatFinal.getFirstChild().getNodeValue());
                        }

                        Node sourceValueEntity = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_SOURCE_VALUE_ENTITY).item(0);
                        if (sourceValueEntity != null && sourceValueEntity.getFirstChild() != null) {
                            switch (sourceValueEntity.getFirstChild().getNodeValue()) {

                                case "ticket" :
                                    parameter.setSourceValueEntity(SourceEntity.ticket);
                                    break;

                                case "task" :
                                    parameter.setSourceValueEntity(SourceEntity.task);
                                    break;

                                case "guide" :
                                    parameter.setSourceValueEntity(SourceEntity.guide);
                                    break;
                            }
                        }

                        Node sourceValueEntityProperty = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_SOURCE_VALUE_ENTITY_PROPERTY).item(0);
                        if (sourceValueEntityProperty != null && (sourceValueEntityProperty.getFirstChild() != null)) {
                            parameter.setSourceValueEntityProperty(sourceValueEntityProperty.getFirstChild().getNodeValue());
                        }

                        Node convert = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_CONVERT).item(0);
                        if (convert != null && convert.getFirstChild() != null) {
                            parameter.setConvert(Boolean.valueOf(convert.getFirstChild().getNodeValue()));
                        }

                        Node valueWhenInParameterEquals = parameterElement.getElementsByTagName(XMLConstants.PARAMETER_VALUE_WHEN_IN_PARAMETER_EQUALS).item(0);
                        if (valueWhenInParameterEquals != null && valueWhenInParameterEquals.getFirstChild() != null) {
                            parameter.setValueWhenInParameterEquals(valueWhenInParameterEquals.getFirstChild().getNodeValue());
                        }

                        if (!property){
                            parameter.setProperties(importParameters(parameterNode,XMLConstants.PARAMETER_PROPERTIES, true, false,true));
                        }
                        parameter.setConvertCondition(importConvertCondition(parameterNode));
                        parameter.setValidateCrossFieldCondition(importValidateCrossFieldCondition(parameterNode));

                        parameters.add(parameter);
                    }
                }
            }
        }
        return parameters;
    }

    private List<Convertion> importConvertCondition(Node node) {
        List<Convertion> convertions = new LinkedList<>();

        if(node != null){
            NodeList convertionNodeList = node.getChildNodes();
            if(convertionNodeList != null){

                for (int i = 0; i < convertionNodeList.getLength(); i++){
                    if (convertionNodeList.item(i).getNodeName() == XMLConstants.PARAMETER_CONVERT_CONDITION) {
                        Convertion convertion = new Convertion();

                        Node convertionNode = convertionNodeList.item(i);
                        Element convertionElement = (Element) convertionNode;

                        Node condition = convertionElement.getElementsByTagName(XMLConstants.CONVERTION_CONDITION).item(0);
                        if (condition != null && condition.getFirstChild() != null) {
                            convertion.setCondition(condition.getFirstChild().getNodeValue());
                        }

                        Node sourceUnit = convertionElement.getElementsByTagName(XMLConstants.CONVERTION_SOURCE_UNIT).item(0);
                        if (sourceUnit != null && sourceUnit.getFirstChild() != null) {
                            convertion.setSourceUnit(sourceUnit.getFirstChild().getNodeValue());
                        }

                        Node destinationUnit = convertionElement.getElementsByTagName(XMLConstants.CONVERTION_DESTINATION_UNIT).item(0);
                        if (destinationUnit != null && destinationUnit.getFirstChild() != null) {
                            convertion.setDestinationUnit(destinationUnit.getFirstChild().getNodeValue());
                        }

                        convertions.add(convertion);
                    }
                }
            }
        }
        return convertions;
    }

    private List<ValidateCrossFieldCondition> importValidateCrossFieldCondition(Node node) {
        List<ValidateCrossFieldCondition> validateCrossFieldConditions = new LinkedList<>();

        if(node != null){
            NodeList validateNodeList = node.getChildNodes();
            if(validateNodeList != null){

                for (int i = 0; i < validateNodeList.getLength(); i++){
                    if (validateNodeList.item(i).getNodeName() == XMLConstants.PARAMETER_VALIDATE_CROSS_FIELD_CONDITION) {
                        ValidateCrossFieldCondition validateCrossFieldCondition = new ValidateCrossFieldCondition();

                        Node validateNode = validateNodeList.item(i);
                        Element validateElement = (Element) validateNode;

                        Node fieldName = validateElement.getElementsByTagName(XMLConstants.VALIDATE_CROSS_FIELD_CONDITION_FIELD_NAME_).item(0);
                        if (fieldName != null && fieldName.getFirstChild() != null) {
                            validateCrossFieldCondition.setFieldName(fieldName.getFirstChild().getNodeValue());
                        }

                        Node condition = validateElement.getElementsByTagName(XMLConstants.VALIDATE_CROSS_FIELD_CONDITION_CONDITION_).item(0);
                        if (condition != null && condition.getFirstChild() != null) {
                            validateCrossFieldCondition.setCondition(condition.getFirstChild().getNodeValue());
                        }

                        Node messageError = validateElement.getElementsByTagName(XMLConstants.VALIDATE_CROSS_FIELD_CONDITION_MESSAGE_ERROR).item(0);
                        if (messageError != null && messageError.getFirstChild() != null) {
                            validateCrossFieldCondition.setMessageError(messageError.getFirstChild().getNodeValue());
                        }

                        validateCrossFieldConditions.add(validateCrossFieldCondition);
                    }
                }
            }
        }
        return validateCrossFieldConditions;
    }

    private List<Condition> importConditions(Node doc) throws ParserConfigurationException {
        List<Condition> conditions = new LinkedList<>();

        if(doc != null){
            NodeList conditionNodeList = doc.getChildNodes();
            if(conditionNodeList != null){

                for (int i = 0; i < conditionNodeList.getLength(); i++){
                    if (conditionNodeList.item(i).getNodeName() == XMLConstants.CONDITION_ELEMENT) {
                        Node conditionNode = conditionNodeList.item(i);
                        Document docCondition = nodeToDocument(conditionNode);
                        Condition condition = importCondition(docCondition,"condition");
                        conditions.add(condition);
                    }
                }
            }
        }
        return conditions;
    }

    private Condition importCondition(Document node, String operator) throws ParserConfigurationException {
        Condition condition = new Condition();
        NodeList conditionNodeList;

        switch (operator) {

            case "operator1" :
                conditionNodeList = node.getElementsByTagName(XMLConstants.BINARY_CONDITION_OPERATOR_UNO);
                break;

            case "operator2" :
                conditionNodeList = node.getElementsByTagName(XMLConstants.BINARY_CONDITION_OPERATOR_DOS);
                break;

            default:
                conditionNodeList = node.getElementsByTagName(XMLConstants.CONDITION_ELEMENT);
        }

        Node conditionNode = conditionNodeList.item(0);
        Element conditionElement = (Element) conditionNode;

        Node conditionType = conditionElement.getAttributeNode("xsi:type");
        String tagCondition = conditionType.getNodeValue();
        if (tagCondition != null) {
            switch (tagCondition) {

                case "ttg:UnaryCondition" :
                    condition.setType(TypeOperation.unaryCondition);
                    Node operationName = conditionElement.getElementsByTagName(XMLConstants.UNARY_CONDITION_OPERATION_NAME).item(0);
                    if (operationName != null && operationName.getFirstChild() != null) {
                        condition.setOperation(operationName.getFirstChild().getNodeValue());
                    }
                    condition.setConditionParameter(importConditionParameters(conditionNode));
                    break;

                case "ttg:BinaryCondition" :
                    condition.setType(TypeOperation.binaryCondition);
                    Node binaryOperator = conditionElement.getElementsByTagName(XMLConstants.BINARY_CONDITION_OPERATION).item(0);
                    if (binaryOperator != null && binaryOperator.getFirstChild() != null) {
                        condition.setOperation(binaryOperator.getFirstChild().getNodeValue());
                    }
                    Node operator1 = conditionElement.getElementsByTagName(XMLConstants.BINARY_CONDITION_OPERATOR_UNO).item(0);
                    Node operator2 = conditionElement.getElementsByTagName(XMLConstants.BINARY_CONDITION_OPERATOR_DOS).item(0);
                    Document docOperator1 = nodeToDocument(operator1);
                    Document docOperator2 = nodeToDocument(operator2);
                    condition.setHijoIzquierdo(importCondition(docOperator1, "operator1"));
                    condition.setHijoDerecho(importCondition(docOperator2, "operator2"));
                    break;
            }
        }
        return condition;
    }

    private List<ConditionParameter> importConditionParameters(Node node) {
        List<ConditionParameter> conditionParameters = new LinkedList<>();

        if(node != null){
            NodeList conditionParameterNodeList = node.getChildNodes();
            if(conditionParameterNodeList != null){

                for (int i = 0; i < conditionParameterNodeList.getLength(); i++){
                    if (conditionParameterNodeList.item(i).getNodeName() == XMLConstants.CONDITION_ENABLE_ALTERNATIVE) {
                        ConditionParameter conditionParameter = new ConditionParameter();

                        Node conditionParameterNode = conditionParameterNodeList.item(i);
                        Element conditionParameterElement = (Element) conditionParameterNode;

                        Node field = conditionParameterElement.getElementsByTagName(XMLConstants.UNARY_CONDITION_FIELD).item(0);
                        if (field != null && field.getFirstChild() != null) {
                            conditionParameter.setField(field.getFirstChild().getNodeValue());
                        }

                        Node fieldType = conditionParameterElement.getElementsByTagName(XMLConstants.UNARY_CONDITION_FIELD_TYPE).item(0);
                        if (fieldType != null && fieldType.getFirstChild() != null) {
                            conditionParameter.setFieldType(fieldType.getFirstChild().getNodeValue());
                        }

                        Node operator = conditionParameterElement.getElementsByTagName(XMLConstants.UNARY_CONDITION_OPERATOR).item(0);
                        if (operator != null && operator.getFirstChild() != null) {
                            conditionParameter.setOperator(operator.getFirstChild().getNodeValue());
                        }

                        Node value = conditionParameterElement.getElementsByTagName(XMLConstants.UNARY_CONDITION_VALUE).item(0);
                        if (value != null && value.getFirstChild() != null) {
                            conditionParameter.setValue(value.getFirstChild().getNodeValue());
                        }

                        conditionParameters.add(conditionParameter);
                    }
                }
            }
        }
        return conditionParameters;
    }

    private List<Groups> importCandidateGroups(Node node) {
        List<Groups> candidates = new LinkedList<>();

        if(node != null){
            NodeList candidateNodeList = node.getChildNodes();
            if(candidateNodeList != null){

                for (int i = 0; i < candidateNodeList.getLength(); i++){
                    if (candidateNodeList.item(i).getNodeName() == XMLConstants.OPERATION_CANDIDATE_GROUPS) {
                        Node candidateNode = candidateNodeList.item(i);
                        Element candidateElement = (Element) candidateNode;

                        NodeList groupNames = candidateElement.getElementsByTagName(XMLConstants.CANDIDATE_GROUPS_GROUP_NAME);

                        for (int j = 0; j < groupNames.getLength(); j++){
                            Groups groups = new Groups();
                            Node groupName = candidateElement.getElementsByTagName(XMLConstants.CANDIDATE_GROUPS_GROUP_NAME).item(j);
                            if (groupName != null && groupName.getFirstChild() != null) {
                                groups.setGroupName(groupName.getFirstChild().getNodeValue());
                            }
                            candidates.add(groups);
                        }
                    }
                }
            }
        }
        return candidates;
    }

    private Document nodeToDocument(Node node) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document newDocument = builder.newDocument();
        Node importedNode = newDocument.importNode(node, true);
        newDocument.appendChild(importedNode);
        return newDocument;
    }

    private Document parseXML(String filePath) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(filePath);
        doc.getDocumentElement().normalize();
        return doc;
    }

    public List<Pair<String, byte[]>> guidePrints (Guide mainGuide){

        List<Pair<String, byte[]>> files = new LinkedList<>();
        files.add(new Pair<>(mainGuide.getName() + ".xml", (GuidePrint(mainGuide))));

        if(mainGuide.getGuides()!= null){
            for(Guide guide : mainGuide.getGuides()){
                files.add(new Pair<>(guide.getName() + ".xml", (GuidePrint(mainGuide))));
            }
        }
        return files;
    }

    public byte[] GuidePrint(Guide guide) {
        try {
            File file = new File(XMLConstants.GUIDE_XML_LOCATION);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc = configureGuidXML(doc, guide);

            doc = configureStepsXML(doc, guide);

            printResult(doc);

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
                idStep.setTextContent(step.getTextId());
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

        mimeType.setTextContent(referenceDoc.getMimeType());
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

            if (taskOperation.getGroupsIds() != null && taskOperation.getGroupsIds().size() > 0){
                Node newCand = docOperation.getElementsByTagName(XMLConstants.OPERATION_ELEMENT).item(0);
                Node refNode = docOperation.getElementsByTagName(XMLConstants.OPERATION_MAIL_TEMPLATE).item(0);

                for (Groups candidate : taskOperation.getGroupsIds()) {
                    Node cand = configureCandidate(doc, candidate);
                    newCand.insertBefore(docOperation.importNode(cand, true), refNode);
                }
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

    private Node configureCandidate(Document doc, Groups candidate) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(XMLConstants.CANDIDATE_GROUPS_XML_LOCATION);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document docCandidateGroups = dBuilder.parse(file);

        Node groupName = docCandidateGroups.getElementsByTagName(XMLConstants.CANDIDATE_GROUPS_GROUP_NAME).item(0);
        groupName.setTextContent(candidate.getGroupName());

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

        Node fieldName = docValidate.getElementsByTagName(XMLConstants.VALIDATE_CROSS_FIELD_CONDITION_FIELD_NAME_).item(0);
        fieldName.setTextContent(validateCrossFieldCondition.getFieldName());
        Node condition = docValidate.getElementsByTagName(XMLConstants.VALIDATE_CROSS_FIELD_CONDITION_CONDITION_).item(0);
        condition.setTextContent(validateCrossFieldCondition.getCondition());
        Node messageError = docValidate.getElementsByTagName(XMLConstants.VALIDATE_CROSS_FIELD_CONDITION_MESSAGE_ERROR).item(0);
        messageError.setTextContent(validateCrossFieldCondition.getMessageError());

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
