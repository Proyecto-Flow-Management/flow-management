package com.proyecto.flowmanagement.backend.def;

public final class XMLConstants {

    //XML Templates
    public static final String GUIDE_XML_LOCATION = "src/main/resources/XMLResources/GuideXML.xml";
    public static final String STEP_XML_LOCATION = "src/main/resources/XMLResources/StepXML.xml";
    public static final String ALTERNATIVE_XML_LOCATION = "src/main/resources/XMLResources/AlternativeXML.xml";
    public static final String UNARY_CONDITION_XML = "src/main/resources/XMLResources/UnaryCondition.xml";

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
    public static final String STEP_TEXT = "ttg:label";
    public static final String STEP_LABEL = "ttg:text";

    //ALTERNATIVES ATTRIBUTES
    public static final String ALTERNATIVE = "ttg:alternative";
    public static final String ALTERNATIVE_STEP_ID = "ttg:stepId";
    public static final String ALTERNATIVE_GUIDE_NAME = "ttg:guideName";
    public static final String ALTERNATIVE_LABEL = "ttg:label";

    //CONDITIONS
    public static final String ALTERNATIVE_CONDITION = "ttg:condition";

    //UnaryConditions
    public static final String UNARY_CONDITION_OPERATION_NAME= "ttg:operationName";
    public static final String UNARY_CONDITION_FIELD= "ttg:field";
    public static final String UNARY_CONDITION_FIELD_TYPE= "ttg:fieldType";
    public static final String UNARY_CONDITION_OPERATOR= "ttg:operator";
    public static final String UNARY_CONDITION_VALUE= "ttg:value";

    // AUX
    public static final String GUIDE_RESULT = "src/main/resources/XMLResources/GuideResult.xml";

}
