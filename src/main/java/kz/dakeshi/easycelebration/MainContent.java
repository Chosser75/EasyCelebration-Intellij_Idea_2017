package kz.dakeshi.easycelebration;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import kz.dakeshi.easycelebration.NewEventWizard.NewEventWizard;

public class MainContent {
    public static void mainContentShow(){
        final NewEventWizard newEventWizard = new NewEventWizard();
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        Button but = new Button("Новое событие");
        but.addClickListener(e->{
            newEventWizard.wizardInitialize();
        });
        mainLayout.addComponent(but);
        mainLayout.setComponentAlignment(but, Alignment.MIDDLE_CENTER);
        UI.getCurrent().setContent(mainLayout);
    }
}
