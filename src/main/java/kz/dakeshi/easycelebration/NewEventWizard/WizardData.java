package kz.dakeshi.easycelebration.NewEventWizard;

import com.vaadin.ui.Embedded;
import javax.swing.*;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

public class WizardData {

    public static String eventType = "";
    public static String initiator = "";
    public static String initiator2 = "";
    public static Date date = new Date();
    public static Date time = new Date(64800000);
    public static String place = "";
    public static String address = "";
    public static String note = "";
    public static Embedded lastSelectedImage = null;
    public static String imagePath = "";
    public static String imageWidth;
    public static String imageHeight;
    public static ArrayList<Guest> guestsList = new ArrayList<>();
    public static ArrayList<Present> presentsList = new ArrayList<>();
    public static int lastImageNumber;
    public static String urlPath;
    public static String invitatationText = "<div style=\"background-image:url(imagePathUrl); background-size: cover; background-repeat:no-repeat; width: mailimgwpx; height: mailimghpx;\">\n" +
            "<div style=\"padding-top: 20px; padding-left: 20px;\">\n" +
            "<p>Уважаемый(ая) ИМЯ ОТЧЕСТВО!</p>\n" +
            "<p>\n" +
            "Приглашаю Вас принять участие в праздновании" +
            "<br>\n" +
            "моего СОБЫТИЕ, которое состоится ДАТА\n" +
            "<br>\n" +
            "в ВРЕМЯ в МЕСТО\n" +
            "<br>\n" +
            " по адресу: АДРЕС.</p>\n" +
            "<br>\n" +
            "<p>ПРИМЕЧАНИЕ</p>\n" +
            "</div>\n" +
            "</div>";

    public static boolean isTextInitialized = false;

    public  static void wdclean() {
        eventType = "";
        initiator = "";
        initiator2 = "";
        date = new Date();
        time.setTime(64800000);
        place = "";
        address = "";
        note = "";
        imagePath = "";
        guestsList.clear();
        presentsList.clear();
        lastSelectedImage = null;
        lastImageNumber = -1;
        invitatationText = "<div style=\"background-image:url(imagePathUrl); background-size: cover; background-repeat:no-repeat; width: mailimgwpx; height: mailimghpx;\">\n" +
                "<div style=\"padding-top: 20px; padding-left: 20px;\">\n" +
                "<p>Уважаемый(ая) ИМЯ ОТЧЕСТВО!</p>\n" +
                "<p>\n" +
                "Приглашаю Вас принять участие в праздновании" +
                "<br>\n" +
                "моего СОБЫТИЕ, которое состоится ДАТА\n" +
                "<br>\n" +
                "в ВРЕМЯ в МЕСТО\n" +
                "<br>\n" +
                " по адресу: АДРЕС.</p>\n" +
                "<br>\n" +
                "<p>ПРИМЕЧАНИЕ</p>\n" +
                "</div>\n" +
                "</div>";
        urlPath = "";
        imageHeight = "";
        imageWidth = "";
        isTextInitialized = false;
    }

    public static void dbSave() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        ResultSet rs = null;
        Connection connection = null;

        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:src\\Clb.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select id from EventTypeSpr where event_type ='"+eventType+"'");
            int eventTypeId = rs.getInt("id");

            java.sql.Date eventDateSQL = new java.sql.Date(date.getTime());
            java.sql.Time eventTimeSQL = new java.sql.Time(time.getTime());

            statement.executeUpdate("insert into Events (event_type_id, initiator, event_date, event_time, place, address, note, invitation_text) "+
            "values ('"+eventTypeId+"', '"+initiator+"', '"+eventDateSQL+"', '"+eventTimeSQL+"', '"+place+"', '"+address+"', '"+note+"', '"+invitatationText+"')");

            rs = statement.executeQuery("select id from Events where event_type_id = '"+eventTypeId+"' and initiator = '"+initiator+"' "+
            "and event_date = '"+eventDateSQL+"' and event_time = '"+eventTimeSQL+"' and place = '"+place+"' and address = '"+address+"'");
            int eventId = rs.getInt("id");

