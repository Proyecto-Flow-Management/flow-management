package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
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

        Operation operation1Guide = new Operation();
        Operation operation2Guide = new Operation();

        operation1Guide.setLabel("Label Operation 1 Guide");
        operation1Guide.setName("Name Operation 1 Guide");
        operation2Guide.setLabel("Label Operation 2 Guide");
        operation2Guide.setName("Name Operation 2 Guide");

        guideOperation.add(operation1Guide);
        guideOperation.add(operation2Guide);

        Alternative alternative1Step1 = new Alternative();
        Alternative alternative2Step1 = new Alternative();

        Operation operation1Step1 = new Operation();
        Operation operation2Step1 = new Operation();

        alternative1Step1.setLabel("Label Alternative 1 Step 1");
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

        guideNew.setSteps(steps);

        return guideNew;
    }
}
