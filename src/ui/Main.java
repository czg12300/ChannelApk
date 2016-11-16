
package ui;


import java.awt.EventQueue;

import javax.swing.JFrame;

public class Main extends JFrame {

    public static void main(String[] args) {  
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame window = new MainFrame();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }  
      
   
}
