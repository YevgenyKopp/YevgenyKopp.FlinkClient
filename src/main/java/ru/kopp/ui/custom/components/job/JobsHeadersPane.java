package ru.kopp.ui.custom.components.job;

import net.miginfocom.swing.MigLayout;
import ru.kopp.ui.custom.components.common.FlinkHeadersPane;
import ru.kopp.ui.custom.graphics.BottomSeparatorBorder;
import ru.kopp.ui.custom.services.DesignGuide;

import javax.swing.*;
import java.awt.*;

public class JobsHeadersPane extends FlinkHeadersPane {
    public JobsHeadersPane() {
        //Appearance
        setLayout(new MigLayout());
        setBorder(new BottomSeparatorBorder(DesignGuide.gray()));

        //Create Components
        JLabel lbName=new JLabel("Job Name");
        JLabel lbStartTime=new JLabel("Start Time");
        JLabel lbDuration=new JLabel("Duration");
        JLabel lbEndTime=new JLabel("End Time");
        JLabel lbTasks=new JLabel("Tasks");
        JLabel lbStatus=new JLabel("Status");
        JLabel lbEmptyLabel=new JLabel("");

        //SetFont
        Font headersFont=new Font("Tahoma",Font.BOLD,12);
        lbName.setFont(headersFont);
        lbStartTime.setFont(headersFont);
        lbDuration.setFont(headersFont);
        lbEndTime.setFont(headersFont);
        lbTasks.setFont(headersFont);
        lbStatus.setFont(headersFont);

        //Components Mapping
        add(lbName, "pushx,growx, al left, w 50:150:500");
        add(lbStartTime, "pushx,growx, al left, w 50:150:500");
        add(lbDuration, "pushx,growx, al left, w 50:150:500");
        add(lbEndTime, "pushx,growx,al left, w 50:150:500");
        add(lbTasks, "al left, pushx, w 50:150:500");
        add(lbStatus, "al left, pushx, w 50:150:500");
        add(lbEmptyLabel, "w 50:100:100");
    }
}