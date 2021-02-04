package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.def.OperationType;
import com.proyecto.flowmanagement.backend.def.SimpleOperationType;
import com.proyecto.flowmanagement.backend.def.TaskOperationType;
import com.proyecto.flowmanagement.backend.def.TypeOperation;
import com.proyecto.flowmanagement.backend.persistence.entity.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class MockTestServiceImpl {

    public Guide GetGuide(String name, String label, String mainStep)
    {
        Guide guide = new Guide();
        guide.setName(name);
        guide.setLabel(label);
        guide.setMainStep(mainStep);

        List<Step> steps = new LinkedList<Step>();

        Step step = new Step();
        step.setTextId("StepId");
        step.setLabel("Label");
        step.setText("Description");

        SimpleOperation simpleOperation = new SimpleOperation();
        simpleOperation.setName("operationName");
        simpleOperation.setLabel("operationLabel");
        simpleOperation.setVisible(true);
        simpleOperation.setOperationType(OperationType.simpleOperation);
        simpleOperation.setType(SimpleOperationType.query);
        simpleOperation.setService("query");
        simpleOperation.setOperationOrder(1);
        simpleOperation.setNotifyOperationDelay(20);

        List<Condition> conditions = new LinkedList<Condition>();

        Condition condition1 = new Condition();
        condition1.setType(TypeOperation.unaryCondition);
        condition1.setOperation("TINCO");

        ConditionParameter conditionParameter = new ConditionParameter();
        conditionParameter.setField("descEstado");
        conditionParameter.setFieldType("string");
        conditionParameter.setOperator("=");
        conditionParameter.setValue("ACTIVO");

        List<ConditionParameter> conditionParameters = new LinkedList<>();
        conditionParameters.add(conditionParameter);

        condition1.setConditionParameter(conditionParameters);
        conditions.add(condition1);

        List<Alternative> alternatives = new LinkedList<>();
        Alternative alternative1 = new Alternative();
        alternative1.setNextStep("alternative1");
        Alternative alternative2 = new Alternative();
        alternative2.setNextStep("alternative2");
        Alternative alternative3 = new Alternative();
        alternative3.setNextStep("alternative3");
        Alternative alternative4 = new Alternative();
        alternative4.setNextStep("alternative4");
        alternatives.add(alternative1);
        alternatives.add(alternative2);
        alternatives.add(alternative3);
        alternatives.add(alternative4);

        simpleOperation.setAlternativeIds(alternatives);

        simpleOperation.setConditions(conditions);

        OperationParameter operationParameter = new OperationParameter();
        operationParameter.setName("NameParameter");
        operationParameter.setType("TypeParameter");
        operationParameter.setDescription("DescriptionParameter");

        List<OperationParameter> operationParameterList = new LinkedList<>();
        operationParameterList.add(operationParameter);

        simpleOperation.setInParameters(operationParameterList);

        List<Operation> operations = new LinkedList<>();
        operations.add(simpleOperation);

        step.setOperations(operations);
        steps.add(step);
        guide.setSteps(steps);
        //guide.setOperations(operations);

        return guide;
    }
}
