
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Janela {

    JFrame frame = new JFrame();
    JLabel label = new JLabel("Hello");

    public Janela() {

        label.setBounds(0,0,100,50);
        label.setFont(new Font(null, Font.PLAIN, 25));

        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setVisible(true);

        frame.add(label);

    }

    

}