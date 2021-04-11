package ru.kopp.ui.custom.services;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.net.MalformedURLException;
import java.net.URL;

public class TextFieldValidator implements DocumentListener {

    private final JTextField textField;

    public TextFieldValidator(JTextField textField) {
        this.textField = textField;
    }

    @Override
    public void insertUpdate(DocumentEvent event) {
        validateEmpty();
        validateUrl();
    }

    @Override
    public void removeUpdate(DocumentEvent event) {
        validateEmpty();
        validateUrl();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        validateEmpty();
        validateUrl();
    }

    private void validateEmpty() {
        if (textField.getText().length() == 0)
            textField.putClientProperty("JComponent.outline","error");
        else
            textField.putClientProperty("JComponent.outline",DesignGuide.lightBlue());
    }

    private void validateUrl(){
        try {
            new URL(textField.getText());
            textField.putClientProperty("JComponent.outline",DesignGuide.lightBlue());
        } catch (MalformedURLException e) {
            textField.putClientProperty("JComponent.outline","error");
        }
    }

    public void validate(){
        validateEmpty();
        validateUrl();
    }
}