            if (!guestsList.isEmpty()){
                for (Guest guest2: WizardData.guestsList){
                    statement.executeUpdate("insert into Guests (last_name, first_name, email, phone, event_id, user_name, password, invitation_sent_ok) values "+
                    "('"+guest2.getLastName()+"', '"+guest2.getFirstName()+"', '"+guest2.getEmail()+"', '"+guest2.getPhone()+"', '"+eventId+"', '"+guest2.getUn()+"', '"+guest2.getPw()+"', '"+guest2.isInvitationIsSent()+"')");
                }
            }

            if (!presentsList.isEmpty()){
                for (Present present2: presentsList){
                    statement.executeUpdate("insert into Presents (present, event_id, note) values "+
                            "('"+present2.getPresentName()+"', '"+eventId+"', '"+present2.getNote()+"')");
                }
            }
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e);
            }
        }
    }

    public static void invitationTextProcess (){
        ArrayList<String> imagesUrls = new ArrayList<String>();
        Collections.addAll(imagesUrls,"http://s020.radikal.ru/i700/1702/ff/467ee91fb5f4.jpg", "http://s014.radikal.ru/i327/1702/92/fc524b8e60ad.jpg", "http://s019.radikal.ru/i608/1702/55/414d325a903f.jpg",
                "http://s008.radikal.ru/i306/1702/07/36673a7bc8cc.png","http://s41.radikal.ru/i094/1702/43/cbbd007ec5c4.jpg","http://s008.radikal.ru/i303/1702/90/c9fb2a49ca66.jpg",
                "http://s002.radikal.ru/i198/1702/0b/8d6a0d31d92d.jpg","http://s56.radikal.ru/i154/1702/f7/97915696ee27.jpg","http://s019.radikal.ru/i601/1702/e4/057afdba3784.jpg",
                "http://s16.radikal.ru/i190/1702/c2/b3acd257fa85.jpg","http://s020.radikal.ru/i704/1702/3d/93b59a88824a.jpg","http://s56.radikal.ru/i154/1702/56/b760b950ac9d.jpg",
                "http://s020.radikal.ru/i701/1702/5b/9640167fb298.jpg","http://s019.radikal.ru/i620/1702/4c/9be1faf22a15.gif","http://s020.radikal.ru/i721/1702/b2/9c0c8273e40b.jpg",
                "http://s020.radikal.ru/i706/1702/49/dce291a057f1.jpg","http://s019.radikal.ru/i635/1702/f2/abcaa27621f3.jpg","http://s018.radikal.ru/i513/1702/97/5d3c9936616e.jpg",
                "http://i079.radikal.ru/1702/3c/8587a090f833.jpg","http://s009.radikal.ru/i308/1702/a6/ca12ffd7d7a3.jpg","http://s019.radikal.ru/i618/1702/42/c1befc2a5ce0.png",
                "http://s018.radikal.ru/i505/1702/82/8482f077f5ca.jpg","http://s020.radikal.ru/i713/1702/3d/23103a3b101d.jpg","http://s012.radikal.ru/i319/1702/52/b0f89ba3d1dc.jpg",
                "http://s020.radikal.ru/i700/1702/55/a88b8a04774f.jpg","http://s019.radikal.ru/i613/1702/7f/7310a5f88880.png","http://s019.radikal.ru/i631/1702/ba/987e8d683c98.jpg",
                "http://s019.radikal.ru/i634/1702/c6/ec3bbfd851d4.jpg","http://s019.radikal.ru/i618/1702/aa/223cfb50cc45.jpg","http://s020.radikal.ru/i712/1702/d1/81c09328bf52.jpg",
                "http://s50.radikal.ru/i129/1702/71/bd89e40326b4.jpg","http://s018.radikal.ru/i500/1702/ee/4fbe351686c8.jpg","http://s019.radikal.ru/i638/1702/3d/c16377618c25.jpg",
                "http://s018.radikal.ru/i521/1702/6d/39dd953bd927.jpg","http://s018.radikal.ru/i505/1702/ba/621329fb1cef.jpg","http://s018.radikal.ru/i518/1702/69/fa6eaa9f1ec0.jpg",
                "http://s06.radikal.ru/i179/1702/b4/71eeda9543e2.png","http://s009.radikal.ru/i310/1702/7a/e899b39b7308.jpg","http://s020.radikal.ru/i715/1702/9a/088beabf8f63.jpg",
                "http://s008.radikal.ru/i306/1702/39/b80686b0f0f6.jpg","http://s019.radikal.ru/i628/1702/13/fd1b8d127499.jpg","http://s011.radikal.ru/i318/1702/c8/861138d69c14.jpg",
                "http://s020.radikal.ru/i723/1702/b3/78b6f25902fb.jpg","http://s13.radikal.ru/i186/1702/29/8d7d1d306c26.jpg","http://s03.radikal.ru/i176/1702/9b/8b61f617f654.jpg",
                "http://s16.radikal.ru/i190/1702/5f/29a2411edcd4.jpg","http://s010.radikal.ru/i311/1702/a9/7c35b98477c1.jpg","http://s45.radikal.ru/i107/1702/18/3a1f1fea2bbc.jpg",
                "http://s013.radikal.ru/i324/1702/63/529dd9b7239e.jpg","http://s019.radikal.ru/i636/1702/ff/2cf20b3551cb.jpg","http://s018.radikal.ru/i514/1702/ed/5f851ecdf33b.jpg",
                "http://s019.radikal.ru/i623/1702/9c/9504ddc154ae.jpg","http://s008.radikal.ru/i305/1702/a0/3d886454f109.png","http://s019.radikal.ru/i605/1702/cb/fc6c3967937c.jpg",
                "http://s018.radikal.ru/i527/1702/b7/1e6d30644a25.jpg","http://s020.radikal.ru/i700/1702/4c/05303ab0513f.jpg","http://s41.radikal.ru/i093/1702/ca/9fa49b7b2a08.jpg",
                "http://s009.radikal.ru/i310/1702/c9/5d8d738e6a65.png","http://s013.radikal.ru/i322/1702/da/ade173bbf8b5.jpg","http://i075.radikal.ru/1702/ff/b32e2b30d6fe.jpg",
                "http://s019.radikal.ru/i640/1702/aa/f2536b95b752.jpg","http://s020.radikal.ru/i712/1702/8a/70ec6aeeea1f.jpg","http://s41.radikal.ru/i091/1702/fc/2e12a5f396e7.jpg",
                "http://s020.radikal.ru/i704/1702/a5/03e6b61dce8d.jpg","http://s41.radikal.ru/i093/1702/91/af0cd7c59ed2.gif","http://s020.radikal.ru/i722/1702/23/9da929f7fd7a.jpg",
                "http://s020.radikal.ru/i714/1702/42/1a39b670c1e7.png","http://s015.radikal.ru/i330/1702/f1/4a5fbada8f52.jpg","http://s019.radikal.ru/i620/1702/27/7aa4386bc11b.jpg",
                "http://i023.radikal.ru/1702/e5/b7f270521644.jpg","http://s019.radikal.ru/i602/1702/8d/3ffa98192b6f.jpg","http://s019.radikal.ru/i615/1702/b3/8f72d3d5c41c.jpg",
                "http://s41.radikal.ru/i091/1702/ed/f0684355b6b4.jpg","http://s019.radikal.ru/i623/1702/46/fcb7ba4b180e.jpg","http://s019.radikal.ru/i600/1702/b9/c497001b7446.jpg",
                "http://s016.radikal.ru/i334/1702/d4/8377ae7ba82f.jpg","http://s41.radikal.ru/i093/1702/eb/1425a9243cb2.jpg","http://s48.radikal.ru/i119/1702/90/d30d598551aa.jpg",
                "http://s019.radikal.ru/i644/1702/8a/30a4ef31321c.png","http://s009.radikal.ru/i308/1702/a9/bd4be216b4aa.jpg","http://s018.radikal.ru/i520/1702/b5/32466daabe65.jpg",
                "http://s45.radikal.ru/i110/1702/8f/6a02426d8d1e.jpg","http://s016.radikal.ru/i336/1702/5f/8e3f6b50194a.jpg","http://s018.radikal.ru/i511/1702/4d/14e4a808c6ce.jpg",
                "http://i013.radikal.ru/1702/c0/ebf1f0f81250.jpg","http://s14.radikal.ru/i187/1702/3b/78b3ee7d3bcc.jpg","http://s04.radikal.ru/i177/1702/d1/a116a616d5cf.jpg",
                "http://s45.radikal.ru/i109/1702/b4/7f6bdb905eca.jpg","http://s45.radikal.ru/i108/1702/87/5b3a0dbecff0.jpg","http://s018.radikal.ru/i506/1702/6b/33471e4bae4a.gif",
                "http://s019.radikal.ru/i640/1702/ef/1e6c41ad2466.jpg","http://s019.radikal.ru/i633/1702/7e/916558426797.jpg","http://s019.radikal.ru/i617/1702/80/648f69848bcc.jpg",
                "http://s41.radikal.ru/i093/1702/88/dec8048b533e.jpg","http://s019.radikal.ru/i613/1702/6e/3d97419b6131.jpg","http://s018.radikal.ru/i501/1702/9f/63d982fd83d7.jpg",
                "http://s19.radikal.ru/i192/1702/4c/d2752ead3380.jpg");

        if (imagePath!="") {
            java.awt.Image image = new ImageIcon(imagePath).getImage();
            imageWidth = String.valueOf(image.getWidth(null));
            imageHeight = String.valueOf(image.getHeight(null));

            File f = new File(imagePath);
            for (String path : imagesUrls) {
                if (path.contains((String) f.getName())) {
                    invitatationText = invitatationText.replaceAll("imagePathUrl", path);
                    urlPath = path;
                    break;
                }
            }
        }else{
            urlPath = "http://s020.radikal.ru/i704/1702/f2/a9251bbf9213.jpg";
            invitatationText = invitatationText.replaceAll("imagePathUrl", urlPath);
            imageWidth = "700";
            imageHeight = "400";
        }
        invitatationText = invitatationText.replaceAll("mailimgw", imageWidth);
        invitatationText = invitatationText.replaceAll("mailimgh", imageHeight);
        if (!isTextInitialized) {
            isTextInitialized = true;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            if (WizardData.date != null)
                WizardData.invitatationText = WizardData.invitatationText.replaceAll("ДАТА", String.valueOf(dateFormat.format(WizardData.date)));
            WizardData.invitatationText = WizardData.invitatationText.replaceAll("ВРЕМЯ", String.valueOf(timeFormat.format(WizardData.time)));
            if (WizardData.place != null && WizardData.place != "")
                WizardData.invitatationText = WizardData.invitatationText.replaceAll("МЕСТО", WizardData.place);
            if (WizardData.address != null && WizardData.address != "")
                WizardData.invitatationText = WizardData.invitatationText.replaceAll("АДРЕС", WizardData.address);
            if (WizardData.note != null && WizardData.note != "") {
                WizardData.invitatationText = WizardData.invitatationText.replaceAll("ПРИМЕЧАНИЕ", WizardData.note);
            } else {
                WizardData.invitatationText = WizardData.invitatationText.replaceAll("ПРИМЕЧАНИЕ", "");
            }
            if (WizardData.eventType != null && WizardData.eventType != "")
                WizardData.invitatationText = WizardData.invitatationText.replaceAll("СОБЫТИЕ", WizardData.eventType);
        }
    }

    public static String getRandomString(int length){
        String randomStr = UUID.randomUUID().toString();
        while(randomStr.length()<length){
            randomStr+= UUID.randomUUID().toString();
        }
        return randomStr.substring(0,length);
    }

}
