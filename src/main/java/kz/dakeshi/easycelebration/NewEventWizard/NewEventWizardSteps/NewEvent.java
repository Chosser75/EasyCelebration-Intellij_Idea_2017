package kz.dakeshi.easycelebration.NewEventWizard.NewEventWizardSteps;

import com.vaadin.server.UserError;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import kz.dakeshi.easycelebration.NewEventWizard.NewEventWizardDB.EventTypeDB;
import kz.dakeshi.easycelebration.NewEventWizard.WizardData;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.ArrayList;
import java.util.Date;


public class NewEvent implements WizardStep {
    private ArrayList<String> eventTypeList;
    private EventTypeDB eventTypeDB;
    private ComboBox eventType;
    private TextField initiator;
    private TextField initiator2;
    private PopupDateField date;
    private InlineDateField time;
    private TextField place;
    private TextField address;
    private TextArea note;

    @Override
    public String getCaption() {
        return "Создание нового мероприятия";
    }

    @Override
    public Component getContent() {
        FormLayout content = new FormLayout();
        eventType = new ComboBox("Выберите тип торжества:");
        eventTypeDB = new EventTypeDB();
        time = new InlineDateField("Время начала:");
        time.setResolution(Resolution.MINUTE);
        initiator = new TextField("Хозяин торжества:");
        initiator.setWidth("400px");
        date = new PopupDateField("Дата проведения:");
        place = new TextField("Место проведения:");
        place.setWidth("400px");
        address = new TextField("Адрес места проведения:");
        address.setWidth("400px");
        note = new TextArea("Описание:");
        note.setWidth("400px");
        note.setHeight("90px");

        try {
            eventTypeList = eventTypeDB.db();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        eventType.addItems(eventTypeList);
        eventType.setValue(WizardData.eventType);
        initiator.setValue(WizardData.initiator);
        date.setValue(WizardData.date);
        time.setValue(WizardData.time);
        place.setValue(WizardData.place);
        address.setValue(WizardData.address);
        note.setValue(WizardData.note);

        content.setSizeFull();
        content.setMargin(true);

        content.addComponents(eventType,initiator, date, time, place, address, note);

        //Check the fields are filled
        time.addStyleName("time-only");
        content.addStyleName("new-event");

        return content;
    }

    @Override
    public boolean onAdvance() {
        // Проверяем заполнение всех полей
        if (eventType.getValue()==null || initiator.getValue()==null || date.getValue()==null
            || time.getValue()==null || place.getValue()==null || address.getValue()==null
                || eventType.getValue()=="" || initiator.getValue()==""
                || place.getValue()=="" || address.getValue()==""){
            Notification.show("Необходимо заполнить все поля");
            return false;
        }
        fieldsFill();
        return true;
    }

    private void fieldsFill() {
        WizardData.eventType = ((String) eventType.getValue()).trim();
        WizardData.initiator = ((String) initiator.getValue()).trim();
        WizardData.date = date.getValue();
        WizardData.time = time.getValue();
        WizardData.place = ((String) place.getValue()).trim();
        WizardData.address = ((String) address.getValue()).trim();
        if (!note.isEmpty()) WizardData.note = ((String) note.getValue()).trim();
    }

    @Override
    public boolean onBack() {
        return true;
    }
}
