module pizzashop {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    opens pizzashop.model to javafx.base;
    exports pizzashop.model;
    opens pizzashop to javafx.fxml;
    exports pizzashop;
    opens pizzashop.controller to javafx.fxml;
    exports pizzashop.controller;
    exports pizzashop.repository;
    exports pizzashop.service;

    // Permite testelor acces la module
    opens pizzashop.service to org.junit.jupiter.api, org.junit.platform.commons; // Deschide pizzashop.service pentru JUnit

}