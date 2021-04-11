package ru.kopp.ui.custom.components.job;

import net.miginfocom.swing.MigLayout;
import ru.kopp.client.api.rest.FlinkResponse;
import ru.kopp.ui.custom.components.common.FlinkBasePanel;
import ru.kopp.ui.custom.services.DesignGuide;
import ru.kopp.ui.custom.services.ResourceService;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class JobExceptionsPane extends JDialog {

    public JobExceptionsPane(String titleMessage, FlinkResponse response) {
        //Appearance
        setLayout(new MigLayout("insets 0"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(Objects.requireNonNull(ResourceService.getImageIcon("squirrel-icon.png")));

        //Create Components
        FlinkBasePanel panel = new FlinkBasePanel();

        JLabel lbTitleMessage = new JLabel(titleMessage);
        JButton btnClose = new JButton("Close");
        JLabel lbExceptions = new JLabel("Exceptions:");
        JTextArea txtExceptions = new JTextArea(response.getDetailedMessage());
        JScrollPane scrExceptions = new JScrollPane(txtExceptions);

        //Components Appearance
        getRootPane().setDefaultButton(btnClose);

        lbTitleMessage.setFont(new Font("Tahoma", Font.BOLD, 20));
        lbTitleMessage.setForeground(DesignGuide.red());

        lbExceptions.setFont(new Font("Tahoma", Font.BOLD, 14));
        txtExceptions.setEditable(false);
        txtExceptions.setForeground(DesignGuide.red());
        txtExceptions.setBackground(DesignGuide.lightGray());
        scrExceptions.setBorder(BorderFactory.createLineBorder(DesignGuide.gray()));

        //Components Mapping
        panel.add(lbTitleMessage, "ay center, cell 0 0, wrap");
        panel.add(lbExceptions, "ax left, wrap");
        panel.add(scrExceptions, "push,grow,al left, w 500:500:n, h 300:300:n, wrap");
        panel.add(btnClose, "al center, w 50:100:100");
        add(panel, "push, grow");

        //Listeners
        btnClose.addActionListener(event -> dispose());

        //Show Dialog
        pack();
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setLocationRelativeTo(null);
        setVisible(true);
    }
}