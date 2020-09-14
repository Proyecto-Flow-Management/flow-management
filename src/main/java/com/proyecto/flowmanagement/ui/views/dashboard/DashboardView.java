package com.proyecto.flowmanagement.ui.views.dashboard;

import com.proyecto.flowmanagement.backend.service.UserService;
import com.proyecto.flowmanagement.backend.service.RolService;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Map;

@PageTitle("Dashboard | Flow Management")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

    private final UserService userService;
    private final RolService rolService;

    public DashboardView(UserService userService, RolService rolService) {
        this.userService = userService;
        this.rolService = rolService;

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(
                getUserStats(),
                getRolesChart()
        );
    }

    private Span getUserStats() {
        Span stats = new Span(userService.count() + " users.");
        stats.addClassName("user-stats");

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
