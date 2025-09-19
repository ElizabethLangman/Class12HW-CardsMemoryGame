module com.example.cardsmemorygame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    requires javafx.media;

    opens com.example.cardsmemorygame to javafx.fxml;
    exports com.example.cardsmemorygame;
}