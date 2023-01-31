module LibretaDirecciones {
	requires javafx.controls;
	requires java.logging;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;
	requires java.prefs;
	requires java.xml;
	requires java.desktop;
	requires org.apache.pdfbox;
	requires java.sql;
    requires jasperreports;


    opens controller to javafx.graphics, javafx.fxml;
	opens view to javafx.graphics, javafx.fxml;
	opens model to javafx.graphics, javafx.fxml, javafx.base;
}
