package services;

import javafx.scene.control.Button;

public abstract class CreateElementService {

    public static Button createButton(String buttonText){
        Button button = new Button();
        button.setText(buttonText);
        button.setOnAction(event -> {

        });
        return button;
    }
}
