module org.example.studentmanagementsystem {

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.controls;


    opens org.example.studentmanagementsystem to javafx.base;
    exports org.example.studentmanagementsystem;
}