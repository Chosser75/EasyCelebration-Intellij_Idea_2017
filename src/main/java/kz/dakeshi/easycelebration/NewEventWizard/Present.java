package kz.dakeshi.easycelebration.NewEventWizard;

/**
 * Created by ASUS on 29.01.2017.
 */
public class Present {
    private String presentName;
    private String note;

    public Present(String presentName, String note) {
        this.presentName = presentName;
        this.note = note;
    }

    public String getPresentName() {
        return presentName;
    }

    public void setPresentName(String presentName) {
        this.presentName = presentName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
