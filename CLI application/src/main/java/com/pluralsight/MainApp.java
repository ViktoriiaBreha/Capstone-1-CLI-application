package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainApp {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<TransactionElements> transactions = new ArrayList<>();

        System.out.println("----Hi! Welcome to our CLI application----");
        System.out.println(" ");

        while (true) {
            System.out.println("Please choose from options below: ");
            System.out.println("(D) Add Deposit");
            System.out.println("(P) Make Payment (Debit)");
            System.out.println("(L) Ledger");
            System.out.println("(X) Exit");
            System.out.print("Enter your choice here: ");
            String menu_choice = scanner.nextLine();


            switch (menu_choice.toUpperCase()) {
                case "D":
                    addDeposit(scanner, transactions);
                    break;
                case "P":
                    makePayment(scanner, transactions);
                    break;
                case "L":
                    displayLedger(scanner, transactions);
                    break;
                case "X":
                    int i;
                    for (i = 3; i >= 1; i--) {
                        System.out.println(i);
                        Thread.sleep(1000);
                    }
                    System.out.println("Thank you for using our application! See you soon!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid selection, please choose again");

            }
        }


    }

    public static void addDeposit(Scanner scanner, ArrayList<TransactionElements> transactions) {
        System.out.println("---Add Deposit---");
        System.out.println(" ");
        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/transaction.csv", true);
            BufferedWriter bufWriter = new BufferedWriter(fileWriter);
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            String formatting_date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String formatting_time = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            System.out.println("Enter all deposit information below: ");
            System.out.print("Enter description of your deposit:");
            String dep_description = scanner.nextLine();
            System.out.print("Enter the name of vendor: ");
            String dep_vendor = scanner.nextLine();
            System.out.print("Enter amount of deposit: ");
            double dep_amount = scanner.nextDouble();
            scanner.nextLine();

            if (dep_amount > 0) {
                TransactionElements dep_transaction = new TransactionElements(date, time, dep_description, dep_vendor, dep_amount);
                bufWriter.write(String.format("%s | %s | %s | %s | $ %.2f", formatting_date, formatting_time,
                        dep_description, dep_vendor, dep_amount));
                bufWriter.newLine();
                System.out.println("Your deposit information was added successfully!");

            } else {
                System.out.println("Wrong number, try again!");
            }
            bufWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void makePayment(Scanner scanner, ArrayList<TransactionElements> transactions) {
        System.out.println("---Make Payment (Deposit)---");
        System.out.println(" ");
        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/transaction.csv", true);
            BufferedWriter bufWriter = new BufferedWriter(fileWriter);
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            String formatting_date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String formatting_time = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            System.out.println("Enter all payment information below: ");
            System.out.print("Enter description of your payment: ");
            String pay_description = scanner.nextLine();
            System.out.print("Enter the name of vendor: ");
            String pay_vendor = scanner.nextLine();
            System.out.print("Enter amount of payment: ");
            double pay_amount = scanner.nextDouble();
            scanner.nextLine();

            if (pay_amount > 0) {
                pay_amount = -pay_amount;
                TransactionElements pay_transaction = new TransactionElements(date, time, pay_description, pay_vendor, pay_amount);
                bufWriter.write(String.format("%s | %s | %s | %s | $ %.2f", formatting_date, formatting_time,
                        pay_description, pay_vendor, pay_amount));
                bufWriter.newLine();
                System.out.println("Your payment information was added successfully!");
            } else {
                System.out.println("Wrong number, try again!");
            }
            bufWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<TransactionElements> readTransactions() {
        ArrayList<TransactionElements> transactions = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("src/main/resources/transaction.csv");
            BufferedReader bufReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufReader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(Pattern.quote("|").trim());
                LocalDate date = LocalDate.parse(parts[0].trim());
                LocalTime time = LocalTime.parse(parts[1].trim());
                String description = parts[2].trim();
                String vendor = parts[3].trim();
                double amount = Double.parseDouble(parts[4].trim());

                TransactionElements transaction = new TransactionElements(date, time, description, vendor, amount);
                transactions.add(transaction);


            }
            bufReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactions;


    }

    public static void displayLedger(Scanner scanner, ArrayList<TransactionElements> transactions) throws InterruptedException {
        System.out.println("---Ledger---");
        System.out.println(" ");

        boolean run = true;

        while (run) {
            System.out.println("Choose below what do you like to display: ");
            System.out.println("(A) All");
            System.out.println("(D) Deposits");
            System.out.println("(P) Payments");
            System.out.println("(R) Reports");
            System.out.println("(H) Home");
            System.out.print("Enter your choice here: ");
            String choice_ledger = scanner.nextLine().trim();

            ArrayList<TransactionElements> transactionHistory = readTransactions();
            Collections.reverse(transactionHistory);

            switch (choice_ledger.toUpperCase().trim()) {
                case "A":
                    System.out.println("Your all entries: ");
                    System.out.println("date | time | description | vendor | amount");
                    for (TransactionElements tran : transactionHistory) {
                        System.out.println(tran);
                    }
                    break;

                case "D":
                    System.out.println("Your all deposits: ");
                    System.out.println("date | time | description | vendor | amount");
                    for (TransactionElements tran : transactionHistory) {
                        if (tran.getAmount() > 0) {
                            System.out.println(tran);
                        }
                    }
                    break;
                case "P":
                    System.out.println("Your all payments: ");
                    System.out.println("date | time | description | vendor | amount");
                    for (TransactionElements tran : transactionHistory) {
                        if (tran.getAmount() < 0) {
                            System.out.println(tran);
                        }
                    }
                    break;
                case "R":

                    boolean report_menu = true;
                    while (report_menu) {
                        System.out.println("What reports do you want to look on?");
                        System.out.println("1) Month To Date");
                        System.out.println("2) Previous Month");
                        System.out.println("3) Year to Date");
                        System.out.println("4) Previous Year");
                        System.out.println("5) Search by Vendor");
                        System.out.println("0) Back");
                        System.out.print("Your number choice: ");
                        int number_choice = scanner.nextInt();
                        scanner.nextLine();


                        LocalDate today = LocalDate.now();
// Interesting part
                        switch (number_choice) {
                            case 1:
                                System.out.println("Month To Date report: ");
                                System.out.println("date | time | description | vendor | amount");
                                boolean check = false;
                                for (TransactionElements tran : transactionHistory) {
                                    if (tran.getDate().getMonth() == today.getMonth() && tran.getDate().getYear() == today.getYear()) {
                                        System.out.println(tran);
                                        check = true;
                                    }
                                }
                                if (!check) {
                                    System.out.println("Information is not found");
                                }
                                break;
                            case 2:
                                System.out.println("Previous Month report: ");
                                System.out.println("date | time | description | vendor | amount");
                                LocalDate today_day = today.withDayOfMonth(1);
                                LocalDate last_month_day = today_day.minusMonths(1);
                                boolean check1 = false;
                                for (TransactionElements tran : transactionHistory) {
                                    if (tran.getDate().getMonth() == last_month_day.getMonth() && tran.getDate().getYear() == last_month_day.getYear()) {
                                        System.out.println(tran);
                                        check1 = true;
                                    }
                                }
                                if (!check1) {
                                    System.out.println("Information is not found");
                                }


                                break;
                            case 3:
                                System.out.println("Year To Date report: ");
                                System.out.println("date | time | description | vendor | amount");
                                boolean check2 = false;
                                for (TransactionElements tran : transactionHistory) {
                                    if (tran.getDate().getYear() == today.getYear()) {
                                        System.out.println(tran);
                                        check2 = true;
                                    }
                                }
                                if (!check2) {
                                    System.out.println("Information is not found");
                                }
                                break;
                            case 4:
                                System.out.println("Previous Year report: ");
                                System.out.println("date | time | description | vendor | amount");
                                boolean check3 = false;
                                for (TransactionElements tran : transactionHistory) {
                                    if (tran.getDate().getYear() == today.getYear() - 1) {
                                        System.out.println(tran);
                                        check3 = true;
                                    }
                                }
                                if (!check3) {
                                    System.out.println("Information is not found");
                                }
                                break;
                            case 5:
                                System.out.println("Search by Vendor: ");
                                System.out.println("date | time | description | vendor | amount");
                                System.out.print("Enter name of vendor: ");
                                String vendor_search = scanner.nextLine().trim();
                                boolean check4 = false;
                                for (TransactionElements tran : transactionHistory) {
                                    if (tran.getVendor().equalsIgnoreCase(vendor_search)) {
                                        System.out.println(tran);
                                        check4 = true;
                                    }
                                }
                                if (!check4) {
                                    System.out.println("Information is not found");
                                }
                                break;

                            case 0:
                                report_menu = false;
                                System.out.println("Returning back");
                                break;
                            default:
                                System.out.println("Invalid selection, please try again");
                                break;
                        }
                    }

                    break;

                case "H":
                    System.out.println("Returning to the main menu...");
                    int i;
                    for (i = 3; i >= 1; i--) {
                        System.out.println(i);
                        Thread.sleep(1000);
                    }
                    run = false;
                    break;
                default:
                    System.out.println("Invalid selection, please choose again");
                    break;

            }

        }
    }


}



