package kz.dakeshi.easycelebration.NewEventWizard.NewEventWizardSteps;

import com.vaadin.addon.contextmenu.ContextMenu;
import com.vaadin.addon.contextmenu.MenuItem;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.*;
import kz.dakeshi.easycelebration.NewEventWizard.WizardData;
import org.vaadin.teemu.wizards.WizardStep;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class Postcards implements WizardStep {

//    private Component content1;
    private ArrayList<String> imageList = new ArrayList<String>();

    @Override
    public String getCaption() {
        return "Выбор открытки";
    }

    @Override
    public Component getContent() {
        Component content1 = imageViewerShow();
        return content1;
    }

    private Component imageViewerShow() {
        VerticalLayout content = new VerticalLayout();
        ArrayList<HorizontalLayout> newLayout = new ArrayList<HorizontalLayout>();
        ArrayList<Embedded> newImage = new ArrayList<Embedded>();

        processImageList();

        int fiveImageCount=0;
        int imageCount=0;
        int layoutCount=0;

        for (String filePath: imageList){
            fiveImageCount+=1;
            if (fiveImageCount==1){
                newLayout.add(new HorizontalLayout());
                layoutCount+=1;//к лейауту обращаться по индексу layoutCount - 1
            }
            if (fiveImageCount==5){fiveImageCount=0;}
            newImage.add(new Embedded("", new FileResource(new File(filePath))));
            imageCount+=1;
            java.awt.Image image = new ImageIcon(filePath).getImage();
            int imgWidth = image.getWidth(null);
            int imgHeight = image.getHeight(null);
            if (imgHeight>imgWidth) {
                newImage.get(imageCount - 1).setHeight("174px");
            } else {
                newImage.get(imageCount - 1).setWidth("174px");
            }
            if (WizardData.lastImageNumber == imageCount) {
                newImage.get(imageCount-1).addStyleName("selected-image");
                WizardData.lastSelectedImage = newImage.get(imageCount -1);
            }
            ContextMenu menu = new ContextMenu(newImage.get(imageCount-1), true);
            int finalImageCount = imageCount;
            final MenuItem select = menu.addItem("Выбрать это изображение", e1 -> {
                WizardData.imagePath = filePath;
                if (WizardData.lastSelectedImage!=null)WizardData.lastSelectedImage.removeStyleName("selected-image");
                newImage.get(finalImageCount -1).addStyleName("selected-image");
                WizardData.lastSelectedImage = newImage.get(finalImageCount -1);
                WizardData.lastImageNumber = finalImageCount;
                Notification.show("Изображение выбрано", Notification.Type.HUMANIZED_MESSAGE);
            });
            final MenuItem unselect = menu.addItem("Отменить выбор", e1 -> {
                if (newImage.get(finalImageCount -1).equals(WizardData.lastSelectedImage)){
                    WizardData.lastSelectedImage = null;
                    WizardData.imagePath = "";
                    newImage.get(finalImageCount -1).removeStyleName("selected-image");
                }
            });
            select.setIcon(FontAwesome.CHECK);
            unselect.setIcon(FontAwesome.TIMES);
            newImage.get(imageCount-1).setDescription("<p>Левая кнопка мыши - просмотр,</p><p>Правая кнопка мыши - выбор</p>");
            newImage.get(imageCount-1).addClickListener(e->{
                if (newImage.get(finalImageCount -1).equals(WizardData.lastSelectedImage)){
                    select.setEnabled(false);
                    unselect.setEnabled(true);
                } else {
                    unselect.setEnabled(false);
                    select.setEnabled(true);
                }
                if (e.getButton()== MouseEventDetails.MouseButton.LEFT){
                    final Window window = new Window("");
                    window.setSizeFull();
                    window.setModal(true);
                    final VerticalLayout windContent = new VerticalLayout();
                    Embedded imgForView = new Embedded("", new FileResource(new File(filePath)));
                    windContent.setHeight("100%");
                    windContent.setWidth("100%");
                    windContent.addComponent(imgForView);
                    imgForView.setHeight("100%");
                    window.addClickListener(e1 -> {
                        window.close();
                    });
                    windContent.setMargin(true);
                    windContent.setComponentAlignment(imgForView, Alignment.MIDDLE_CENTER);
                    window.setContent(windContent);
                    UI.getCurrent().addWindow(window);
                }
            });
            newLayout.get(layoutCount-1).addComponent(newImage.get(imageCount-1));
            newLayout.get(layoutCount-1).setSpacing(true);

            if (fiveImageCount==0 || imageCount==imageList.size()){
                content.addComponent(newLayout.get(layoutCount-1));
            }
        }
        content.setSpacing(true);
        content.setMargin(true);
        return content;
    }

    public void processImageList() {
        String fileName;
        if (imageList.isEmpty()) {
            String pathToDir = VaadinService.getCurrent()
                    .getBaseDirectory().getAbsolutePath() + "/VAADIN/themes/mytheme/images";
            File imagesDir = new File(pathToDir);
            File[] folderEntries = imagesDir.listFiles();
            for (File entry : folderEntries) {
                if (!entry.isDirectory()) {
                    fileName = entry.getName();
                   if (!fileName.equals("Thumbs.db")) {
                        imageList.add(entry.getAbsolutePath());
                    }
                }
            }
        }
    }

    @Override
    public boolean onAdvance() {
        return true;
    }

    @Override
    public boolean onBack() {
        return true;
    }
}
