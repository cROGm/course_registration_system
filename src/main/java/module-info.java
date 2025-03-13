module com.crs {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires java.naming;
    requires java.sql;
    requires jakarta.transaction;
    requires org.slf4j;
    requires jakarta.cdi;
    requires org.hibernate.orm.core;


    opens com.crs to javafx.fxml;
    exports com.crs;

    // Make sure to open your model classes to Hibernate
    opens com.crs.model to org.hibernate.orm.core;
    // And your DAO classes if needed
    opens com.crs.dao to org.hibernate.orm.core;

    exports com.crs.controller;
    exports com.crs.model;

    opens com.crs.controller to javafx.fxml;
}