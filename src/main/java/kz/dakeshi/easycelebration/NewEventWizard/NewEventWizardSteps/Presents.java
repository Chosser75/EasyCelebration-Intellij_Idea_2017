package kz.dakeshi.easycelebration.NewEventWizard.NewEventWizardSteps;

import com.vaadin.data.Item;
import com.vaadin.ui.*;
import kz.dakeshi.easycelebration.NewEventWizard.Present;
import kz.dakeshi.easycelebration.NewEventWizard.WizardData;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Iterator;

public class Presents implements WizardStep {
    private Component content;
    private Table table;
    private Label label;
    private int presentCount = 0;
    private WizardData wizardData = new WizardData();
    private boolean answer;

    @Override
    public String getCaption() {
        return "Создание списка подарков";
    }

    @Override
    public Component getContent() {
        content = tableShow();
        return content;
    }

    private Component tableShow() {
        HorizontalLayout content1 = new HorizontalLayout();
        table = new Table("Список желаемых подарков");
        table.addContainerProperty("Наименование", String.class, null);
        table.addContainerProperty("Примечание", String.class, null);
        table.setSelectable(true);
        table.setImmediate(true);
        table.setEditable(false);
        table.setSizeFull();

        CheckBox editable = new CheckBox("Редактировать", false);
        editable.addValueChangeListener(valueChange ->
                table.setEditable((Boolean) editable.getValue()));

        Button newPresent = new Button("Добавить подарок");
        Button delete = new Button("Удалить подарок");
        newPresent.setWidth("175px");
        delete.setWidth("175px");
        newPresent.setHeight("50px");
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
        label = new Label("Всего подарков: " + table.size());
        labelLayout.setSizeUndefined();
        labelLayout.addComponents(label, newPresent, delete, editable);

        newPresent.addClickListener(e->{
            newPresentWindowShow();
        });

        delete.addClickListener(e->{
            yesNoWindowShow("Вы уверены, что хотите удалить эту запись?");
        });

        if (!wizardData.presentsList.isEmpty()){
            for (Present present2: WizardData.presentsList){
                presentCount +=1;
                table.addItem(new Object[]{present2.getPresentName(), present2.getNote()}, presentCount);
            }
            label.setValue("Всего подарков: " + table.size());
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
        newPresent.addStyleName("neweventwizard-newguest-button");
        content1.addStyleName("new-event");

        return content1;
    }

    private void newPresentWindowShow() {
        Window newPresentWindow = new Window("Новый подарок");
        VerticalLayout newPresentWindowSubContent = new VerticalLayout();
        newPresentWindowSubContent.setMargin(true);
        newPresentWindowSubContent.setSpacing(true);
        newPresentWindow.setContent(newPresentWindowSubContent);
        TextField presentName = new TextField("Наименование:");
        TextField presentNote = new TextField("Примечание:");
        presentName.setWidth("500px");
        presentNote.setWidth("500px");
        Button done = new Button("Готово");
        done.addClickListener(e1->{
            if (presentName.isEmpty()){
                Notification.show("Поле 'Наименование' должно быть заполнено");
            } else {
                presentCount += 1;
                table.addItem(new Object[]{(presentName.getValue()).trim(), (presentNote.getValue()).trim()}, presentCount);
                label.setValue("Всего подарков: " + table.size());
                newPresentWindow.close();
            }
        });
        Button cancel = new Button("Отмена");
        cancel.addClickListener(e1->{
            newPresentWindow.close();
        });
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponents(cancel, done);
        buttons.setSpacing(true);
        newPresentWindowSubContent.addComponents(presentName,presentNote, buttons);
        newPresentWindowSubContent.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);
        newPresentWindow.center();
        newPresentWindow.setModal(true);
        UI.getCurrent().addWindow(newPresentWindow);
    }

    private void tableListSave() {
        wizardData.presentsList.clear();
        table.select(table.firstItemId());
        if (!table.isEmpty()){
            for (Iterator i = table.getItemIds().iterator(); i.hasNext();) {
                // Get the current item identifier, which is an integer.
                int iid = (Integer) i.next();
                // Now get the actual item from the table.
                Item item = table.getItem(iid);
                wizardData.presentsList.add(new Present(((String) item.getItemProperty("Наименование").getValue()).trim(),
                        (String) item.getItemProperty("Примечание").getValue()));
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
            label.setValue("Всего подарков: " + table.size());
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
