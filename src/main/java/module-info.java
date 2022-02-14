module HomeBudget {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires com.jfoenix;
    requires ormlite.core;
    requires org.xerial.sqlitejdbc;
    requires ormlite.jdbc;
    requires java.sql;

    exports pl.patrickit to javafx.graphics;
    exports pl.patrickit.database.models to ormlite.core;
    opens pl.patrickit.database.models to ormlite.core;
    opens pl.patrickit.controllers to javafx.fxml;

}