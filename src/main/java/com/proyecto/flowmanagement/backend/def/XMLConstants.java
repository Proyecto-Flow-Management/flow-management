package com.proyecto.flowmanagement.backend.def;

public final class XMLConstants {

    //XML Templates
    public static final String GUIDE_XML_LOCATION = "src/main/resources/XMLResources/GuideXML.xml";
    public static final String STEP_XML_LOCATION = "src/main/resources/XMLResources/StepXML.xml";
    public static final String SIMPLE_OPERATION_XML_LOCATION = "src/main/resources/XMLResources/SimpleOperationXML.xml";
    public static final String TASK_OPERATION_XML_LOCATION = "src/main/resources/XMLResources/TaskOperationXML.xml";
    public static final String IN_PARAMETER_XML_LOCATION = "src/main/resources/XMLResources/InParameterXML.xml";
    public static final String OUT_PARAMETER_XML_LOCATION = "src/main/resources/XMLResources/OutParameterXML.xml";
    public static final String ALTERNATIVE_XML_LOCATION = "src/main/resources/XMLResources/AlternativeXML.xml";
    public static final String UNARY_CONDITION_XML_LOCATION = "src/main/resources/XMLResources/UnaryConditionXML.xml";
    public static final String BINARY_CONDITION_XML_LOCATION = "src/main/resources/XMLResources/BinaryConditionXML.xml";
    public static final String OPERATOR_UNO_UNARY_CONDITION_XML_LOCATION = "src/main/resources/XMLResources/Operator1UnaryXML.xml";
    public static final String OPERATOR_DOS_UNARY_CONDITION_XML_LOCATION = "src/main/resources/XMLResources/Operator2UnaryXML.xml";
    public static final String OPERATOR_UNO_BINARY_CONDITION_XML_LOCATION = "src/main/resources/XMLResources/Operator1BinaryXML.xml";
    public static final String OPERATOR_DOS_BINARY_CONDITION_XML_LOCATION = "src/main/resources/XMLResources/Operator2BinaryXML.xml";
    public static final String CONDITION_ENABLE_ALTERNATIVE_LOCATION = "src/main/resources/XMLResources/ConditionParameterXML.xml";


    //GUIDE ATTRIBUTES
    public static final String GUIDE_ELEMENT = "ttg:guide";
    public static final String MAIN_STEP_ID = "ttg:mainStepId";

    public static final String XMLNS_VC = "http://www.w3.org/2007/XMLSchema-versioning";
    public static final String XMLNS_TTG = "http://ns.antel.com.uy/schema/troubleticket/guide";
    public static final String XMLNS_SCHEMALOCATION = "http://ns.antel.com.uy/schema/troubleticket/guide file:/D:/generic-app/Guide.xsd";
    public static final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";

    //STEPS ATTRIBUTES
    public static final String STEP_ELEMENT = "ttg:step";
    public static final String STEP_ID = "ttg:stepId";
    public static final String STEP_LABEL = "ttg:label";
    public static final String STEP_TEXT = "ttg:text";

    //ALTERNATIVES ATTRIBUTES
    public static final String ALTERNATIVE_ELEMENT = "ttg:alternative";
    public static final String ALTERNATIVE_STEP_ID = "ttg:stepId";
    public static final String ALTERNATIVE_GUIDE_NAME = "ttg:guideName";
    public static final String ALTERNATIVE_LABEL = "ttg:label";

    //CONDITIONS
    public static final String UNARY_CONDITION_ELEMENT = "ttg:condition";
    public static final String BINARY_CONDITION_ELEMENT = "ttg:condition";
    public static final String CONDITION_ENABLE_ALTERNATIVE = "ttg:conditionsEnableAlternative";

    //UnaryConditions
    public static final String UNARY_CONDITION_OPERATION_NAME= "ttg:operationName";
    public static final String UNARY_CONDITION_OPERATOR_UNO = "ttg:operator1";
    public static final String UNARY_CONDITION_OPERATOR_DOS = "ttg:operator2";
    public static final String UNARY_CONDITION_FIELD= "ttg:field";
    public static final String UNARY_CONDITION_FIELD_TYPE= "ttg:fieldType";
    public static final String UNARY_CONDITION_OPERATOR= "ttg:operator";
    public static final String UNARY_CONDITION_VALUE= "ttg:value";

    //BinaryCondition
    public static final String BINARY_CONDITION_OPERATION = "ttg:operation";
    public static final String BINARY_CONDITION_OPERATOR_UNO = "ttg:operator1";
    public static final String BINARY_CONDITION_OPERATOR_DOS = "ttg:operator2";


