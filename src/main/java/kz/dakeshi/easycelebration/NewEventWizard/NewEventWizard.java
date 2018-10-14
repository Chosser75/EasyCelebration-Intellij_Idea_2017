package kz.dakeshi.easycelebration.NewEventWizard;

import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import kz.dakeshi.easycelebration.MainContent;
import kz.dakeshi.easycelebration.NewEventWizard.NewEventWizardSteps.*;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.*;

public class NewEventWizard  implements WizardProgressListener {
    private Wizard wizard;
    private VerticalLayout mainLayout;

    public void wizardInitialize() {

        wizard = new Wizard();

        mainLayout = new VerticalLayout();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        UI.getCurrent().setContent(mainLayout);

        wizard.setUriFragmentEnabled(true);
        wizard.addListener(this);
        wizard.addStep(new NewEvent(), "newevent");
        wizard.addStep(new Guests(), "guests");
        wizard.addStep(new Presents(), "presents");
        wizard.addStep(new Postcards(), "postcards");
        wizard.addStep(new InvitationText(), "invitationtext");
        wizard.setHeight("100%");
        wizard.setWidth("80%");

        wizard.getNextButton().setCaption("Далее");
        wizard.getBackButton().setCaption("Назад");
        wizard.getFinishButton().setCaption("Готово");
        wizard.getCancelButton().setCaption("Отмена");

        mainLayout.addComponent(wizard);
        mainLayout.setComponentAlignment(wizard, Alignment.TOP_CENTER);
    }

    @Override
    public void activeStepChanged(WizardStepActivationEvent event) {
        Page.getCurrent().setTitle(event.getActivatedStep().getCaption());
    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent wizardStepSetChangedEvent) {

    }

    @Override
    public void wizardCompleted(WizardCompletedEvent wizardCompletedEvent) {
        (new Thread(new Emailer())).start();
        wizard.setVisible(false);
        MainContent.mainContentShow();
    }

    @Override
    public void wizardCancelled(WizardCancelledEvent wizardCancelledEvent) {
        WizardData.wdclean();
        wizard.finish();
        Notification.show("Мероприятие отменено!");
        MainContent.mainContentShow();
    }

}
