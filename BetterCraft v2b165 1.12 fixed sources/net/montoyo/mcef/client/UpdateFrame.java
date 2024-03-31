// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.client;

import net.montoyo.mcef.utilities.Util;
import net.montoyo.mcef.utilities.Log;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import javax.swing.Box;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import net.montoyo.mcef.utilities.IProgressListener;
import javax.swing.JFrame;

public class UpdateFrame extends JFrame implements IProgressListener
{
    private JLabel label;
    private JProgressBar pbar;
    
    public UpdateFrame() {
        this.label = new JLabel("Preparing...");
        this.pbar = new JProgressBar();
        this.setTitle("Minecraft ChromiumEF");
        this.setDefaultCloseOperation(0);
        this.setLocationRelativeTo(null);
        final JPanel lpane = new JPanel();
        lpane.setLayout(new BoxLayout(lpane, 2));
        lpane.add(this.label);
        lpane.add(Box.createHorizontalGlue());
        Dimension dim = new Dimension(5, 5);
        final JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pane.setLayout(new BoxLayout(pane, 3));
        pane.add(lpane);
        pane.add(new Box.Filler(dim, dim, dim));
        pane.add(this.pbar);
        this.setContentPane(pane);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (final Throwable t) {
            Log.info("Note: couldn't set system look & feel.", new Object[0]);
        }
        this.setVisible(true);
        dim = new Dimension(50, 26);
        this.pbar.setMinimumSize(dim);
        this.pbar.setPreferredSize(dim);
        this.setMinimumSize(new Dimension(540, 90));
        this.pack();
    }
    
    @Override
    public void onProgressed(final double d) {
        final int val = (int)Util.clamp(d, 0.0, 100.0);
        this.pbar.setValue(val);
    }
    
    @Override
    public void onTaskChanged(final String name) {
        Log.info("Task changed to \"%s\"", name);
        this.label.setText(name);
    }
    
    @Override
    public void onProgressEnd() {
        this.dispose();
    }
}