    //OPERATION ATTRIBUTES
    public static final String OPERATION_ELEMENT = "ttg:operation";
    public static final String OPERATION_NAME = "ttg:name";
    public static final String OPERATION_LABEL = "ttg:label";
    public static final String OPERATION_VISIBLE = "ttg:visible";
    public static final String OPERATION_PRE_EXECUTE = "ttg:preExecute";
    public static final String OPERATION_COMMENT = "ttg:comment";
    public static final String OPERATION_TITLE = "ttg:title";
    public static final String OPERATION_AUTOMATIC = "ttg:automatic";
    public static final String OPERATION_PAUSE_EXECUTION = "ttg:pauseExecution";
    public static final String OPERATION_OPERATION_ORDER = "ttg:operationOrder";
    public static final String OPERATION_OPERATION_TYPE = "ttg:operationType";
    public static final String OPERATION_IN_PARAMETERS = "ttg:inParameters";
    public static final String OPERATION_OUT_PARAMETERS = "ttg:outParameters";
    public static final String OPERATION_CONDITION = "ttg:condition";
    public static final String OPERATION_NOTIFY_ALTERNATIVE = "ttg:notifyAlternative";
    public static final String OPERATION_ALTERNATIVE_IDS = "ttg:alternativeIds";
    public static final String OPERATION_NOTIFY_OPERATION = "ttg:notifyOperation";
    public static final String OPERATION_OPERATION_NOTIFY_IDS = "ttg:operationNotifyIds";
    public static final String OPERATION_NOTIFY_OPERATION_DELAY = "ttg:notifyOperationDelay";
    public static final String OPERATION_TYPE = "ttg:type";
    public static final String OPERATION_SERVICE = "ttg:servicio";
    public static final String OPERATION_TARGET_SYSTEM = "ttg:targetSystem";
    public static final String OPERATION_CANDIDATE_GROUPS = "ttg:candidateGroups";
    public static final String OPERATION_MAIL_TEMPLATE = "ttg:mailTemplate";
    public static final String OPERATION_MAIL_TO = "ttg:mailTo";
    public static final String OPERATION_MAIL_SUBJECT_PREFIX = "ttg:mailSubjectPrefix";

    //OPERATION PARAMETER ATTRIBUTES
    public static final String PARAMETER_IN_ELEMENT = "ttg:inParameters";
    public static final String PARAMETER_OUT_ELEMENT = "ttg:outParameters";
    public static final String PARAMETER_NAME = "ttg:name";
    public static final String PARAMETER_LABEL = "ttg:label";
    public static final String PARAMETER_VISIBLE = "ttg:visible";
    public static final String PARAMETER_VISIBLE_WHEN_IN_PARAMETER_EQUALS_CONDITION = "ttg:visibleWhenInParameterEqualsCondition";
    public static final String PARAMETER_TYPE = "ttg:type";
    public static final String PARAMETER_DESCRIPTION = "ttg:description";
    public static final String PARAMETER_VALUE = "ttg:value";
    public static final String PARAMETER_ENABLE = "ttg:enable";
    public static final String PARAMETER_REQUIRED = "ttg:required";
    public static final String PARAMETER_VALIDATE_EXPRESSION = "ttg:validateExpression";
    public static final String PARAMETER_VALIDATE_EXPRESSION_ERROR_DESCRIPTION = "ttg:validateExpressionErrorDescription";
    public static final String PARAMETER_OPTION_VALUE = "ttg:optionValue";
    public static final String PARAMETER_DATE_FORMAT = "ttg:dateFormat";
    public static final String PARAMETER_DTE_FORMAT_RANGE_END = "ttg:dateFormatRangeEnd";
    public static final String PARAMETER_DATE_FORMAT_FINAL = "ttg:dateFormatFinal";
    public static final String PARAMETER_SOURCE_VALUE_ENTITY = "ttg:sourceValueEntity";
    public static final String PARAMETER_SOURCE_VALUE_ENTITY_PROPERTY = "ttg:sourceValueEntityProperty";
    public static final String PARAMETER_PROPERTIES = "ttg:properties";
    public static final String PARAMETER_CONVERT = "ttg:convert";
    public static final String PARAMETER_CONVERT_CONDITION = "ttg:convertCondition";
    public static final String PARAMETER_VALIDATE_CROSS_FIELD_CONDITION = "ttg:validateCrossFieldCondition";
    public static final String PARAMETER_VALUE_WHEN_IN_PARAMETER_EQUALS = "ttg:valueWhenInParameterEquals";

    // AUX
    public static final String GUIDE_RESULT = "src/main/resources/XMLResources/GuideResult.xml";
    public static final String AUX = "ttg:auxiliar";

}
