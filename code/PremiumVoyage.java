import java.util.List;
import java.util.Locale;

// Subclass representing a premium voyage
class PremiumVoyage extends Voyage {
    public double refundPercentage;
    public double prePercentage;
    public double refundPrice;
    // Constructor
    public PremiumVoyage(String type, int id, String from, String to, int numberOfRows, double price, double revenue, List<String> sold, int numberOfCol, String[][] seatingPlan, double refundPercentage, double refundPrice, double prePercentage) {
        super(type, id, from, to, numberOfRows, price, revenue, sold, 3, seatingPlan);
        this.refundPercentage = refundPercentage;
        this.refundPrice = refundPrice; // Initialize refundPrice
        this.prePercentage = prePercentage;
    }
    // Override method to generate an initializing message for the premium voyage
    @Override
    public String initializingVoyageMessage(){
        return "Voyage " + id + " was initialized as a premium (1+2) voyage from " + from +" to " + to +
                " with " +  String.format(Locale.ENGLISH,"%.2f", price) + " TL priced " + (numberOfRows * 2) + " regular seats and " + String.format(Locale.ENGLISH,"%.2f", (price * (100 + prePercentage) / 100)) + " TL priced " +
                numberOfRows + " premium seats. Note that refunds will be " + String.format(Locale.ENGLISH,"%.0f", refundPercentage) + "% less" +
                " than the paid amount.";
    }
    // Override method to calculate refund amount
    @Override
    public void calculateRevenue(int numberOfSell){
        revenue += (numberOfSell * price);
    }

    @Override
    public double calculateRefund(String[] refundTicketArr, int numberOfTickets){
        int preSeats = 0;
        int normalSeats = 0;
        for (String st: refundTicketArr){
            if (Integer.parseInt(st) % 3 == 1){
                // premium ticket
                preSeats++;
            }
            else{
                normalSeats++;
            }
        }
        refundPrice += price - ((price * (100 - refundPercentage) / 100) * normalSeats) + ((((price * (100 + prePercentage) / 100)) * (100 - refundPercentage) / 100) * preSeats);
        return ((price * (100 - refundPercentage) / 100) * normalSeats) + ((((price * (100 + prePercentage) / 100)) * (100 - refundPercentage) / 100) * preSeats);
    }
    // Override method to cancel the voyage and adjust revenue
    @Override
    public void cancelVoyageAnd(){
        int preSeats = 0;
        int normalSeats = 0;
        for (String st: sold){
            if (Integer.parseInt(st) % 3 == 1){
                // premium ticket
                preSeats++;
            }
            else{
                normalSeats++;
            }
        }

        revenue -= ((price * normalSeats) + (price * ((100 + prePercentage) / 100) * preSeats));
    }
    // Override method to get the premium percentage
    @Override
    public double getPrePercentage() {
        return prePercentage;
    }

}
