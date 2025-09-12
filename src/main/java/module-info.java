module com.example.cardsmemorygame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;

    opens com.example.cardsmemorygame to javafx.fxml;
    exports com.example.cardsmemorygame;
}