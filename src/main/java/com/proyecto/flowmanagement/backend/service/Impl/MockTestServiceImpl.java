package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.persistence.entity.*;
import com.proyecto.flowmanagement.backend.def.OperationType;
import com.proyecto.flowmanagement.backend.def.SimpleOperationType;
import com.proyecto.flowmanagement.backend.def.TaskOperationType;
import com.proyecto.flowmanagement.backend.persistence.entity.*;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class MockTestServiceImpl {

    public Guide GetGuide(String name, String label, String mainStep)
    {

        Guide guideNew = new Guide();

        guideNew.setName(name);
        guideNew.setLabel(label);
        guideNew.setMainStep(mainStep);

        List<Step> steps = new LinkedList<Step>();

        Step stepOne = new Step();

        List<Alternative> stepAlternative = new LinkedList<Alternative>();
        List<Operation> stepOperation = new LinkedList<Operation>();

        List<Operation> guideOperation = new LinkedList<Operation>();

        SimpleOperation operation1Guide = new SimpleOperation();
        TaskOperation operation2Guide = new TaskOperation();

        operation1Guide.setLabel("Label Operation 1 Guide Chito");
        operation1Guide.setName("Name Operation 1 Guide");
        operation1Guide.setVisible(true);
        operation1Guide.setOperationType(OperationType.simpleOperation);
        operation1Guide.setType(SimpleOperationType.configuration);
        operation2Guide.setLabel("Label Operation 2 Guide");
        operation2Guide.setName("Name Operation 2 Guide");
        operation2Guide.setOperationType(OperationType.taskOperation);
        operation2Guide.setType(TaskOperationType.close);


        guideOperation.add(operation1Guide);
        guideOperation.add(operation2Guide);
        Alternative alternative1Step1 = new Alternative();
        Alternative alternative2Step1 = new Alternative();

        Operation operation1Step1 = new Operation();
        Operation operation2Step1 = new Operation();

        alternative1Step1.setLabel("CF_EA_CW_GESTIONABLE");

        //Unary
        UnaryCondition unaryCondition = new UnaryCondition();
        unaryCondition.setOperationName("ACS_getCPEidByInternalNumber");
        ConditionParameter conditionParameterUnary = new ConditionParameter();
        conditionParameterUnary.setField("gestionable");
        conditionParameterUnary.setFieldType("string");
        conditionParameterUnary.setOperator("=");
        conditionParameterUnary.setValue("HABILITADA");
        unaryCondition.setConditionParameter(conditionParameterUnary);
        alternative1Step1.setConditions(unaryCondition);


        // BinaryCondition
        List<UnaryCondition> listUnary = new LinkedList<>();

        BinaryCondition binaryCondition = new BinaryCondition();
        binaryCondition.setOperator("OR");

        UnaryCondition unaryCondition1A2 = new UnaryCondition();
        unaryCondition1A2.setOperationName("consultaEstadoGSF");
        ConditionParameter conditionParameterO1A2 = new ConditionParameter();
        conditionParameterO1A2.setField("consultaEstadoGSF.estado_pon.phase_state");
        conditionParameterO1A2.setFieldType("string");
        conditionParameterO1A2.setOperator("=");
        conditionParameterO1A2.setValue("OK");
        unaryCondition1A2.setConditionParameter(conditionParameterO1A2);


        UnaryCondition unaryCondition2A2 = new UnaryCondition();
        unaryCondition2A2.setOperationName("consultaEstadoGSF");
        ConditionParameter conditionParameterO2A2 = new ConditionParameter();
        conditionParameterO2A2.setField("consultaEstadoGSF.estado_pon.phase_state");
        conditionParameterO2A2.setFieldType("string");
        conditionParameterO2A2.setOperator("=");
        conditionParameterO2A2.setValue("SIN INFORMACION");
        unaryCondition2A2.setConditionParameter(conditionParameterO2A2);

        listUnary.add(unaryCondition1A2);
        listUnary.add(unaryCondition2A2);

        binaryCondition.setConditions(listUnary);
        alternative2Step1.setBinaryConditions(binaryCondition);

        alternative2Step1.setLabel("Label Alternative 2 Step 1");

        stepAlternative.add(alternative1Step1);
        stepAlternative.add(alternative2Step1);
        stepOne.setAlternatives(stepAlternative);

        operation1Step1.setLabel("Label Operation 1 Step 1");
        operation1Step1.setName("Name Operation 1 Step 1");
        operation2Step1.setLabel("Label Operation 2 Step 1");
        operation2Step1.setName("Name Operation 2 Step 1");

        stepOperation.add(operation1Step1);
        stepOperation.add(operation2Step1);
        stepOne.setOperations(stepOperation);

        stepOne.setLabel("Label TEST 1");
        stepOne.setText("Name TEST 1");

        steps.add(stepOne);

        guideNew.setOperations(guideOperation);
        guideNew.setSteps(steps);

        return guideNew;
    }
}
