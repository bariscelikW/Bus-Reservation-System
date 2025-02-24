import java.util.*;

// Class representing the main booking system
public class BookingSystem {

    public static void main(String[] args) {

        // Check if the correct number of command-line arguments is provided
        if (args.length != 2) {
            System.out.println("Usage: java Main arg1 arg2");
            return;
        }

        // Retrieve input and output file paths from command-line arguments
        String inputPath = args[0];
        String outputPath = args[1];

        // Variables for system state tracking
        boolean isReal = false;
        boolean noZReportAnymore = false;
        int lineNum = 0;

        // HashMap to store voyages with their IDs as keys
        HashMap<Integer, Voyage> voyages = new HashMap<>();
        // Read content from input file
        String[] content = FileInput.readFile(inputPath, false, false);
        // Clear the contents of the output file
        FileOutput.writeToFile(outputPath + ".txt", "", false, false);

        int lenOfInput = content.length;
        int lengthOfZReport = 0;

        // Calculate the length of "Z_REPORT" commands
        for (String line : content){
            line = line.trim();
            String[] parts2 = line.split("\t");

            if (line.isEmpty()){
                continue;
            }

            if (parts2[0].equals("Z_REPORT")){
                lengthOfZReport++;
            }
        }

        // Loop through each line in the input content
        outerLoop: for (String line : content) {
            line = line.trim();

            // Skip processing if the line is empty
            if (line.isEmpty()){
                lenOfInput--;
                continue;
            }
            // Split the line by tab character
            String[] parts = line.split("\t");
            String whatToDo = parts[0];

            // Switch case to handle different commands
            switch (whatToDo){

                case "INIT_VOYAGE":
                    // Check if there are no more "Z_REPORT" commands and add a newline if applicable
                    if (noZReportAnymore == true){
                        FileOutput.writeToFile(outputPath + ".txt", "\n", true, false);
                    }

                    isReal = false;

                    FileOutput.writeToFile(outputPath + ".txt", "COMMAND: " + line, true, true);
                    String type = parts[1];
                    String[] typeNames = new String[]{"Standard", "Premium", "Minibus"};
                    // Validate the type of the voyage
                    if (!Arrays.asList(typeNames).contains(type)) {
                        // Handle the case where the type is not one of the predefined types

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue; // Skip processing further for this line
                    }
                    // Extract and validate the ID of the voyage
                    int id;
                    try {
                        id = Integer.parseInt(parts[2]);
                        if (id <= 0) {

                            FileOutput.writeToFile(outputPath + ".txt","ERROR: " + id + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
                            continue; // Skip processing the rest of the loop iteration
                        }
                    } catch (NumberFormatException e) {

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue; // Skip processing the rest of the loop iteration
                    }

                    // Check if a voyage with the same ID already exists
                    if (voyages.containsKey(id)){
                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: There is already a voyage with ID of " + id + "!", true, true);
                        continue;
                    }


                    String from = parts[3];
                    String to = parts[4];

                    int numberOfRows;
                    try {
                        numberOfRows = Integer.parseInt(parts[5]);
                        if (numberOfRows <= 0) {

                            FileOutput.writeToFile(outputPath + ".txt", "ERROR: " + numberOfRows + " is not a positive integer, number of seat rows of a voyage must be a positive integer!", true, true);
                            continue; // Skip processing the rest of the loop iteration
                        }
                    } catch (NumberFormatException e) {

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue; // Skip processing the rest of the loop iteration
                    }

                    double price;
                    try {
                        price = Double.parseDouble(parts[6]);
                        if (price <= 0) {

                            FileOutput.writeToFile(outputPath + ".txt", "ERROR: " + String.format(Locale.ENGLISH,"%.0f", price) + " is not a positive number, price must be a positive number!", true, true);
                            continue; // Skip processing the rest of the loop iteration
                        }
                    } catch (NumberFormatException e) {

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue; // Skip processing the rest of the loop iteration
                    }


                    // Initialize voyages based on their types
                    if (type.equals("Standard")) {

                        double refundPercentage;
                        try {
                            refundPercentage = Double.parseDouble(parts[7]);
                            if (refundPercentage < 0 || refundPercentage > 100) {

                                FileOutput.writeToFile(outputPath + ".txt", "ERROR: " + String.format(Locale.ENGLISH,"%.0f", refundPercentage) + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", true, true);
                                continue; // Skip processing the rest of the loop iteration
                            }
                        } catch (NumberFormatException e) {

                            FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                            continue; // Skip processing the rest of the loop iteration
                        }
                        // Create a new StandardVoyage instance
                        voyages.put(id, new StandardVoyage(type, id, from, to, numberOfRows, price, 0, new ArrayList<>(),4, new String[numberOfRows][4], refundPercentage, 0));

                    } else if (type.equals("Premium")) {

                        double refundPercentage;
                        try {
                            refundPercentage = Double.parseDouble(parts[7]);
                            if (refundPercentage < 0 || refundPercentage > 100) {

                                FileOutput.writeToFile(outputPath + ".txt", "ERROR: " + String.format(Locale.ENGLISH,"%.0f", refundPercentage) + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", true, true);
                                continue; // Skip processing the rest of the loop iteration
                            }
                        } catch (NumberFormatException e) {

                            FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                            continue; // Skip processing the rest of the loop iteration
                        }


                        double prePercentage;
                        try {
                            prePercentage = Double.parseDouble(parts[8]);
                            if (prePercentage < 0) {

                                FileOutput.writeToFile(outputPath + ".txt", "ERROR: " + String.format(Locale.ENGLISH,"%.0f", prePercentage) + " is not a non-negative integer, premium fee must be a non-negative integer!", true, true);
                                continue; // Skip processing the rest of the loop iteration
                            }
                        } catch (NumberFormatException e) {

                            FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                            continue; // Skip processing the rest of the loop iteration
                        }
                        // Create a new PremiumVoyage instance
                        voyages.put(id, new PremiumVoyage(type, id, from, to, numberOfRows, price, 0, new ArrayList<>(),3, new String[numberOfRows][3], refundPercentage, 0, prePercentage));

                    } else if (type.equals("Minibus")) {
                        voyages.put(id, new MinibusVoyage(type, id, from, to, numberOfRows, price, 0, new ArrayList<>(),2, new String[numberOfRows][2]));
                    }
                    // Get the initialized voyage
                    Voyage iniVoyage = voyages.get(id);

                    // Write the initializing message to the output file
                    FileOutput.writeToFile(outputPath + ".txt", iniVoyage.initializingVoyageMessage(), true, true);

                    break;


                case "Z_REPORT":
                    isReal = true;
                    lineNum += 1;
                    // Write the command to the output file
                    FileOutput.writeToFile(outputPath + ".txt", "COMMAND: " + line, true, true);
                    if (parts.length != 1){
                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue;
                    }
                    // Check if there are no voyages available
                    if (voyages.size() == 0){
                        // Write appropriate messages to the output file
                        if (lineNum == lengthOfZReport){
                            FileOutput.writeToFile(outputPath + ".txt", "Z Report:", true, true);
                            FileOutput.writeToFile(outputPath + ".txt", "----------------", true, true);
                            FileOutput.writeToFile(outputPath + ".txt", "No Voyages Available!", true, true);
                            FileOutput.writeToFile(outputPath + ".txt", "----------------", true, false);
                            continue;
                        }
                        else{
                            FileOutput.writeToFile(outputPath + ".txt", "Z Report:", true, true);
                            FileOutput.writeToFile(outputPath + ".txt", "----------------", true, true);
                            FileOutput.writeToFile(outputPath + ".txt", "No Voyages Available!", true, true);
                            FileOutput.writeToFile(outputPath + ".txt", "----------------", true, true);
                            continue;
                        }
                    }

                    // Write the "Z Report" header to the output file
                    FileOutput.writeToFile(outputPath + ".txt", "Z Report:", true, true);
                    FileOutput.writeToFile(outputPath + ".txt", "----------------", true, true);
                    int vl = 0;
                    // Iterate over voyages and print their seating plans
                    for (Voyage voyageObj: voyages.values()){
                        vl++;
                        voyageObj.iniSeatingPlan();
                        voyageObj.updateSeatingPlan();
                        FileOutput.writeToFile(outputPath + ".txt", voyageObj.printSeatingPlan(), true, true);
                        // Add a separator after each voyage's seating plan except for the last one
                        if (lineNum !=  lengthOfZReport || vl != voyages.size())  {
                            FileOutput.writeToFile(outputPath + ".txt", "----------------", true, true);
                        }
                        else{
                            noZReportAnymore = true;
                            FileOutput.writeToFile(outputPath + ".txt", "----------------", true, false);
                        }
                    }

                    break;

                case "PRINT_VOYAGE":
                    // Check if there will be no more Z reports
                    if (noZReportAnymore == true){
                        FileOutput.writeToFile(outputPath + ".txt", "\n", true, false);
                    }
                    isReal = false;
                    FileOutput.writeToFile(outputPath + ".txt", "COMMAND: " + line, true, true);

                    if (parts.length != 2){
                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue;
                    }

                    int printVoyageId;
                    try{
                        printVoyageId = Integer.parseInt(parts[1]);
                        if (printVoyageId <= 0) {
                            FileOutput.writeToFile(outputPath + ".txt", "ERROR: " + printVoyageId + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
                            continue; // Skip processing the rest of the loop iteration
                        }
                    } catch (NumberFormatException e) {

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue; // Skip processing the rest of the loop iteration
                    }

                    if (!voyages.containsKey(printVoyageId)){

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: There is no voyage with ID of " + printVoyageId + "!", true, true);
                        continue;
                    }
                    // Get the voyage to print
                    Voyage printVoyage = voyages.get(printVoyageId);
                    if (printVoyage != null) {
                        // Initialize and update seating plan
                        printVoyage.iniSeatingPlan();
                        printVoyage.updateSeatingPlan();

                        FileOutput.writeToFile(outputPath + ".txt", printVoyage.printSeatingPlan(), true, true);

                    } else {

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Voyage with ID " + printVoyageId + " not found.", true, true);
                    }
                    break;

                case "SELL_TICKET":
                    // Check if there will be no more Z reports
                    if (noZReportAnymore == true){
                        FileOutput.writeToFile(outputPath + ".txt", "\n", true, false);
                    }
                    // COMMAND: SELL_TICKET	7	3
                    isReal = false;

                    FileOutput.writeToFile(outputPath + ".txt", "COMMAND: " + line, true, true);

                    if (parts.length != 3){

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue;
                    }

                    int voyageId;
                    try{
                        voyageId = Integer.parseInt(parts[1]);
                        if (voyageId <= 0) {

                            FileOutput.writeToFile(outputPath + ".txt", "ERROR: " + voyageId + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
                            continue; // Skip processing the rest of the loop iteration
                        }
                    } catch (NumberFormatException e) {

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue; // Skip processing the rest of the loop iteration
                    }

                    if (!voyages.containsKey(voyageId)){

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: There is no voyage with ID of " + voyageId + "!", true, true);
                        continue;
                    }
                    // Get the voyage to sell tickets
                    Voyage sellVoyage = voyages.get(voyageId);
                    String mergedString;
                    String seats;
                    String[] ticketArr;
                    String[] sellTicketArrTemp;
                    try {
                        seats = parts[2];
                        sellTicketArrTemp = seats.split("_");
                        mergedString = String.join("-", sellTicketArrTemp);
                        ticketArr = seats.split("_");

                        // Validate each ticket ID
                        for (int i = 0; i < ticketArr.length; i++) {
                            int ticketId = Integer.parseInt(ticketArr[i]);
                            if (ticketId <= 0) {
                                FileOutput.writeToFile(outputPath + ".txt", "ERROR: " + ticketId + " is not a positive integer, seat number must be a positive integer!", true, true);
                                continue outerLoop;
                            }
                            else if((ticketId > (sellVoyage.numberOfRows * sellVoyage.numberOfCol))){
                                FileOutput.writeToFile(outputPath + ".txt","ERROR: There is no such a seat!", true, true);
                                continue outerLoop;
                            }
                            else if (sellVoyage.sold.contains(Integer.toString(ticketId))) {
                                FileOutput.writeToFile(outputPath + ".txt", "ERROR: One or more seats already sold!", true, true);
                                continue outerLoop;
                            }
                        }
                    } catch (NumberFormatException e) {
                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue; // Skip processing the rest of the loop iteration
                    }
                    // Calculate revenue and update sold tickets
                    if (sellVoyage.type.equals("Standard") || sellVoyage.type.equals("Minibus")){

                        FileOutput.writeToFile(outputPath + ".txt",  "Seat " + mergedString + " of the Voyage " + voyageId +
                                " from " + sellVoyage.getFrom() + " to " + sellVoyage.getTo() + " was successfully sold for " +
                                String.format(Locale.ENGLISH,"%.2f", (sellVoyage.getPrice() * ticketArr.length)) + " TL.", true, true);
                        sellVoyage.calculateRevenue(ticketArr.length);
                        for (String i: ticketArr){
                            sellVoyage.addSoldTickets(i);
                        }
                    }
                    else{
                        int preNumber = 0;
                        int normalNumber = 0;
                        // Count premium and normal tickets
                        for (int i = 0; i < ticketArr.length; i++) {
                            if (Integer.parseInt(ticketArr[i]) % 3 == 1){
                                preNumber++;
                                sellVoyage.addSoldTickets(ticketArr[i]);
                            }
                            else{
                                normalNumber++;
                                sellVoyage.addSoldTickets(ticketArr[i]);
                            }
                        }
                        // Calculate revenue for premium and normal tickets
                        FileOutput.writeToFile(outputPath + ".txt", "Seat " + mergedString + " of the Voyage " + voyageId +
                                " from " + sellVoyage.getFrom() + " to " + sellVoyage.getTo() + " was successfully sold for " +
                                String.format(Locale.ENGLISH,"%.2f", ((sellVoyage.getPrice() * normalNumber) + (preNumber * sellVoyage.getPrice() * (100 + sellVoyage.getPrePercentage()) / 100) )) + " TL.", true, true);

                        sellVoyage.revenue +=  ((sellVoyage.getPrice() * normalNumber) + (preNumber * sellVoyage.getPrice() * (100 + sellVoyage.getPrePercentage()) / 100) );
                    }

                    break;

                case "REFUND_TICKET":
                    if (noZReportAnymore == true){
                        FileOutput.writeToFile(outputPath + ".txt", "\n", true, false);
                    }

                    isReal = false;

                    FileOutput.writeToFile(outputPath + ".txt", "COMMAND: " + line, true, true);

                    if (parts.length != 3){

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue;
                    }

                    int refundVoyageId;
                    try {
                        refundVoyageId = Integer.parseInt(parts[1]);
                        if (refundVoyageId <= 0) {

                            FileOutput.writeToFile(outputPath + ".txt", "ERROR: " + refundVoyageId + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
                            continue; // Skip processing the rest of the loop iteration
                        }
                    } catch (NumberFormatException e) {

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue; // Skip processing the rest of the loop iteration
                    }

                    if (!voyages.containsKey(refundVoyageId)){

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: There is no voyage with ID of " + refundVoyageId + "!", true, true);
                        continue;
                    }

                    Voyage refundVoyage = voyages.get(refundVoyageId);
                    if (refundVoyage.getType().equals("Minibus")) {

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Minibus tickets are not refundable!", true, true);
                        continue;
                    }

                    String mergedStrings;
                    String refundSeats;
                    String[] ticketAr;
                    String[] refundTicketArrTemp;

                    try {
                        refundSeats = parts[2];
                        refundTicketArrTemp = refundSeats.split("_");
                        mergedStrings = String.join("-", refundTicketArrTemp);
                        ticketAr = refundSeats.split("_");

                        for (int i = 0; i < ticketAr.length; i++) {
                            // ticketArr[i] this is an id
                            int ticketId = Integer.parseInt(ticketAr[i]);

                            if (ticketId <= 0) {

                                FileOutput.writeToFile(outputPath + ".txt", "ERROR: " + ticketId + " is not a positive integer, seat number must be a positive integer!", true, true);
                                continue outerLoop;
                            } else if (ticketId > (refundVoyage.numberOfRows * refundVoyage.numberOfCol)) {

                                FileOutput.writeToFile(outputPath + ".txt", "ERROR: There is no such a seat!", true, true);
                                continue outerLoop;
                            } else if (refundVoyage.sold.contains(Integer.toString(ticketId)) == false) {

                                FileOutput.writeToFile(outputPath + ".txt", "ERROR: One or more seats are already empty!", true, true);
                                continue outerLoop;
                            }
                        }

                    } catch (NumberFormatException e) {

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue; // Skip processing the rest of the loop iteration
                    }

                    if (refundVoyage != null) {

                        String[] refundTicketArr = refundSeats.split("_");
                        double refundAmount = refundVoyage.calculateRefund(refundTicketArr, refundTicketArr.length);
                        refundVoyage.subtractRevenue(refundAmount);
                        refundVoyage.removeSoldTickets(Arrays.asList(refundTicketArr));

                        FileOutput.writeToFile(outputPath + ".txt", "Seat " + mergedStrings + " of the Voyage " + refundVoyageId + " from " + refundVoyage.from +
                                " to " + refundVoyage.to + " was successfully refunded for " + String.format(Locale.ENGLISH,"%.2f", refundAmount) + " TL.", true, true);
                    } else {
                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Voyage with ID " + refundVoyageId + " not found.", true, true);
                    }
                    break;


                case "CANCEL_VOYAGE":
                    if (noZReportAnymore == true){
                        FileOutput.writeToFile(outputPath + ".txt", "\n", true, false);
                    }

                    isReal = false;

                    FileOutput.writeToFile(outputPath + ".txt", "COMMAND: " + line, true, true);
                    if (parts.length != 2){

                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue;
                    }

                    int cancelVoyageId;
                    try{
                        cancelVoyageId = Integer.parseInt(parts[1]);
                        if (cancelVoyageId <= 0) {

                            FileOutput.writeToFile(outputPath + ".txt", "ERROR: " + cancelVoyageId + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
                            continue; // Skip processing the rest of the loop iteration
                        }
                    } catch (NumberFormatException e) {
                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: Erroneous usage of \"" + whatToDo + "\" command!", true, true);
                        continue; // Skip processing the rest of the loop iteration
                    }

                    if (!voyages.containsKey(cancelVoyageId)){
                        FileOutput.writeToFile(outputPath + ".txt", "ERROR: There is no voyage with ID of " + cancelVoyageId + "!", true, true);
                        continue;
                    }

                    FileOutput.writeToFile(outputPath + ".txt", "Voyage " + cancelVoyageId + " was successfully cancelled!", true, true);
                    FileOutput.writeToFile(outputPath + ".txt", "Voyage details can be found below:", true, true);
                    // Get the voyage to cancel tickets
                    Voyage cancelVoyage = voyages.get(cancelVoyageId);
                    cancelVoyage.iniSeatingPlan();
                    cancelVoyage.updateSeatingPlan();
                    cancelVoyage.cancelVoyageAnd();

                    FileOutput.writeToFile(outputPath + ".txt", cancelVoyage.printSeatingPlan(), true, true);
                    voyages.remove(cancelVoyageId);
                    break;

                default:
                    FileOutput.writeToFile(outputPath + ".txt", "COMMAND: " + line, true, true);
                    FileOutput.writeToFile(outputPath + ".txt", "ERROR: There is no command namely " +  whatToDo + "!", true, true);
                    break;
            }
        }

        /*
        * CHECK IF LAST LINE NOT EQUAL TO Z REPORT
        * */
        if (content.length == 0 || isReal == false){


            if (voyages.size() == 0){

                FileOutput.writeToFile(outputPath + ".txt", "Z Report:", true, true);
                FileOutput.writeToFile(outputPath + ".txt", "----------------", true, true);
                FileOutput.writeToFile(outputPath + ".txt", "No Voyages Available!", true, true);
                FileOutput.writeToFile(outputPath + ".txt", "----------------", true, false);
            }
            else {
                FileOutput.writeToFile(outputPath + ".txt", "Z Report:", true, true);
                FileOutput.writeToFile(outputPath + ".txt", "----------------", true, true);
                for (Voyage voyageObj: voyages.values()){
                    voyageObj.iniSeatingPlan();
                    voyageObj.updateSeatingPlan();
                    FileOutput.writeToFile(outputPath + ".txt", voyageObj.printSeatingPlan(), true, true);
                    FileOutput.writeToFile(outputPath + ".txt", "----------------", true, false);
                }
            }

        }
    }
}