package com.proyecto.flowmanagement.ui.views.forms;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class GuideDetailsForm extends VerticalLayout {

    Accordion accordion = new Accordion();

    VerticalLayout personalInformationLayout = new VerticalLayout();

    TextField uno = new TextField("Name");
    TextField dos = new TextField("Name");
    TextField tres = new TextField("Name");

    public GuideDetailsForm()
    {
        accordion.add("Personal Information", personalInformationLayout);

        VerticalLayout billingAddressLayout = new VerticalLayout();

        billingAddressLayout.add(
                new TextField("Address"),
                new TextField("City"),
                new TextField("State"),
                new TextField("Zip Code")
        );
        accordion.add("Billing Address", billingAddressLayout);

        VerticalLayout paymentLayout = new VerticalLayout();
        paymentLayout.add(
                new Span("Not yet implemented")
        );
        AccordionPanel billingAddressPanel = accordion.add("Payment", paymentLayout);
        billingAddressPanel.setEnabled(false);
        add(accordion);
    }
}
