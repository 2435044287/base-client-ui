package pers.yuews.bcu.demo.handler;

import pers.yuews.bcu.UIDemo;
import pers.yuews.bcu.core.common.annotaion.Autowired;
import pers.yuews.bcu.core.common.annotaion.Container;
import pers.yuews.bcu.core.common.annotaion.UIComponent;
import pers.yuews.bcu.core.common.handler.BuilderHandler;

import javax.swing.*;

/**
 * @author yuews
 * @create 2024/5/29 15:48
 * @describe
 */
@UIComponent( isFirst = true,text = "工号")
public class EmpLabelHandler extends BuilderHandler {

    @Container
    public JTextField empTextField = new JTextField(5);

    @Autowired(type = StarterButtonHandler.class)
    StarterButtonHandler starterButtonHandler;

    @Autowired(type = UIDemo.class)
    UIDemo ui;

    @Override
    protected BuilderHandler addComponent(BuilderHandler successor,UIComponent annotation ) throws InstantiationException, IllegalAccessException {
        JLabel empLabel = new JLabel(annotation.text(), SwingConstants.LEFT);
        //创建文本框
        empTextField.addActionListener(getListener());
        ui.getNavPanel().add(empLabel);
        ui.getNavPanel().add(empTextField);
        return starterButtonHandler;
    }

}
