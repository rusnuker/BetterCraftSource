// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.setup;

import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.io.File;
import java.awt.Container;
import javax.swing.Box;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.LayoutManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JFrame;
import java.awt.event.WindowListener;
import java.awt.event.ActionListener;

public class McLocationPrompt implements ActionListener, WindowListener
{
    private JFrame parent;
    private JFrame frame;
    private GridLayout layout;
    private JPanel mainPane;
    private JTextField locationField;
    private JButton btnLocate;
    private JButton btnBack;
    private JButton btnOk;
    private String action;
    
    public McLocationPrompt(final JFrame p, final String action) {
        this.parent = p;
        this.action = action;
        (this.frame = new JFrame("MCEF Setup - Minecraft location")).setMinimumSize(new Dimension(500, 1));
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(0);
        this.frame.addWindowListener(this);
        this.mainPane = new JPanel();
        this.layout = new GridLayout(3, 1, 3, 3);
        this.mainPane.setBorder(new EmptyBorder(3, 3, 3, 3));
        this.mainPane.setLayout(this.layout);
        this.mainPane.add(new JLabel("Please tell us where Minecraft is installed:"));
        JPanel line = new JPanel(new GridBagLayout());
        line.setMinimumSize(new Dimension(1, 250));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(0, 0, 0, 3);
        line.add(this.locationField = new JTextField(), c);
        c = new GridBagConstraints();
        c.fill = 3;
        c.gridx = 1;
        c.weighty = 1.0;
        line.add(this.btnLocate = new JButton("..."), c);
        this.btnLocate.addActionListener(this);
        this.mainPane.add(line);
        line = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = 2;
        c.weightx = 1.0;
        line.add(Box.createHorizontalGlue(), c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.insets = new Insets(0, 0, 0, 3);
        line.add(this.btnBack = new JButton("Back"), c);
        this.btnBack.addActionListener(this);
        c = new GridBagConstraints();
        c.gridx = 2;
        line.add(this.btnOk = new JButton("Ok"), c);
        this.btnOk.addActionListener(this);
        this.mainPane.add(line);
        try {
            this.locationField.setText(this.autoLocateMinecraft());
        }
        catch (final Throwable t) {
            System.err.println("Note: could not locate Minecraft:");
            t.printStackTrace();
        }
        this.frame.setContentPane(this.mainPane);
        this.frame.pack();
        this.parent.setVisible(false);
        this.frame.setVisible(true);
    }
    
    private String autoLocateMinecraft() {
        final File cDir = new File(".").getAbsoluteFile();
        if (cDir.getName().equals("mods")) {
            final File pFile = cDir.getParentFile();
            final File saves = new File(pFile, "saves");
            final File rpacks = new File(pFile, "resourcepacks");
            if (saves.exists() && saves.isDirectory() && rpacks.exists() && rpacks.isDirectory()) {
                return pFile.getAbsolutePath();
            }
        }
        File root = new File(System.getProperty("user.home", "."));
        final String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            root = new File(System.getenv("APPDATA"));
        }
        else if (os.contains("mac")) {
            root = new File(new File(root, "Library"), "Application Support");
        }
        root = new File(root, ".minecraft");
        return root.exists() ? root.getAbsolutePath() : "";
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.btnLocate) {
            final JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Where's Minecraft?");
            fc.setCurrentDirectory(new File(this.locationField.getText()));
            fc.setFileSelectionMode(1);
            if (fc.showOpenDialog(this.frame) == 0) {
                this.locationField.setText(fc.getSelectedFile().getAbsolutePath());
            }
        }
        else if (e.getSource() == this.btnBack) {
            this.parent.setVisible(true);
            this.frame.dispose();
        }
        else if (e.getSource() == this.btnOk) {
            final File loc = new File(this.locationField.getText());
            if (!loc.exists() || !loc.isDirectory()) {
                JOptionPane.showMessageDialog(this.frame, "The selected directory does not exist.", "Error", 0);
                return;
            }
            final File saves = new File(loc, "saves");
            final File rpacks = new File(loc, "resourcepacks");
            if ((!saves.exists() || !saves.isDirectory() || !rpacks.exists() || !rpacks.isDirectory()) && JOptionPane.showConfirmDialog(this.frame, "The selected directory does not look like a valid Minecraft setup...\nWould you like to continue?", "Hmmm...", 0) == 1) {
                return;
            }
            if (this.action.equals("configure")) {
                final File configDir = new File(loc, "config");
                if (!configDir.exists()) {
                    configDir.mkdirs();
                }
                new ConfigForm(this.parent, new File(configDir, "MCEF.cfg"));
                this.frame.dispose();
                return;
            }
            try {
                if (Processes.class.getMethod(this.action, JFrame.class, File.class).invoke(null, this.frame, loc)) {
                    this.parent.setVisible(true);
                    this.frame.dispose();
                }
            }
            catch (final Throwable t) {
                System.err.println("Could not execute action \"" + this.action + "\":");
                t.printStackTrace();
                JOptionPane.showMessageDialog(this.frame, "Could not execute action \"" + this.action + "\".\nThis shouldn't happen; please contact mod author.", "Error", 0);
            }
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
