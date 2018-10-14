package kz.dakeshi.easycelebration.NewEventWizard;

public class Guest {
    private String lastName;
    private String firstName;
    private String email;
    private String phone;
    private String un;
    private String pw;
    private  boolean invitationIsSent = true;

    public Guest(String lastName, String firstName, String email, String phone, String un, String pw) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phone = phone;
        this.un = un;
        this.pw = pw;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUn() {
        return un;
    }

    public String getPw() {
        return pw;
    }

    public boolean isInvitationIsSent() {
        return invitationIsSent;
    }

    public void setInvitationIsSent(boolean invitationIsSent) {
        this.invitationIsSent = invitationIsSent;
    }
}
