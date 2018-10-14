package kz.dakeshi.easycelebration.NewEventWizard.NewEventWizardSteps;

import com.vaadin.data.Item;
import com.vaadin.ui.*;
import kz.dakeshi.easycelebration.NewEventWizard.Guest;
import kz.dakeshi.easycelebration.NewEventWizard.WizardData;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Iterator;

public class Guests implements WizardStep {
    private Component content;
    private Table table;
    private Label label;
    private int guestCount = 0;
    private WizardData wizardData = new WizardData();
    private boolean answer;

    @Override
    public String getCaption() {
        return "Создание списка гостей";
    }

    @Override
    public Component getContent() {

        content = tableShow();
        return content;
    }

    private Component tableShow() {
        HorizontalLayout content1 = new HorizontalLayout();
        table = new Table("Список приглашаемых гостей");
        table.addContainerProperty("Фамилия", String.class, null);
        table.addContainerProperty("Имя", String.class, null);
        table.addContainerProperty("E-mail", String.class, null);
        table.addContainerProperty("Телефон", String.class, null);
        table.setSelectable(true);
        table.setImmediate(true);
        table.setEditable(false);
        table.setSizeFull();

        CheckBox editable = new CheckBox("Редактировать", false);
        editable.addValueChangeListener(valueChange ->
                table.setEditable((Boolean) editable.getValue()));

        Button newGuest = new Button("Добавить гостя");
        Button delete = new Button("Удалить");
        newGuest.setWidth("140px");
        delete.setWidth("140px");
        newGuest.setHeight("50px");
        delete.setHeight("50px");
        delete.setEnabled(false);
        table.addValueChangeListener(e->{
            if (table.isEmpty()){
                delete.setEnabled(false);
            }
            else {
                delete.setEnabled(true);
            }
        });

        VerticalLayout labelLayout =new VerticalLayout();
        label = new Label("Всего гостей: " + table.size());
        labelLayout.setSizeUndefined();
        labelLayout.addComponents(label, newGuest, delete, editable);

        newGuest.addClickListener(e->{
            newGuestWindowShow();
        });

        delete.addClickListener(e->{
            yesNoWindowShow("Вы уверены, что хотите удалить эту запись?");
        });

        if (!wizardData.guestsList.isEmpty()){
            for (Guest guest2: WizardData.guestsList){
                guestCount+=1;
                table.addItem(new Object[]{guest2.getLastName(), guest2.getFirstName(),
                        guest2.getEmail(), guest2.getPhone()}, guestCount);
            }
            label.setValue("Всего гостей: " + table.size());
        }

        content1.setSizeFull();
        content1.setMargin(true);
        content1.addComponents(table, labelLayout);
        content1.setExpandRatio(table, 1);
        labelLayout.setMargin(true);
        labelLayout.setSpacing(true);

        //Styles
        table.addStyleName("table1");
        delete.addStyleName("neweventwizard-guestdelete-button");
        newGuest.addStyleName("neweventwizard-newguest-button");
        content1.addStyleName("new-event");

        return content1;
    }

    private void newGuestWindowShow() {
        Window newGuestWindow = new Window("Новый гость");
        VerticalLayout newGuestWindowSubContent = new VerticalLayout();
        newGuestWindowSubContent.setMargin(true);
        newGuestWindowSubContent.setSpacing(true);
        newGuestWindow.setContent(newGuestWindowSubContent);
        TextField guestLastName = new TextField("Фамилия:");
        TextField guestFirstName = new TextField("Имя, отчество:");
        TextField guestEmail = new TextField("E-mail:");
        TextField guestPhone = new TextField("Телефон:");

        guestLastName.setWidth("300px");
        guestFirstName.setWidth("300px");
        guestEmail.setWidth("300px");
        guestPhone.setWidth("300px");

        Button done = new Button("Готово");
        done.addClickListener(e1->{
            if (guestLastName.isEmpty() || guestFirstName.isEmpty() || guestEmail.isEmpty() || guestPhone.isEmpty()){
                Notification.show("Все поля должны быть заполнены");
            } else {
                guestCount+= 1;
                table.addItem(new Object[]{(guestLastName.getValue()).trim(), (guestFirstName.getValue()).trim(),
                        (guestEmail.getValue()).trim(), (guestPhone.getValue()).trim()}, guestCount);
                label.setValue("Всего гостей: " + table.size());
                newGuestWindow.close();
            }
        });
        Button cancel = new Button("Отмена");
        cancel.addClickListener(e1->{
            newGuestWindow.close();
        });
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponents(cancel, done);
        buttons.setSpacing(true);
        newGuestWindowSubContent.addComponents(guestLastName,guestFirstName,
                                            guestEmail, guestPhone, buttons);
        newGuestWindowSubContent.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);
        newGuestWindow.center();
        newGuestWindow.setModal(true);
        UI.getCurrent().addWindow(newGuestWindow);
    }

    private void tableListSave() {
        wizardData.guestsList.clear();
        table.select(table.firstItemId());
        if (!table.isEmpty()){
            for (Iterator i = table.getItemIds().iterator(); i.hasNext();) {
                // Get the current item identifier, which is an integer.
                int iid = (Integer) i.next();
                // Now get the actual item from the table.
                Item item = table.getItem(iid);
                String un = WizardData.getRandomString(10);
                String pw = WizardData.getRandomString(10);
                wizardData.guestsList.add(new Guest(((String) item.getItemProperty("Фамилия").getValue()).trim(),
                        ((String) item.getItemProperty("Имя").getValue()).trim(),
                        ((String) item.getItemProperty("E-mail").getValue()).trim(),
                        (String) item.getItemProperty("Телефон").getValue(),un, pw));
            }
        }
    }

    public void yesNoWindowShow(String message) {
        Label label1 = new Label();
        Window noYesWindow = new Window("Подтверждение действия");
        VerticalLayout windowSubContent = new VerticalLayout();
        windowSubContent.setMargin(true);
        windowSubContent.setSpacing(true);
        label1.setValue(message);
        Button done = new Button("ДА");
        done.addClickListener(e1->{
            table.removeItem(table.getValue());
            label.setValue("Всего гостей: " + table.size());
            noYesWindow.close();
        });

        Button cancel = new Button("НЕТ");
        cancel.addClickListener(e1->{
            noYesWindow.close();
        });

        //Styles
        label1.addStyleName("message-label");


        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponents(cancel, done);
        buttons.setSpacing(true);

        windowSubContent.addComponents(label1, buttons);
        windowSubContent.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);
        noYesWindow.setContent(windowSubContent);
        noYesWindow.center();
        noYesWindow.setModal(true);
        UI.getCurrent().addWindow(noYesWindow);
    }

    @Override
    public boolean onAdvance() {
        tableListSave();
        return true;
    }

    @Override
    public boolean onBack() {
        tableListSave();
        return true;
    }
}
