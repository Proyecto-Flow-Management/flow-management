package com.proyecto.flowmanagement;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;

/**
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@PWA(name = "Flow Management", shortName = "Flow Management", enableInstallPrompt = false)
public class AppShell implements AppShellConfigurator {
}
