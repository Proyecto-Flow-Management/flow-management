package com.proyecto.flowmanagement.ui.views.dashboard;

import com.proyecto.flowmanagement.backend.service.CompanyService;
import com.proyecto.flowmanagement.backend.service.ContactService;
import com.proyecto.flowmanagement.backend.service.RolService;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Map;

@PageTitle("Dashboard | Flow Management")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

    private final ContactService contactService;
    private final RolService rolService;

    public DashboardView(ContactService contactService,  RolService rolService) {
        this.contactService = contactService;
        this.rolService = rolService;

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(
                getContactStats(),
                getRolesChart()
        );
    }

    private Span getContactStats() {
        Span stats = new Span(contactService.count() + " contacts.");
        stats.addClassName("contact-stats");

        return stats;
    }

    private Component getRolesChart(){
        Chart chart = new Chart(ChartType.PIE);

        DataSeries dataSeries = new DataSeries();
        Map<String, Integer> stats = rolService.getStats();
        stats.forEach((name, number) ->
                dataSeries.add(new DataSeriesItem(name, number)));

        chart.getConfiguration().setSeries(dataSeries);
        return chart;

    }


}
