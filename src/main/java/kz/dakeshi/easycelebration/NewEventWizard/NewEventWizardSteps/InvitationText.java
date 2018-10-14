package kz.dakeshi.easycelebration.NewEventWizard.NewEventWizardSteps;

import com.vaadin.ui.*;
import kz.dakeshi.easycelebration.NewEventWizard.WizardData;
import org.vaadin.openesignforms.ckeditor.CKEditorConfig;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;
import org.vaadin.teemu.wizards.WizardStep;


public class InvitationText implements WizardStep {
    CKEditorTextField textArea;

    @Override
    public String getCaption() {
        return "Текст приглашения";
    }

    @Override
    public Component getContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        CKEditorConfig config = new CKEditorConfig();
        config.useCompactTags();
        config.disableElementsPath();
        config.disableSpellChecker();
        config.disableResizeEditor();
        config.addFontName("Mistral");
        config.addCustomToolbarLine("{ items : ['Preview','Print','-','Cut','Copy','Paste','-','Undo','Redo','-','Font','FontSize','TextColor','BGColor','-','Maximize'] }");
        config.addCustomToolbarLine("{ items : ['Bold','Italic','Underline','-','NumberedList','BulletedList','-','Outdent','Indent','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','Image','SpecialChar','-','Source','CreateDiv'] }");
        textArea = new CKEditorTextField(config);
        textArea.setSizeFull();
        textArea.setImmediate(true);
        WizardData.invitationTextProcess();
        textArea.setValue(WizardData.invitatationText);
        textArea.addValueChangeListener(e->{WizardData.invitatationText = textArea.getValue();});
        content.setSizeFull();
        content.addComponents(textArea);
//        content.setExpandRatio(textArea, 1);
        return content;
    }

    @Override
    public boolean onAdvance() {
        WizardData.invitatationText = textArea.getValue();
        return true;
    }

    @Override
    public boolean onBack() {
        WizardData.invitatationText = textArea.getValue();
        WizardData.invitatationText = WizardData.invitatationText.replaceAll(WizardData.urlPath, "imagePathUrl");
        if (WizardData.imageWidth != null) WizardData.invitatationText = WizardData.invitatationText.replaceAll(WizardData.imageWidth, "mailimgw");
        if (WizardData.imageHeight != null) WizardData.invitatationText = WizardData.invitatationText.replaceAll(WizardData.imageHeight, "mailimgh");
        return true;
    }
}
