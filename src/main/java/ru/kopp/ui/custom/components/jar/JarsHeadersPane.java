package ru.kopp.ui.custom.components.jar;

import ru.kopp.ui.custom.components.common.FlinkHeadersPane;

import javax.swing.*;
import java.awt.*;

public class JarsHeadersPane extends FlinkHeadersPane {
    public JarsHeadersPane() {
        //Create Components
        JLabel lbName=new JLabel("Name");
        JLabel lbUpload=new JLabel("Upload Time");
        JLabel lbEntry=new JLabel("Entry Class");
        JLabel lbEmptyLabel=new JLabel();

        //SetFont
        Font headersFont=new Font("Tahoma",Font.BOLD,12);
        lbName.setFont(headersFont);
        lbUpload.setFont(headersFont);
        lbEntry.setFont(headersFont);

        //Components Mapping
        add(lbName,"pushx,growx,al left,w 50:150:500, gap 5");
        add(lbUpload,"pushx,growx,al left,w 50:200:550");
        add(lbEntry,"pushx,growx,al left,w 50:400:1000");
        add(lbEmptyLabel, "w 50:100:100, gap n 5");
    }
}