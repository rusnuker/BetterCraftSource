// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.setup;

import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.Container;
import javax.swing.Box;
import java.awt.GridBagConstraints;
import javax.swing.border.TitledBorder;
import java.awt.LayoutManager;
import java.awt.GridBagLayout;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.event.WindowListener;
import java.awt.event.ActionListener;

public class ConfigForm implements ActionListener, WindowListener
{
    private CfgParser config;
    private JFrame parent;
    private JFrame frame;
    private JPanel contentPane;
    private JPanel cMainPane;
    private JTextField cMainForcedMirror;
    private JCheckBox cMainSkipUpdates;
    private JCheckBox cMainForgeSplash;
    private JCheckBox cMainWarnUpdates;
    private JPanel cBrowserPane;
    private JCheckBox cBrowserEnable;
    private JTextField cBrowserHome;
    private JPanel btnPane;
    private JButton btnOk;
    private JButton btnBack;
    private JButton btnApply;
    
    public ConfigForm(final JFrame p, final File cfgFile) {
        this.config = new CfgParser(cfgFile);
        this.parent = p;
        this.config.load();
        (this.frame = new JFrame("MCEF Setup - Configuration")).setMinimumSize(new Dimension(500, 1));
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(0);
        this.frame.addWindowListener(this);
        (this.contentPane = new JPanel()).setBorder(new EmptyBorder(3, 3, 3, 3));
        this.contentPane.setLayout(new GridBagLayout());
        (this.cMainPane = new JPanel()).setBorder(new TitledBorder("Main"));
        this.cMainPane.setLayout(new GridBagLayout());
        this.cMainForcedMirror = new JTextField();
        this.cMainSkipUpdates = new JCheckBox();
        this.cMainForgeSplash = new JCheckBox();
        this.cMainWarnUpdates = new JCheckBox();
        this.addFormComponent(this.cMainPane, 0, "Forced mirror", this.cMainForcedMirror);
        this.addFormComponent(this.cMainPane, 1, "Skip updates", this.cMainSkipUpdates);
        this.addFormComponent(this.cMainPane, 2, "Use forge splash", this.cMainForgeSplash);
        this.addFormComponent(this.cMainPane, 3, "Warn updates", this.cMainWarnUpdates);
        this.cMainForcedMirror.setText(this.config.getStringValue("main", "forcedMirror", ""));
        this.cMainSkipUpdates.setSelected(this.config.getBooleanValue("main", "skipUpdates", false));
        this.cMainForgeSplash.setSelected(this.config.getBooleanValue("main", "useForgeSplash", true));
        this.cMainWarnUpdates.setSelected(this.config.getBooleanValue("main", "warnUpdates", true));
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 4;
        c.fill = 3;
        c.weighty = 1.0;
        this.cMainPane.add(Box.createVerticalGlue(), c);
        c = new GridBagConstraints();
        c.fill = 1;
        c.weightx = 1.0;
        c.weighty = 0.5;
        this.contentPane.add(this.cMainPane, c);
        (this.cBrowserPane = new JPanel()).setBorder(new TitledBorder("Browser"));
        this.cBrowserPane.setLayout(new GridBagLayout());
        this.cBrowserEnable = new JCheckBox();
        this.cBrowserHome = new JTextField();
        this.addFormComponent(this.cBrowserPane, 0, "Enable", this.cBrowserEnable);
        this.addFormComponent(this.cBrowserPane, 1, "Home page", this.cBrowserHome);
        this.cBrowserEnable.setSelected(this.config.getBooleanValue("examplebrowser", "enable", true));
        this.cBrowserHome.setText(this.config.getStringValue("examplebrowser", "home", "mod://mcef/home.html"));
        c = new GridBagConstraints();
        c.gridy = 2;
        c.fill = 3;
        c.weighty = 1.0;
        this.cBrowserPane.add(Box.createVerticalGlue(), c);
        c = new GridBagConstraints();
        c.gridy = 1;
        c.fill = 1;
        c.weightx = 1.0;
        c.weighty = 0.5;
        this.contentPane.add(this.cBrowserPane, c);
        (this.btnPane = new JPanel()).setLayout(new GridBagLayout());
        this.btnOk = new JButton("Ok");
        this.btnBack = new JButton("Back");
        this.btnApply = new JButton("Apply");
        this.btnOk.addActionListener(this);
        this.btnBack.addActionListener(this);
        this.btnApply.addActionListener(this);
        c = new GridBagConstraints();
        c.fill = 2;
        c.weightx = 1.0;
        this.btnPane.add(Box.createHorizontalGlue(), c);
        this.addFormButton(1, this.btnOk);
        this.addFormButton(2, this.btnBack);
        this.addFormButton(3, this.btnApply);
        c = new GridBagConstraints();
        c.gridy = 2;
        c.fill = 2;
        c.weightx = 1.0;
        this.contentPane.add(this.btnPane, c);
        this.frame.setContentPane(this.contentPane);
        this.frame.pack();
        this.parent.setVisible(false);
        this.frame.setVisible(true);
    }
    
    private void addFormComponent(final JPanel pane, final int line, final String label, final Component comp) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 3, 3, 3);
        c.fill = 2;
        c.gridy = line;
        pane.add(new JLabel(label), c);
        c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 3, 3);
        c.gridx = 1;
        c.gridy = line;
        c.fill = 2;
        c.weightx = 1.0;
        pane.add(comp, c);
    }
    
    private void addFormButton(final int x, final JButton btn) {
        final GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 0, 3, 3);
        c.gridx = x;
        this.btnPane.add(btn, c);
    }
    
    private void saveChanges() {
        this.config.setStringValue("main", "forcedMirror", this.cMainForcedMirror.getText());
        this.config.setBooleanValue("main", "skipUpdates", this.cMainSkipUpdates.isSelected());
        this.config.setBooleanValue("main", "useForgeSplash", this.cMainForgeSplash.isSelected());
        this.config.setBooleanValue("main", "warnUpdates", this.cMainWarnUpdates.isSelected());
        this.config.setBooleanValue("examplebrowser", "enable", this.cBrowserEnable.isSelected());
        this.config.setStringValue("examplebrowser", "home", this.cBrowserHome.getText());
        if (!this.config.save()) {
            JOptionPane.showMessageDialog(this.frame, "Could not save configuration file.\nMake sure you have the permissions to write in the config folder.", "Error", 0);
        }
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.btnBack) {
            this.windowClosing(null);
        }
        else if (e.getSource() == this.btnApply) {
            this.saveChanges();
        }
        else if (e.getSource() == this.btnOk) {
            this.saveChanges();
            this.windowClosing(null);
        }
    }
    
    @Override
    public void windowOpened(final WindowEvent e) {
    }
    
    @Override
    public void windowClosing(final WindowEvent e) {
        this.parent.setVisible(true);
        this.frame.dispose();
    }
    
    @Override
    public void windowClosed(final WindowEvent e) {
    }
    
    @Override
    public void windowIconified(final WindowEvent e) {
    }
    
    @Override
    public void windowDeiconified(final WindowEvent e) {
    }
    
    @Override
    public void windowActivated(final WindowEvent e) {
    }
    
    @Override
    public void windowDeactivated(final WindowEvent e) {
    }
}
