package ru.kopp.ui.custom.components.common;

import net.miginfocom.swing.MigLayout;
import ru.kopp.client.api.rest.FlinkResponse;
import ru.kopp.ui.custom.services.DesignGuide;
import ru.kopp.ui.custom.services.ResourceService;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class FlinkErrorDialog extends JDialog {

    private final FlinkResponse response;

    public FlinkErrorDialog(String mainMessage, FlinkResponse response) {
        this.response = response;

        //Appearance
        setLayout(new MigLayout("insets 0"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(Objects.requireNonNull(ResourceService.getImageIcon("squirrel-icon.png")));
        setModal(true);

        //Create Components
        FlinkBasePanel panel = new FlinkBasePanel(new MigLayout("hidemode 3"));
        JLabel errorIcon = new JLabel();
        JLabel lbMainMessage = new JLabel(mainMessage);
        JButton btnClose = new JButton("Close");
        JLabel lbMessage = new JLabel("Message:");
        JTextArea txtMessage = new JTextArea(response.getShortMessage());
        JScrollPane scrMessage = new JScrollPane(txtMessage);
        JLabel lbExceptions = new JLabel("Exceptions:");
        JTextArea txtExceptions = new JTextArea(response.getDetailedMessage());
        JScrollPane scrExceptions = new JScrollPane(txtExceptions);

        //Components Appearance
        errorIcon.setIcon(new ImageIcon(getScaledErrorImage()));
        getRootPane().setDefaultButton(btnClose);

        lbMainMessage.setFont(new Font("Tahoma", Font.BOLD, 20));
        lbMainMessage.setForeground(DesignGuide.red());

        lbMessage.setFont(new Font("Tahoma", Font.BOLD, 14));
        txtMessage.setEditable(false);
        txtMessage.setForeground(DesignGuide.red());
        txtMessage.setBackground(DesignGuide.lightGray());
        scrMessage.setBorder(BorderFactory.createLineBorder(DesignGuide.gray()));

        lbExceptions.setFont(new Font("Tahoma", Font.BOLD, 14));
        txtExceptions.setEditable(false);
        txtExceptions.setForeground(DesignGuide.red());
        txtExceptions.setBackground(DesignGuide.lightGray());
        scrMessage.setBorder(BorderFactory.createLineBorder(DesignGuide.gray()));

        lbMessage.setVisible(hasHttpMessage());
        scrMessage.setVisible(hasHttpMessage());
        lbExceptions.setVisible(hasEntity());
        scrExceptions.setVisible(hasEntity());

        //Components Mapping
        panel.add(errorIcon, "ax left");
        panel.add(lbMainMessage, "ay center, cell 0 0, wrap, gap 20");
        panel.add(lbMessage, "ax left, wrap");
        panel.add(scrMessage, "push,grow,al left, w 500:500:n, wrap");
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

    private boolean hasEntity() {
        if (response.getDetailedMessage().equals("null")) return false;
        return !response.getDetailedMessage().equals("");
    }

    private boolean hasHttpMessage() {
        if (response.getShortMessage().equals("null")) return false;
        return !response.getShortMessage().equals("");
    }

    protected Image getScaledErrorImage() {
        final int w = 80;
        final int h = 55;
        final Image srcImg = ResourceService.getImageIcon("squirrel-lies.png");

        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImg.createGraphics();

        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(srcImg, 0, 0, w, h, null);
        graphics2D.dispose();

        return resizedImg;
    }
}