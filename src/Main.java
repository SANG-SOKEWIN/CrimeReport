import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class CrimeReport {
    private static Scanner sc = new Scanner(System.in);
    private static String currentUser = "";
    private static boolean isPolice = false;

    public static void main(String[] args) {
        int loginChoice = 0;
        boolean validLogin = false;

        while (!validLogin) {
            try {
                System.out.println("===== CRIME REPORT MANAGEMENT SYSTEM =====");
                System.out.println("Login as:");
                System.out.println("1 for User");
                System.out.println("2 for Police");
                System.out.println("3 for User Registration");
                System.out.println("4 to Exit");
                System.out.print("Enter your choice: ");
                loginChoice = sc.nextInt();
                sc.nextLine();

                if (loginChoice == 1) {
                    userLogin();
                    validLogin = true;
                    isPolice = false;
                } else if (loginChoice == 2) {
                    policeLogin();
                    validLogin = true;
                    isPolice = true;
                } else if (loginChoice == 3) {
                    registerUser();
                } else if (loginChoice == 4) {
                    System.out.println("Exiting the system. Goodbye!");
                    System.exit(0);
                } else {
                    System.out.println("\nInvalid choice\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input\n");
                sc.nextLine();
            }
        }

        if (loginChoice == 1) {
            System.out.println("\nWelcome User: " + currentUser);
            userMenu();
        } else if (loginChoice == 2) {
            System.out.println("\nWelcome Police Officer: " + currentUser);
            policeMenu();
        }

        sc.close();
    }

    static void userLogin() {
        boolean correctLogin = false;
        while (!correctLogin) {
            try {
                System.out.println();
                System.out.print("Enter National ID: ");
                String enteredUsername = sc.nextLine();
                System.out.print("Enter Password: ");
                String enteredPassword = sc.nextLine();

                File file = new File("userLogin.txt");
                Scanner fileScanner = new Scanner(file);

                boolean validLogin = false;

                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String username = parts[0];
                        String password = parts[1];
                        if (enteredUsername.equals(username) && enteredPassword.equals(password)) {
                            validLogin = true;
                            currentUser = username;
                            break;
                        }
                    }
                }
                if (validLogin) {
                    System.out.println("Login successful.");
                    correctLogin = true;
                } else {
                    System.out.println("Wrong username or password.");
                }

                fileScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("User login file not found.");
                createFile("userLogin.txt");
                System.out.println("Created a new user login file. Please register first.");
                break;
            }
        }
    }

    static void policeLogin() {
        boolean correctLogin = false;
        while (!correctLogin) {
            try {
                System.out.println();
                System.out.print("Enter Police ID: ");
                String enteredUsername = sc.nextLine();
                System.out.print("Enter Password: ");
                String enteredPassword = sc.nextLine();

                File file = new File("policeLogin.txt");
                Scanner fileScanner = new Scanner(file);

                boolean validLogin = false;

                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String username = parts[0];
                        String password = parts[1];
                        if (enteredUsername.equals(username) && enteredPassword.equals(password)) {
                            validLogin = true;
                            currentUser = username;
                            break;
                        }
                    }
                }
                if (validLogin) {
                    System.out.println("Login successful.");
                    correctLogin = true;
                } else {
                    System.out.println("Invalid username or password.");
                }

                fileScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("Police login file not found.");
                createFile("policeLogin.txt");
                System.out.println("Created a new police login file. Please contact administrator to add credentials.");
                break;
            }
        }
    }

    static void registerUser() {
        try {
            System.out.println("\n===== USER REGISTRATION =====");
            System.out.print("Enter National ID: ");
            String nationalId = sc.nextLine();

            // Check if user already exists
            if (userExists(nationalId)) {
                System.out.println("A user with this National ID already exists.");
                return;
            }

            System.out.print("Enter Password: ");
            String password = sc.nextLine();
            System.out.print("Enter Phone Number: ");
            String phoneNumber = sc.nextLine();
            System.out.print("Enter Full Name: ");
            String fullName = sc.nextLine();

            // Save login credentials
            try (FileWriter fw = new FileWriter("userLogin.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(nationalId + ":" + password);
            } catch (IOException e) {
                System.out.println("Error saving login credentials: " + e.getMessage());
                return;
            }

            // Save user details
            try (FileWriter fw = new FileWriter("userDetails.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(nationalId + "," + fullName + "," + phoneNumber);
            } catch (IOException e) {
                System.out.println("Error saving user details: " + e.getMessage());
                return;
            }

            System.out.println("Registration successful! You can now login.");
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    private static boolean userExists(String nationalId) {
        try {
            File file = new File("userLogin.txt");
            if (!file.exists()) {
                return false;
            }

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length >= 1 && parts[0].equals(nationalId)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return false;
    }

    // User Menu
    static void userMenu() {
        boolean exit = false;
        while (!exit) {
            try {
                System.out.println("\n===== USER MENU =====");
                System.out.println("1. Report a Crime");
                System.out.println("2. View My Reports");
                System.out.println("3. Track Case Status");
                System.out.println("4. Logout");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        reportCrime();
                        break;
                    case 2:
                        viewMyReports();
                        break;
                    case 3:
                        trackCaseStatus();
                        break;
                    case 4:
                        exit = true;
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Consume invalid input
            }
        }
    }

    // Police Menu
    static void policeMenu() {
        boolean exit = false;
        while (!exit) {
            try {
                System.out.println("\n===== POLICE OFFICER MENU =====");
                System.out.println("1. View All Crime Reports");
                System.out.println("2. Update Case Status");
                System.out.println("3. Search Case by ID");
                System.out.println("4. Generate Reports");
                System.out.println("5. Logout");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewAllReports();
                        break;
                    case 2:
                        updateCaseStatus();
                        break;
                    case 3:
                        searchCaseById();
                        break;
                    case 4:
                        generateReports();
                        break;
                    case 5:
                        exit = true;
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Consume invalid input
            }
        }
    }

    // User Functions
    static void reportCrime() {
        try {
            System.out.println("\n===== REPORT A CRIME =====");

            // Generate a unique report ID
            String reportId = generateReportId();

            System.out.println("Report ID: " + reportId);

            System.out.println("Select Crime Type:");
            System.out.println("1. Theft");
            System.out.println("2. Assault");
            System.out.println("3. Vandalism");
            System.out.println("4. Hit and Run");
            System.out.println("5. Other");
            System.out.print("Enter your choice (1-5): ");
            int crimeTypeChoice = sc.nextInt();
            sc.nextLine();

            String crimeType = "";
            switch (crimeTypeChoice) {
                case 1: crimeType = "Theft"; break;
                case 2: crimeType = "Assault"; break;
                case 3: crimeType = "Vandalism"; break;
                case 4: crimeType = "Hit and Run"; break;
                case 5:
                    System.out.print("Specify crime type: ");
                    crimeType = sc.nextLine();
                    break;
                default: crimeType = "Unspecified"; break;
            }

            System.out.print("Enter location of the crime: ");
            String location = sc.nextLine();

            System.out.print("Enter date of the crime (DD/MM/YYYY): ");
            String date = sc.nextLine();

            System.out.print("Enter time of the crime (HH:MM): ");
            String time = sc.nextLine();

            System.out.println("Enter a detailed description of the crime:");
            String description = sc.nextLine();

            System.out.print("Do you have any evidence to report? (y/n): ");
            String hasEvidence = sc.nextLine().toLowerCase();

            String evidence = "None";
            if (hasEvidence.equals("y") || hasEvidence.equals("yes")) {
                System.out.println("Enter evidence details (e.g., plate numbers, images description, witness info):");
                evidence = sc.nextLine();
            }

            // Get current timestamp
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String reportTimestamp = now.format(formatter);

            // Save the report
            String reportStatus = "New";
            String reportData = reportId + "," + currentUser + "," + crimeType + "," + location + "," +
                    date + "," + time + "," + description + "," + evidence + "," +
                    reportStatus + "," + reportTimestamp + "," + "-";

            try (FileWriter fw = new FileWriter("crimeReports.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(reportData);
                System.out.println("\nYour crime report has been successfully submitted!");
                System.out.println("Report ID: " + reportId);
                System.out.println("Please keep this ID for tracking your case status.");
            } catch (IOException e) {
                System.out.println("Error saving crime report: " + e.getMessage());
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid input format. Please try again.");
            sc.nextLine(); // Consume invalid input
        }
    }

    static void viewMyReports() {
        try {
            System.out.println("\n===== MY REPORTS =====");

            File file = new File("crimeReports.txt");
            if (!file.exists()) {
                System.out.println("No reports found.");
                return;
            }

            Scanner fileScanner = new Scanner(file);
            boolean foundReports = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 11 && parts[1].equals(currentUser)) {
                    foundReports = true;
                    System.out.println("\nReport ID: " + parts[0]);
                    System.out.println("Crime Type: " + parts[2]);
                    System.out.println("Location: " + parts[3]);
                    System.out.println("Date: " + parts[4]);
                    System.out.println("Status: " + parts[8]);
                    System.out.println("Submitted on: " + parts[9]);

                    if (!parts[10].equals("-")) {
                        System.out.println("Police Notes: " + parts[10]);
                    }

                    System.out.println("--------------------------------");
                }
            }

            fileScanner.close();

            if (!foundReports) {
                System.out.println("You have not submitted any crime reports yet.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("No reports found.");
        } catch (Exception e) {
            System.out.println("Error viewing reports: " + e.getMessage());
        }
    }

    static void trackCaseStatus() {
        try {
            System.out.println("\n===== TRACK CASE STATUS =====");
            System.out.print("Enter Report ID: ");
            String reportId = sc.nextLine();

            File file = new File("crimeReports.txt");
            if (!file.exists()) {
                System.out.println("No reports found.");
                return;
            }

            Scanner fileScanner = new Scanner(file);
            boolean foundReport = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 11 && parts[0].equals(reportId)) {
                    // Verify this report belongs to the current user
                    if (!parts[1].equals(currentUser) && !isPolice) {
                        System.out.println("You do not have permission to view this report.");
                        fileScanner.close();
                        return;
                    }

                    foundReport = true;
                    System.out.println("\nReport ID: " + parts[0]);
                    System.out.println("Submitted by: " + parts[1]);
                    System.out.println("Crime Type: " + parts[2]);
                    System.out.println("Location: " + parts[3]);
                    System.out.println("Date: " + parts[4]);
                    System.out.println("Time: " + parts[5]);
                    System.out.println("Description: " + parts[6]);
                    System.out.println("Evidence: " + parts[7]);
                    System.out.println("Status: " + parts[8]);
                    System.out.println("Submitted on: " + parts[9]);

                    if (!parts[10].equals("-")) {
                        System.out.println("Police Notes: " + parts[10]);
                    }

                    break;
                }
            }

            fileScanner.close();

            if (!foundReport) {
                System.out.println("Report not found. Please verify the Report ID.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("No reports found.");
        } catch (Exception e) {
            System.out.println("Error tracking case: " + e.getMessage());
        }
    }

    // Police Functions
    static void viewAllReports() {
        try {
            System.out.println("\n===== ALL CRIME REPORTS =====");

            File file = new File("crimeReports.txt");
            if (!file.exists()) {
                System.out.println("No reports found.");
                return;
            }

            Scanner fileScanner = new Scanner(file);
            boolean foundReports = false;
            int count = 0;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 11) {
                    foundReports = true;
                    count++;

                    System.out.println("\nReport #" + count);
                    System.out.println("Report ID: " + parts[0]);
                    System.out.println("Submitted by: " + parts[1]);
                    System.out.println("Crime Type: " + parts[2]);
                    System.out.println("Location: " + parts[3]);
                    System.out.println("Date/Time: " + parts[4] + " " + parts[5]);
                    System.out.println("Status: " + parts[8]);
                    System.out.println("Submitted on: " + parts[9]);
                    System.out.println("--------------------------------");
                }
            }

            fileScanner.close();

            if (!foundReports) {
                System.out.println("No crime reports found in the system.");
            } else {
                System.out.println("\nTotal Reports: " + count);
            }

        } catch (FileNotFoundException e) {
            System.out.println("No reports found.");
        } catch (Exception e) {
            System.out.println("Error viewing reports: " + e.getMessage());
        }
    }

    static void updateCaseStatus() {
        try {
            System.out.println("\n===== UPDATE CASE STATUS =====");
            System.out.print("Enter Report ID: ");
            String reportId = sc.nextLine();

            File inputFile = new File("crimeReports.txt");
            File tempFile = new File("temp.txt");

            if (!inputFile.exists()) {
                System.out.println("No reports found.");
                return;
            }

            Scanner fileScanner = new Scanner(inputFile);
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

            boolean foundReport = false;
            String reportLine = "";

            // Find the report
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 11 && parts[0].equals(reportId)) {
                    foundReport = true;
                    reportLine = line;

                    System.out.println("\nCurrent Report Details:");
                    System.out.println("Report ID: " + parts[0]);
                    System.out.println("Submitted by: " + parts[1]);
                    System.out.println("Crime Type: " + parts[2]);
                    System.out.println("Location: " + parts[3]);
                    System.out.println("Current Status: " + parts[8]);
                } else {
                    writer.println(line);
                }
            }

            fileScanner.close();

            if (!foundReport) {
                writer.close();
                tempFile.delete();
                System.out.println("Report not found. Please verify the Report ID.");
                return;
            }

            // Update status
            System.out.println("\nSelect new status:");
            System.out.println("1. Seen");
            System.out.println("2. Under Investigation");
            System.out.println("3. Investigated");
            System.out.println("4. Reported to Headquarters");
            System.out.println("5. Closed");
            System.out.print("Enter your choice (1-5): ");
            int statusChoice = sc.nextInt();
            sc.nextLine(); // Consume newline

            String newStatus = "";
            switch (statusChoice) {
                case 1: newStatus = "Seen"; break;
                case 2: newStatus = "Under Investigation"; break;
                case 3: newStatus = "Investigated"; break;
                case 4: newStatus = "Reported to Headquarters"; break;
                case 5: newStatus = "Closed"; break;
                default: newStatus = "Unknown"; break;
            }

            System.out.print("Add police notes: ");
            String policeNotes = sc.nextLine();

            // Update the report line
            String[] reportParts = reportLine.split(",");
            reportParts[8] = newStatus;
            reportParts[10] = policeNotes;

            // Reconstruct the line
            StringBuilder newLine = new StringBuilder();
            for (int i = 0; i < reportParts.length; i++) {
                newLine.append(reportParts[i]);
                if (i < reportParts.length - 1) {
                    newLine.append(",");
                }
            }

            writer.println(newLine.toString());
            writer.close();

            // Replace the old file
            if (inputFile.delete()) {
                if (!tempFile.renameTo(inputFile)) {
                    System.out.println("Error updating file.");
                    return;
                }
            } else {
                System.out.println("Error updating file.");
                return;
            }

            System.out.println("\nCase status successfully updated to: " + newStatus);

        } catch (FileNotFoundException e) {
            System.out.println("No reports found.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            sc.nextLine(); // Consume invalid input
        } catch (Exception e) {
            System.out.println("Error updating case status: " + e.getMessage());
        }
    }

    static void searchCaseById() {
        try {
            System.out.println("\n===== SEARCH CASE BY ID =====");
            System.out.print("Enter Report ID: ");
            String reportId = sc.nextLine();

            File file = new File("crimeReports.txt");
            if (!file.exists()) {
                System.out.println("No reports found.");
                return;
            }

            Scanner fileScanner = new Scanner(file);
            boolean foundReport = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 11 && parts[0].equals(reportId)) {
                    foundReport = true;

                    System.out.println("\nReport Details:");
                    System.out.println("Report ID: " + parts[0]);
                    System.out.println("Submitted by: " + parts[1]);
                    System.out.println("Crime Type: " + parts[2]);
                    System.out.println("Location: " + parts[3]);
                    System.out.println("Date: " + parts[4]);
                    System.out.println("Time: " + parts[5]);
                    System.out.println("Description: " + parts[6]);
                    System.out.println("Evidence: " + parts[7]);
                    System.out.println("Status: " + parts[8]);
                    System.out.println("Submitted on: " + parts[9]);

                    if (!parts[10].equals("-")) {
                        System.out.println("Police Notes: " + parts[10]);
                    }

                    break;
                }
            }

            fileScanner.close();

            if (!foundReport) {
                System.out.println("Report not found. Please verify the Report ID.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("No reports found.");
        } catch (Exception e) {
            System.out.println("Error searching case: " + e.getMessage());
        }
    }

    static void generateReports() {
        try {
            System.out.println("\n===== GENERATE REPORTS =====");
            System.out.println("Select Report Type:");
            System.out.println("1. Crime Statistics by Type");
            System.out.println("2. Case Status Summary");
            System.out.println("3. Monthly Activity Report");
            System.out.print("Enter your choice (1-3): ");
            int reportChoice = sc.nextInt();
            sc.nextLine(); // Consume newline

            File file = new File("crimeReports.txt");
            if (!file.exists()) {
                System.out.println("No data available for reporting.");
                return;
            }

            Scanner fileScanner = new Scanner(file);
            Map<String, Integer> crimeTypeCount = new HashMap<>();
            Map<String, Integer> statusCount = new HashMap<>();
            Map<String, Integer> monthlyCount = new HashMap<>();
            int totalReports = 0;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 11) {
                    totalReports++;

                    // Count by crime type
                    String crimeType = parts[2];
                    crimeTypeCount.put(crimeType, crimeTypeCount.getOrDefault(crimeType, 0) + 1);

                    // Count by status
                    String status = parts[8];
                    statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);

                    // Count by month (from submission date)
                    String dateStr = parts[9]; // Format: yyyy-MM-dd HH:mm:ss
                    String month = dateStr.substring(0, 7); // Extract YYYY-MM
                    monthlyCount.put(month, monthlyCount.getOrDefault(month, 0) + 1);
                }
            }

            fileScanner.close();

            if (totalReports == 0) {
                System.out.println("No reports found for analysis.");
                return;
            }

            switch (reportChoice) {
                case 1:
                    System.out.println("\n===== CRIME STATISTICS BY TYPE =====");
                    System.out.println("Total Reports: " + totalReports);
                    System.out.println("\nBreakdown by Crime Type:");

                    for (Map.Entry<String, Integer> entry : crimeTypeCount.entrySet()) {
                        double percentage = (entry.getValue() * 100.0) / totalReports;
                        System.out.printf("%s: %d reports (%.1f%%)\n",
                                entry.getKey(), entry.getValue(), percentage);
                    }
                    break;

                case 2:
                    System.out.println("\n===== CASE STATUS SUMMARY =====");
                    System.out.println("Total Reports: " + totalReports);
                    System.out.println("\nBreakdown by Status:");

                    for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
                        double percentage = (entry.getValue() * 100.0) / totalReports;
                        System.out.printf("%s: %d reports (%.1f%%)\n",
                                entry.getKey(), entry.getValue(), percentage);
                    }
                    break;

                case 3:
                    System.out.println("\n===== MONTHLY ACTIVITY REPORT =====");
                    System.out.println("Total Reports: " + totalReports);
                    System.out.println("\nBreakdown by Month:");

                    List<Map.Entry<String, Integer>> monthlyList = new ArrayList<>(monthlyCount.entrySet());
                    monthlyList.sort(Map.Entry.comparingByKey());

                    for (Map.Entry<String, Integer> entry : monthlyList) {
                        System.out.printf("%s: %d reports\n", entry.getKey(), entry.getValue());
                    }
                    break;

                default:
                    System.out.println("Invalid report type selection.");
                    break;
            }

        } catch (FileNotFoundException e) {
            System.out.println("No reports found.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }

    // Utility Functions
    private static void createFile(String fileName) {
        try {
            File file = new File(fileName);
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    private static String generateReportId() {
        // Format: CR-YYYYMMDD-XXXX where XXXX is a random number
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = now.format(formatter);

        // Generate random 4-digit number
        int randomNum = (int) (Math.random() * 9000) + 1000;

        return "CR-" + dateStr + "-" + randomNum;
    }
}