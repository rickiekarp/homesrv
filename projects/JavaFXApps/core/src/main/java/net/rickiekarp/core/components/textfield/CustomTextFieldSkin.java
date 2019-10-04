package net.rickiekarp.core.components.textfield;

import javafx.scene.control.TextField;
import javafx.scene.control.skin.TextFieldSkin;

public class CustomTextFieldSkin extends TextFieldSkin {

    public CustomTextFieldSkin(CustomTextField passwordField) {
        super(passwordField);
    }

    @Override protected String maskText(String txt) {
        TextField textField = getSkinnable();

        int n = textField.getLength();
        StringBuilder passwordBuilder = new StringBuilder(n);
        for (int i=0; i<n; i++) {
            char BULLET = '\u2022';
            passwordBuilder.append(BULLET);
        }

        return passwordBuilder.toString();
    }
}