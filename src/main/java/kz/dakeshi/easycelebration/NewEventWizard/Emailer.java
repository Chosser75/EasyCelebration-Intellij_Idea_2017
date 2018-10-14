package kz.dakeshi.easycelebration.NewEventWizard;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vaadin.ui.*;
import kz.dakeshi.easycelebration.MainContent;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.vaadin.teemu.wizards.Wizard;
import org.xhtmlrenderer.swing.Java2DRenderer;
import org.xhtmlrenderer.util.FSImageWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.Button;
import java.awt.Label;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Emailer implements Runnable {
    private String invitationPicture = "invitation";
    private String template1="<html>\n" +
            "<head>\n" +
            "\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "<img src=\"http://res.cloudinary.com/dnmowzxh0/image/upload/v1486995782/invitation.png\" align=\"left\" style=\"width:98%; max-width:700px;\">\n" +
            "</body>\n" +
            "</html>";
    private String emailFails = "Не удалось отправить сообщения следующим получателям:";
    private boolean emailFail = false;
    private final ProgressBar progress = new ProgressBar();
    private int progressCurrent = 0;
    private int progressMax = WizardData.guestsList.size();
    private com.vaadin.ui.Window progressWindow;

    @Override
    public void run() {
        UI.getCurrent().access(() -> {
        File file = new File("invitation.png");
        File file1 = new File("html.html");
        String subj = WizardData.initiator+" приглашает Вас на "+WizardData.eventType;
        progressWindowShow();
        UI.getCurrent().push();
        for (Guest guest: WizardData.guestsList) {
            ++progressCurrent;
            progress.setValue((float) progressCurrent / progressMax);
            UI.getCurrent().push();
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dnmowzxh0",
                    "api_key", "486684858584561",
                    "api_secret", "ibL8vafDHj83H6qDePumhTEUC8c"));
            String template = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<meta charset=\"utf-8\"/>\n" +
                    "<title></title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    WizardData.invitatationText +
                    "</body>\n" +
                    "</html>";
            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(465);
            email.setSSLOnConnect(true);
            email.setAuthentication(
                    "dkshibekov@gmail.com", "k.vbybcwtyn");

            template = template.replaceAll("ИМЯ ОТЧЕСТВО", guest.getFirstName());
            template = template.replaceAll("&nbsp;", "<br/>");
            if(file.exists()){
                file.delete();
            }
            if(file1.exists()){
                file1.delete();
            }
            int width = Integer.parseInt(WizardData.imageWidth);
            int height = Integer.parseInt(WizardData.imageHeight);
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("html.html"));
                out.write(template);
                out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Java2DRenderer renderer = null;
            try {
                renderer = new Java2DRenderer(file1, width, height);
                renderer.setRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            BufferedImage image = renderer.getImage();
            // write it out full size png defaults to png.
            FSImageWriter imagWriter = new FSImageWriter();
            try {
                imagWriter.write(image, "invitation.png");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                Map uploadResult = cloudinary.uploader().upload(new File("invitation.png"), ObjectUtils.emptyMap());
                changeTemplate(uploadResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                email.setFrom("dkshibekov@gmail.com");
                email.addTo(guest.getEmail());
                email.setSubject(subj);
                email.setCharset("utf-8");
                email.setHtmlMsg(template1);
                //Include invitation card as attachment
//                EmailAttachment attachment = new EmailAttachment();
//                attachment.setPath("Invitation.jpg");
//                attachment.setDisposition(EmailAttachment.ATTACHMENT);
//                email.attach(attachment);
                email.send();
            } catch (EmailException e) {
                guest.setInvitationIsSent(false);
                emailFails += " "+guest.getLastName() + ": "+guest.getEmail()+";";
                emailFail = true;
            }
        }
            progress.setValue(0f);
            progress.setVisible(!progress.isIndeterminate());
            progressCurrent = 0;
            progressWindow.setClosable(true);
            progressWindow.close();
            UI.getCurrent().push();
        if (emailFail) {
            emailFailureWindowShow ();
               UI.getCurrent().push();
        } else {
            Notification.show("Мероприятие создано!");}
            try {
                WizardData.dbSave();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            WizardData.wdclean();
        });
    }

    private void emailFailureWindowShow (){
        emailFails=emailFails.replaceFirst(".$","");
        com.vaadin.ui.Label label = new com.vaadin.ui.Label(emailFails);
        label.setWidth("400px");
        com.vaadin.ui.Window emailFailureWindow = new com.vaadin.ui.Window("Ошибка отправки сообщений!");
        VerticalLayout windowSubContent = new VerticalLayout();
        windowSubContent.setMargin(true);
        windowSubContent.setSpacing(true);

        com.vaadin.ui.Button done = new com.vaadin.ui.Button("ДА");
        done.addClickListener(e1->{

            emailFailureWindow.close();
            Notification.show("Мероприятие создано!");
        });

        com.vaadin.ui.Button cancel = new com.vaadin.ui.Button("НЕТ");
        cancel.addClickListener(e1->{
            emailFailureWindow.close();
            Notification.show("Мероприятие создано!");
        });

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponents(cancel, done);
        buttons.setSpacing(true);

        windowSubContent.addComponents(label, buttons);
        windowSubContent.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        windowSubContent.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);
        emailFailureWindow.setContent(windowSubContent);
        emailFailureWindow.center();
        emailFailureWindow.setModal(true);
        UI.getCurrent().addWindow(emailFailureWindow);
    }

    private  void changeTemplate(Map uploadResult){
        template1 = template1.replaceAll(invitationPicture, (String)uploadResult.get("public_id"));
        invitationPicture = (String)uploadResult.get("public_id");
    }

    private void progressWindowShow(){
        progress.setValue(0f);
        progress.setVisible(true);
        progress.setWidth("300px");
        progressWindow = new com.vaadin.ui.Window("Отправка приглашений по электронной почте");
        VerticalLayout windowSubContent = new VerticalLayout();
        windowSubContent.setMargin(true);
        windowSubContent.setSpacing(true);

        windowSubContent.addComponent(progress);
        windowSubContent.setComponentAlignment(progress, Alignment.MIDDLE_CENTER);
        progressWindow.setContent(windowSubContent);
        progressWindow.center();
        progressWindow.setModal(true);
        progressWindow.setResizable(false);
        progressWindow.setClosable(false);
        UI.getCurrent().addWindow(progressWindow);
    }
}
