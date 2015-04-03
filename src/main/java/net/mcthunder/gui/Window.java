/*
 * Created by JFormDesigner on Fri Apr 03 03:24:14 EDT 2015
 */

package net.mcthunder.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author unknown
 */
public class Window extends JFrame {
    public Window() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Connor Smith
        frame1 = new JFrame();
        MCThunder = new JPanel();
        allPanels = new JTabbedPane();
        mainPanel = new JPanel();
        fileEditPanel = new JPanel();
        ipLabel = new JLabel();
        ipText = new JTextField();
        stopButton = new JButton();
        restartButton = new JButton();

        //======== frame1 ========
        {
            frame1.setTitle("MCThunder");
            frame1.setResizable(false);
            frame1.setVisible(true);
            frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Container frame1ContentPane = frame1.getContentPane();

            //======== MCThunder ========
            {
                MCThunder.setMaximumSize(new Dimension(850, 500));

                /* JFormDesigner evaluation mark
                MCThunder.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                        "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                        java.awt.Color.red), MCThunder.getBorder())); MCThunder.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});
*/

                //======== allPanels ========
                {

                    //======== mainPanel ========
                    {

                        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
                        mainPanel.setLayout(mainPanelLayout);
                        mainPanelLayout.setHorizontalGroup(
                            mainPanelLayout.createParallelGroup()
                                .addGap(0, 811, Short.MAX_VALUE)
                        );
                        mainPanelLayout.setVerticalGroup(
                            mainPanelLayout.createParallelGroup()
                                .addGap(0, 410, Short.MAX_VALUE)
                        );
                    }
                    allPanels.addTab("Main", mainPanel);

                    //======== fileEditPanel ========
                    {

                        GroupLayout fileEditPanelLayout = new GroupLayout(fileEditPanel);
                        fileEditPanel.setLayout(fileEditPanelLayout);
                        fileEditPanelLayout.setHorizontalGroup(
                            fileEditPanelLayout.createParallelGroup()
                                .addGap(0, 811, Short.MAX_VALUE)
                        );
                        fileEditPanelLayout.setVerticalGroup(
                            fileEditPanelLayout.createParallelGroup()
                                .addGap(0, 410, Short.MAX_VALUE)
                        );
                    }
                    allPanels.addTab("File Edit", fileEditPanel);
                }

                //---- ipLabel ----
                ipLabel.setText("Connect IP:");

                //---- stopButton ----
                stopButton.setText("Stop");

                //---- restartButton ----
                restartButton.setText("Restart");

                GroupLayout MCThunderLayout = new GroupLayout(MCThunder);
                MCThunder.setLayout(MCThunderLayout);
                MCThunderLayout.setHorizontalGroup(
                    MCThunderLayout.createParallelGroup()
                        .addGroup(MCThunderLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(MCThunderLayout.createParallelGroup()
                                .addComponent(allPanels)
                                .addGroup(MCThunderLayout.createSequentialGroup()
                                    .addComponent(ipLabel)
                                    .addGap(12, 12, 12)
                                    .addComponent(ipText, GroupLayout.PREFERRED_SIZE, 393, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(stopButton)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(restartButton)
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                );
                MCThunderLayout.setVerticalGroup(
                    MCThunderLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, MCThunderLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(MCThunderLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(ipLabel)
                                .addComponent(ipText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(stopButton)
                                .addComponent(restartButton))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(allPanels)
                            .addContainerGap())
                );
            }

            GroupLayout frame1ContentPaneLayout = new GroupLayout(frame1ContentPane);
            frame1ContentPane.setLayout(frame1ContentPaneLayout);
            frame1ContentPaneLayout.setHorizontalGroup(
                frame1ContentPaneLayout.createParallelGroup()
                    .addGroup(frame1ContentPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(MCThunder, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
            );
            frame1ContentPaneLayout.setVerticalGroup(
                frame1ContentPaneLayout.createParallelGroup()
                    .addGroup(frame1ContentPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(MCThunder, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
            );
            frame1.pack();
            frame1.setLocationRelativeTo(frame1.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Connor Smith
    private JFrame frame1;
    private JPanel MCThunder;
    private JTabbedPane allPanels;
    private JPanel mainPanel;
    private JPanel fileEditPanel;
    private JLabel ipLabel;
    private JTextField ipText;
    private JButton stopButton;
    private JButton restartButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    public JTextField getIpText(){
        return this.ipText;
    }
    public void setIpText(JTextField text){
        this.ipText = text;
    }
}
