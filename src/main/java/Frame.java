import javax.swing.JFrame;

public class Frame extends JFrame {

    // Create a Panel within the Frame
    Panel panel = new Panel();

    // Constructor
    Frame() {

        // Set frame details
        this.setTitle("GTA");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.add(panel);
        this.pack(); // to automatically size the Frame to the Panel settings
        this.setLayout(null);
        this.setLocationRelativeTo(null);

    }
}

