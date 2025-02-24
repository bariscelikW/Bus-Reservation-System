import java.util.List;
import java.util.Locale;

// Subclass representing a standard voyage
class StandardVoyage extends Voyage {
    // Additional attributes specific to StandardVoyage
    public double refundPercentage;
    public double refundPrice;
    // Constructor
    public StandardVoyage(String type, int id, String from, String to, int numberOfRows, double price, double revenue, List<String> sold, int numberOfCol, String[][] seatingPlan, double refundPercentage, double refundPrice) {
        super(type, id, from, to, numberOfRows, price, revenue, sold, 4, seatingPlan);
        this.refundPercentage = refundPercentage;
        this.refundPrice = refundPrice; // Initialize refundPrice
    }
    // Override method to generate an initializing message for the standard voyage
    @Override
    public String initializingVoyageMessage(){
        return "Voyage " + id + " was initialized as a standard (2+2) voyage from " + from +" to " + to +
                " with " +  String.format(Locale.ENGLISH,"%.2f", price) + " TL priced " + (numberOfRows * 4) + " regular seats. Note that refunds will be "
                + String.format(Locale.ENGLISH,"%.0f", refundPercentage) + "% less" +
                " than the paid amount.";
    }
    // Override method to calculate refund amount
    @Override
    public double calculateRefund(String[] refundTicketArr, int numberOfTickets){
        refundPrice += price - ((price * (100 - refundPercentage) / 100) * numberOfTickets);
        return ((price * (100 - refundPercentage) / 100) * numberOfTickets);
    }

    // Override method to cancel the standard voyage and adjust revenue
    @Override
    public void cancelVoyageAnd(){
        revenue -= price * sold.size();
    }
}
