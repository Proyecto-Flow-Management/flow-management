package com.proyecto.flowmanagement.backend.def;

public final class XMLConstants {

    //XML Templates
    public static final String GUIDE_XML_LOCATION = "src/main/resources/XMLResources/GuideXML.xml";
    public static final String STEP_XML_LOCATION = "src/main/resources/XMLResources/StepXML.xml";

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

    // AUX
    public static final String GUIDE_RESULT = "src/main/resources/XMLResources/GuideResult.xml";

}
