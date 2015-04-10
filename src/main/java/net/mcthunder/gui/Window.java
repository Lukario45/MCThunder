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
        mainFrame = new JFrame();
        MCThunder = new JPanel();
        allPanels = new JTabbedPane();
        mainPanel = new JPanel();
        mainScrollPane = new JScrollPane();
        consolePane = new JTextPane();
        chatLabel = new JLabel();
        Chat = new JTextField();
        chatSendButton = new JButton();
        fileEditPanel = new JPanel();
        ipLabel = new JLabel();
        ipText = new JTextField();
        stopButton = new JButton();
        restartButton = new JButton();

        //======== mainFrame ========
        {
            mainFrame.setTitle("MCThunder");
            mainFrame.setResizable(false);
            Container mainFrameContentPane = mainFrame.getContentPane();

            //======== MCThunder ========
            {
                MCThunder.setMaximumSize(new Dimension(850, 500));

                // JFormDesigner evaluation mark
                MCThunder.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                        "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                        java.awt.Color.red), MCThunder.getBorder())); MCThunder.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

                //======== allPanels ========
                {
                    //======== mainPanel ========
                    {
                        //======== mainScrollPane ========
                        {
                            mainScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                            mainScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                            //---- consolePane ----
                            consolePane.setEditable(false);
                            mainScrollPane.setViewportView(consolePane);
                        }

                        //---- chatLabel ----
                        chatLabel.setText("Chat:");

                        //---- chatSendButton ----
                        chatSendButton.setText("Send");

                        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
                        mainPanel.setLayout(mainPanelLayout);
                        mainPanelLayout.setHorizontalGroup(
                            mainPanelLayout.createParallelGroup()
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                            .addComponent(chatLabel)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(Chat)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(chatSendButton))
                                        .addComponent(mainScrollPane, GroupLayout.PREFERRED_SIZE, 645, GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap(160, Short.MAX_VALUE))
                        );
                        mainPanelLayout.setVerticalGroup(
                            mainPanelLayout.createParallelGroup()
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addComponent(mainScrollPane, GroupLayout.PREFERRED_SIZE, 367, GroupLayout.PREFERRED_SIZE)
                                    .addGap(12, 12, 12)
                                    .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(chatLabel)
                                        .addComponent(chatSendButton)
                                        .addComponent(Chat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

                //---- ipText ----
                ipText.setEditable(false);

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
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(ipText, GroupLayout.PREFERRED_SIZE, 399, GroupLayout.PREFERRED_SIZE)
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
                                .addComponent(stopButton)
                                .addComponent(restartButton)
                                .addComponent(ipText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(allPanels, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                            .addContainerGap())
                );
            }

            GroupLayout mainFrameContentPaneLayout = new GroupLayout(mainFrameContentPane);
            mainFrameContentPane.setLayout(mainFrameContentPaneLayout);
            mainFrameContentPaneLayout.setHorizontalGroup(
                mainFrameContentPaneLayout.createParallelGroup()
                    .addGroup(mainFrameContentPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(MCThunder, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
            );
            mainFrameContentPaneLayout.setVerticalGroup(
                mainFrameContentPaneLayout.createParallelGroup()
                    .addGroup(mainFrameContentPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(MCThunder, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
            );
            mainFrame.pack();
            mainFrame.setLocationRelativeTo(mainFrame.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Connor Smith
    private JFrame mainFrame;
    private JPanel MCThunder;
    private JTabbedPane allPanels;
    private JPanel mainPanel;
    private JScrollPane mainScrollPane;
    private JTextPane consolePane;
    private JLabel chatLabel;
    private JTextField Chat;
    private JButton chatSendButton;
    private JPanel fileEditPanel;
    private JLabel ipLabel;
    private JTextField ipText;
    private JButton stopButton;
    private JButton restartButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    public JTextField getIpText(){
        return this.ipText;
    }

    public JTextPane getConsolePane(){
        return this.consolePane;
    }
    public void startGUI(){
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }
}