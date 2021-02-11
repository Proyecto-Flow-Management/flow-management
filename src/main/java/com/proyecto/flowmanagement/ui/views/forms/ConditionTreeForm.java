package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.commun.ConditionDTO;
import com.proyecto.flowmanagement.backend.commun.TestDTO;
import com.proyecto.flowmanagement.backend.def.TypeOperation;
import com.proyecto.flowmanagement.backend.persistence.entity.Condition;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.ui.*;
import org.aspectj.weaver.ast.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class ConditionTreeForm  extends HorizontalLayout {

    public TreeGrid<Condition> arbolCondition = new TreeGrid<Condition>(Condition.class);
    public List<Condition> conditions;
    TreeDataProvider<Condition> dataProvider;
    TreeData<Condition> data;

    public ConditionTreeForm(){

        this.conditions = new LinkedList<>();

        setSizeFull();

        configureGrid();

        configureElements();

        updateGrid();

        add(arbolCondition);
    }

    public ConditionTreeForm(List<Condition> conditions){

        this.conditions = conditions;

        setSizeFull();

        configureGrid();

        configureElements();

        updateGrid();

        add(arbolCondition);
    }

    public void updateGrid() {
        data.clear();
        arbolCondition.getDataProvider().refreshAll();
        for (Condition cond : this.conditions) {
            data.addItem(null, cond);
            if(cond.getHijoIzquierdo() != null)
                agregarNodo(cond, cond.getHijoIzquierdo());
            if(cond.getHijoDerecho() != null)
                agregarNodo(cond, cond.getHijoDerecho());
        }
        arbolCondition.getDataProvider().refreshAll();
    }

    public void addCondition(Condition conditions)
    {
        data.addItem(null,conditions);
    }

    private void agregarNodo(Condition padre,Condition hijo) {

        data.addItem(padre, hijo);

        if(hijo.getHijoIzquierdo() != null)
            agregarNodo(hijo, hijo.getHijoIzquierdo());

        if(hijo.getHijoDerecho() != null)
            agregarNodo(hijo, hijo.getHijoDerecho());
    }

    private void configureGrid() {
        arbolCondition.setWidthFull();
        arbolCondition.removeAllColumns();
        arbolCondition.addColumn("operation");
        arbolCondition.setHierarchyColumn("operation");
        TreeDataProvider<Condition> dataProvider = (TreeDataProvider<Condition>) arbolCondition.getDataProvider();
        arbolCondition.addComponentColumn(this::getFoo).setHeader(new Span("Header Component")).setId("someID");
        data  = dataProvider.getTreeData();
        arbolCondition.setHierarchyColumn("operation");
    }

    private void configureElements() {
        add(arbolCondition);
    }

    public Component getFoo(Condition s) {
        Div div = new Div();
        div.setText("My text: " + s.getOperation());
        return div;
    }
}
