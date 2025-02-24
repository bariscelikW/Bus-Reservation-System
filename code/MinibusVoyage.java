import java.util.List;
import java.util.Locale;

// Subclass representing a minibus voyage
class MinibusVoyage extends Voyage {
    // Constructor
    public MinibusVoyage(String type, int id, String from, String to, int numberOfRows, double price, double revenue, List<String> sold,  int numberOfCol, String[][] seatingPlan) {
        super(type, id, from, to, numberOfRows, price, revenue, sold,2, seatingPlan);
    }
    // Override method to generate an initializing message for the minibus voyage
    @Override
    public String initializingVoyageMessage(){ //
        return "Voyage " + id + " was initialized as a minibus (2) voyage from " + from +" to " + to +
                " with " +  String.format(Locale.ENGLISH,"%.2f", price) + " TL priced " + (numberOfRows * 2) + " regular seats. Note that minibus tickets are not refundable.";
    }
    // Override method to calculate refund amount (always returns 0 for minibus voyages)
    @Override
    public double calculateRefund(String[] refundTicketArr, int length) {
        return 0;
    }
    // Override method to cancel the minibus voyage and adjust revenue
    @Override
    public void cancelVoyageAnd(){
        revenue -= price * sold.size();
    }
}
