// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.altmanager.impl;

import javax.swing.JOptionPane;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class AccountImport extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    public JButton openButton;
    private JFileChooser fc;
    
    public AccountImport() {
        (this.fc = new JFileChooser()).setFileFilter(new FileNameExtensionFilter("Text Files", new String[] { "txt" }));
        (this.openButton = new JButton("Open a File...")).addActionListener(this);
        this.add(this.openButton);
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.openButton) {
            final int returnVal = this.fc.showOpenDialog(this);
            if (returnVal == 0) {
                try {
                    Throwable t = null;
                    try {
                        final BufferedReader bufferedReader = new BufferedReader(new FileReader(this.fc.getSelectedFile()));
                        try {
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                final String[] arguments = line.split(":");
                                for (int i = 0; i < 2; ++i) {
                                    arguments[i].replace(" ", "");
                                }
                                AccountManager.getInstance().getAccounts().add(new Account(arguments[0], arguments[1], ""));
                            }
                            AccountManager.getInstance().save();
                        }
                        finally {
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                        }
                    }
                    finally {
                        if (t == null) {
                            final Throwable t2;
                            t = t2;
                        }
                        else {
                            final Throwable t2;
                            if (t != t2) {
                                t.addSuppressed(t2);
                            }
                        }
                    }
                }
                catch (final Exception ex) {
                    JOptionPane.showMessageDialog(null, "An error happened.", "ERROR", 1);
                }
            }
        }
    }
}
