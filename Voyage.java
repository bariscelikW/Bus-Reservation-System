import java.util.*;

// Abstract class representing a voyage
abstract class Voyage {
    protected final String type;
    public int prePercentage;
    protected int id;
    protected String from;
    protected String to;
    protected int numberOfRows;
    protected double price;
    public double revenue;
    protected List<String> sold;
    protected String[][] seatingPlan = new String[0][0];
    protected int numberOfCol;
    // Constructor
    public Voyage(String type, int id, String from, String to, int numberOfRows, double price, double revenue, List<String> sold, int numberOfCol, String[][] seatingPlan) {
        this.type = type;
        this.id = id;
        this.from = from;
        this.to = to;
        this.numberOfRows = numberOfRows;
        this.price = price;
        this.revenue = revenue;
        this.sold = sold;
        this.seatingPlan = seatingPlan;
        this.numberOfCol = numberOfCol;
    }

    // Initializes the seating plan with default values
    public void iniSeatingPlan(){

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfCol; j++) {
                seatingPlan[i][j] = "*";
            }
        }
    }
    // Updates the seating plan based on sold tickets
    public void updateSeatingPlan(){

        for (String num: sold){
            int rowIndex = -1;
            int colIndex = -1;

            int nums = Integer.parseInt(num);
            if (nums % numberOfCol == 0){
                colIndex = (numberOfCol - 1);
            }
            else{
                colIndex = (nums % (numberOfCol)) - 1;
            }

            if (nums % numberOfCol == 0){
                rowIndex = (nums / numberOfCol) - 1;
            }
            else{
                rowIndex = (nums / numberOfCol);
            }
            // Mark the seat as sold in the seating plan
            seatingPlan[rowIndex][colIndex] = "X";
        }
    }
    // Getters and setters
    public String getType(){
        return type;
    }
    public int getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public double getPrice() {
        return price;
    }

    public void calculateRevenue(int numberOfSell){
        revenue += (numberOfSell * price);
    }

    public void addSoldTickets(String ticketNumber){
        sold.add(ticketNumber);
    }

    // Abstract methods to be implemented by subclasses
    public abstract String initializingVoyageMessage();
    public abstract double calculateRefund(String[] refundTicketArr, int length);
    public abstract void cancelVoyageAnd();
    public double getPrePercentage(){
        return 0;
    }


    // Calculates revenue based on the number of tickets sold
    public void subtractRevenue(double amount) {
        revenue -= amount;
    }

    public void removeSoldTickets(List<String> tickets) {
        sold.removeAll(tickets);
    }

    // Generates a string representation of the seating plan
    public String printSeatingPlan() {

        StringBuilder seatingPlanStr = new StringBuilder();
        seatingPlanStr.append("Voyage " + id + "\n" + from + "-" + to + "\n");

        for (String[] row : seatingPlan) {

            if (numberOfCol == 4){
                seatingPlanStr.append(row[0]).append(" ");
                seatingPlanStr.append(row[1]).append(" ");
                seatingPlanStr.append("|").append(" ");
                seatingPlanStr.append(row[2]).append(" ");
                seatingPlanStr.append(row[3]);
            }
            else if (numberOfCol == 3){
                seatingPlanStr.append(row[0]).append(" ");
                seatingPlanStr.append("|").append(" ");
                seatingPlanStr.append(row[1]).append(" ");
                seatingPlanStr.append(row[2]);
            }
            else{
                seatingPlanStr.append(row[0]).append(" ");
                seatingPlanStr.append(row[1]);
            }
            seatingPlanStr.append("\n");
        }
        seatingPlanStr.append("Revenue: " + String.format(Locale.ENGLISH,"%.2f", revenue));
        return seatingPlanStr.toString();
    }
}