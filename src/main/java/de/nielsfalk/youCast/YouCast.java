package de.nielsfalk.youCast;

import com.apple.eawt.Application;
import de.nielsfalk.youCast.heroku.Main;
import org.eclipse.jetty.server.Server;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * @author Niels Falk
 */
public class YouCast {
    public static void main(String[] args) throws Exception {
        URL envelopeImage = YouCast.class.getResource("/youCast.png");

        JFrame youCast = new JFrame("youCast");
        if (envelopeImage != null) {
            Application.getApplication().setDockIconImage(Toolkit.getDefaultToolkit().getImage(envelopeImage));
        }
        int port = 8521;
        try {
            Server server = Main.startServer(port);
            youCast.setTitle("youCast started");
            youCast.setSize(new Dimension(500, 40));
            youCast.setVisible(true);
            server.join();
        } catch (Exception e) {
            JFrame error = new JFrame("tried to start localhost:"+port+" - "+e.getMessage());
            error.setSize(new Dimension(500, 40));
            error.setVisible(true);
        }
    }
}
