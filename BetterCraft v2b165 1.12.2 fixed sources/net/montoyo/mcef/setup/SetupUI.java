// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.setup;

import java.net.URI;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;
import java.io.FileOutputStream;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JFrame;
import java.io.File;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.awt.event.ActionListener;

public class SetupUI implements ActionListener, WindowListener, MouseListener
{
    public static SetupUI INSTANCE;
    private File selfDestruct;
    private JFrame frame;
    private GridLayout layout;
    private JPanel mainPane;
    private JButton btnInstall;
    private JButton btnConfigure;
    private JButton btnUninstall;
    private JButton btnExit;
    private JLabel aboutLabel;
    
    static {
        SetupUI.INSTANCE = null;
    }
    
    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (final Throwable t) {
            t.printStackTrace();
        }
        SetupUI.INSTANCE = new SetupUI();
    }
    
    public SetupUI() {
        this.selfDestruct = null;
        (this.frame = new JFrame("MCEF Setup")).setMinimumSize(new Dimension(300, 100));
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(0);
        this.frame.addWindowListener(this);
        this.btnInstall = new JButton("Install");
        this.btnConfigure = new JButton("Configure");
        this.btnUninstall = new JButton("Uninstall");
        this.btnExit = new JButton("Exit");
        this.btnInstall.addActionListener(this);
        this.btnConfigure.addActionListener(this);
        this.btnUninstall.addActionListener(this);
        this.btnExit.addActionListener(this);
        final JPanel labelPane = new JPanel();
        labelPane.setLayout(new BoxLayout(labelPane, 3));
        labelPane.add(new JLabel("Welcome to the MCEF Setup Wizard."));
        labelPane.add(new JLabel("What do you like to do?"));
        this.mainPane = new JPanel();
        this.layout = new GridLayout(6, 1, 3, 3);
        this.mainPane.setBorder(new EmptyBorder(3, 3, 3, 3));
        this.mainPane.setLayout(this.layout);
        this.mainPane.add(labelPane);
        this.mainPane.add(this.btnInstall);
        this.mainPane.add(this.btnConfigure);
        this.mainPane.add(this.btnUninstall);
        this.mainPane.add(this.btnExit);
        (this.aboutLabel = new JLabel("<html><i>MCEF was written by <u><font color=\"#000099\">montoyo</font></u></i>&nbsp;&nbsp;</html>")).setHorizontalAlignment(4);
        this.aboutLabel.addMouseListener(this);
        this.mainPane.add(this.aboutLabel);
        this.frame.setContentPane(this.mainPane);
        this.frame.pack();
        this.frame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.btnExit) {
            this.windowClosing(null);
        }
        else if (e.getSource() == this.btnInstall) {
            new McLocationPrompt(this.frame, "install");
        }
        else if (e.getSource() == this.btnConfigure) {
            new McLocationPrompt(this.frame, "configure");
        }
        else if (e.getSource() == this.btnUninstall) {
            new McLocationPrompt(this.frame, "uninstall");
        }
    }
    
    void initiateSelfDestruct(final File f) {
        this.selfDestruct = f;
    }
    
    void abortSelfDestruct() {
        this.selfDestruct = null;
    }
    
    private void runSelfDestructionUnsafe() throws Throwable {
        final File tmp = File.createTempFile("mcef-deleter", ".jar");
        final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tmp));
        final InputStream is = SetupUI.class.getResourceAsStream("/net/montoyo/mcef/setup/Deleter.class");
        final byte[] buf = new byte[8192];
        zos.putNextEntry(new ZipEntry("net/montoyo/mcef/setup/Deleter.class"));
        int read;
        while ((read = is.read(buf)) > 0) {
            zos.write(buf, 0, read);
        }
        try {
            zos.closeEntry();
        }
        catch (final Throwable t) {}
        SetupUtil.silentClose(zos);
        SetupUtil.silentClose(is);
        String java = "\"" + System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            java = String.valueOf(java) + "w.exe";
        }
        java = String.valueOf(java) + "\" -classpath \"";
        java = String.valueOf(java) + tmp.getAbsolutePath();
        java = String.valueOf(java) + "\" net.montoyo.mcef.setup.Deleter \"";
        java = String.valueOf(java) + this.selfDestruct.getAbsolutePath();
        java = String.valueOf(java) + "\"";
        System.out.println("Running auto-deleter:");
        System.out.println(java);
        Runtime.getRuntime().exec(java);
    }
    
    @Override
    public void windowOpened(final WindowEvent e) {
    }
    
    @Override
    public void windowClosing(final WindowEvent e) {
        this.frame.dispose();
        if (this.selfDestruct != null) {
            try {
                this.runSelfDestructionUnsafe();
            }
            catch (final Throwable t) {
                System.err.println("Failed to destruct myself:");
                t.printStackTrace();
            }
        }
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
    
    @Override
    public void mouseClicked(final MouseEvent e) {
        try {
            Desktop.getDesktop().browse(new URI("https://montoyo.net"));
        }
        catch (final Throwable t) {
            t.printStackTrace();
        }
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
    }
    
    @Override
    public void mouseReleased(final MouseEvent e) {
    }
    
    @Override
    public void mouseEntered(final MouseEvent e) {
    }
    
    @Override
    public void mouseExited(final MouseEvent e) {
    }
}
