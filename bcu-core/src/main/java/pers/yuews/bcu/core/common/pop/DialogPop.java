package pers.yuews.bcu.core.common.pop;

import javax.swing.*;
import java.awt.*;

/**
 * @author yuews
 * @create 2024/5/21 14:21
 * @describe
 */
public class DialogPop extends JDialog {
    public DialogPop(String message,String title) {
        this.setVisible(true);
        this.setBounds(100, 100, 250, 250);
        this.setAlwaysOnTop(true);
        this.setTitle(title);
        final JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
        label.setForeground(Color.RED);
        this.add(label);
        this.setLocationRelativeTo(null);
    }
}
