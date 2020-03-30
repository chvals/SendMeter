

import javax.swing.*;
import java.awt.event.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SendMeter extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfColdMeter1;
    private JLabel lbColdMeter1;
    private JLabel lbColdMeter2;
    private JLabel lbHotMeter1;
    private JLabel lbHotMeter2;
    private JTextField tfColdMeter2;
    private JTextField tfHotMeter1;
    private JTextField tfHotMeter2;

    public SendMeter() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        if ((tfColdMeter1.getText().isEmpty())
            | (tfColdMeter2.getText().isEmpty())
            | (tfHotMeter1.getText().isEmpty())
            | (tfHotMeter2.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Не заполнены показания всех приборов учета!",
                    "Внимание!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(null,
                "Отправить показания приборов учета?",
                "Пдтверждение...",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) == 1) {
            return;
        }
        // Send cold meter
        String strSubjCold = "показания приборов учета хол. воды л/с 802010909";
        String strBodyCold = "Чвало В.А.\n" +
                "л/с 802010909\n" +
                "Авиаторов пр-т, д.98, кв. 89\n" +
                "Текущие (на " +  date + ") показания  приборов учета хол. воды:\n" +
                "1) 9904606(м3): " + tfColdMeter1.getText() + "\n" +
                "2) 9909653(м3): " + tfColdMeter2.getText() + "\n" +
                "\n" +
                "Текущие (на " + date + ") показания  приборов учета гор. воды:\n" +
                "1) 9850557(м3): " + tfHotMeter1.getText() + "\n" +
                "2) 9850562(м3): " + tfHotMeter2.getText();
        SendMail("info.yaroslavl@yarobleirc.ru", strSubjCold, strBodyCold);
        // Send hot meter
        String strSubjHot = "показания приборов учета по л/с 202513360";
        String strBodyHot = "Чвало В.А.\n" +
                "л/с 202513360\n" +
                "Авиаторов пр-т, д.98, кв. 89\n" +
                "Текущие (на " + date + ") показания  приборов учета гор. воды:\n" +
                "1) 9850557(м3): " + tfHotMeter1.getText() + "\n" +
                "2) 9850562(м3): " + tfHotMeter2.getText();
        SendMail("zavupravdom@mail.ru", strSubjHot, strBodyHot);
        //
        JOptionPane.showMessageDialog(null, "Показания отправлены!");
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        SendMeter dialog = new SendMeter();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public void SendMail(String mailAdress, String MailSubj, String MailBody) {
        final String username = "vchvalo@list.ru";
        final String password = "Alcatel@12";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        //props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("vchvalo@list.ru"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mailAdress));
            message.setSubject(MailSubj);
            message.setText(MailBody);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
