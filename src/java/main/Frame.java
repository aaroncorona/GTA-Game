package main;

import javax.swing.JFrame;

public class Frame extends JFrame {

    // Constructor
    Frame() {

        // Create panel
        Panel panel = new Panel();

        // Set frame details
        this.setTitle("GTA");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.add(panel);
        this.pack(); // to automatically size the main.Frame to the main.Panel settings
        this.setLayout(null);
        this.setLocationRelativeTo(null);

        // Start game loop
        panel.startGameThread();
    }
}

