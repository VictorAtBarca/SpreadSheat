import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import Model.Cell;
import Model.Spreadsheet;
import Model.CellContents.FormulaContent;
import Model.CellContents.NumericalContent;
import Model.CellContents.TextContent; // Import TextContent


public class SpreadsheetManager { 
    public static void main(String[] args) {
        // Main menu loop
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        Spreadsheet spreadsheet = null; // Declare the spreadsheet object
        String folderPath = "./A-Spreadsheet";

        while (!exit) {

            System.out.println("\nMenu:");
            System.out.println("1. Create a Spreadsheet");
            System.out.println("2. Edit a Spreadsheet");
            System.out.println("3. Quit");
            System.out.print("Choose an option (1/2/3):");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    spreadsheet = new Spreadsheet(); 
                    editMenu(scanner, spreadsheet);
                    break;
                case 2:
                    String fileName = selectFile(scanner, folderPath);
                    spreadsheet = loadSpreadsheet(scanner,fileName);
                    editMenu(scanner,spreadsheet);
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }

        // Close the scanner
        scanner.close();
    }

    private static void editMenu(Scanner scanner, Spreadsheet spreadsheet) {
        boolean menu = true;
    
        while (menu) {
            System.out.print("\nEditspreadsheet :\n1. Add cell\n2. See Spreadsheet\n3. Save Spreadsheet\n4. Quit\nChoose an option (1/2/3/4) : ");
            
            if (scanner.hasNextInt()) {
                int userChoice = scanner.nextInt();
    
                switch (userChoice) {
                    case 1:
                        editSpreadsheet(scanner, spreadsheet);
                        break;
    
                    case 2:
                        // User chose to see the current spreadsheet
                        displaySpreadsheet(spreadsheet);
                        break;
    
                    case 3:
                        // User chose to save the spreadsheet
                        saveSpreadsheet(scanner, spreadsheet);
                        break;
    
                    case 4:
                        // User chose to quit (exit loop)
                        menu = false;
                        break;
    
                    default:
                        System.out.println("Invalid option. Please choose again.");
                }
            } else {
                System.out.println("Please enter an integer.");
                scanner.nextLine(); 
            }
        }
    }
   private static void saveSpreadsheet(Scanner scanner, Spreadsheet spreadsheet) {
        System.out.print("Enter the filename to save the spreadsheet: ");

        if (scanner.hasNext()) {
            String filename = scanner.next();

            try (FileWriter writer = new FileWriter( "A-Spreadsheet/" + filename)) {
                for (Cell cell : spreadsheet.getCells()) {
                    String  content;
                    
                    if (cell.getContent() instanceof FormulaContent) {
                        content = "1";
                    } else if (cell.getContent() instanceof NumericalContent) {
                        content = "2";
                    } else {
                        content = "3";
                    }
                    writer.write(cell.getCellKey() + "|" + cell.getContent().getValue() + "|" + content + "\n");
                }
                System.out.println("Spreadsheet saved successfully");
            } catch (IOException e) {
                System.out.println("Error saving the spreadsheet: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid input. Please enter a filename.");
        }
    }

    private static void displaySpreadsheet(Spreadsheet spreadsheet) {
        for (Cell cell : spreadsheet.getCells()) {
            Object result = cell.evaluateCell(spreadsheet);
            System.out.println(cell.getCellKey() + " : " + result);
        }
    }

    private static String selectFile(Scanner scanner, String folderPath) {
        boolean menu = true;
        String fileName = null;
    
        while (menu) {
            File folder = new File(folderPath);
            File[] files = folder.listFiles();
    
            if (files != null && files.length > 0) {
                int index = 1;
    
                for (File file : files) {
                    if (file.isFile()) {
                        System.out.println(index + " : " + file.getName());
                        index++;
                    }
                }
    
                System.out.println("Enter the number of the file you want to select:");
                
                // Check if the user's input is an integer
                if (scanner.hasNextInt()) {
                    int selectedFileIndex = scanner.nextInt();
    
                    if (selectedFileIndex >= 1 && selectedFileIndex <= files.length) {
                        File selectedFile = files[selectedFileIndex - 1];
                        fileName = selectedFile.getName();
                        break;
                    } else {
                        System.out.println("Invalid selection. Please enter a valid number.");
                    }
                } else {
                    // User did not enter an integer
                    System.out.println("Please enter an integer.");
                    scanner.nextLine(); // Consume the remaining newline
                }
            } else {
                System.out.println("No files found in '" + folderPath + "'.");
                break;
            }
        }
        return fileName;
    }

    private static Spreadsheet loadSpreadsheet(Scanner scanner, String fileName) {

    Spreadsheet spreadsheet = new Spreadsheet();

    try (BufferedReader reader = new BufferedReader(new FileReader("A-Spreadsheet/" + fileName))) {
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length == 3) {
                String cellKey = parts[0];
                String column = cellKey.substring(0, 1);
                int row = Character.getNumericValue(cellKey.charAt(1));

                String value = parts[1];
                String content = parts[2];
                Cell cell; 

                switch (content) {
                    case "1":
                        // FormulaContent
                        cell = new Cell(column, row, new FormulaContent(value));
                        break;
                    case "2":
                        // NumericalContent
                        double numericalValue = Double.parseDouble(value);
                        cell = new Cell(column, row, new NumericalContent(numericalValue));
                        break;
                    case "3":
                        // TextContent
                        cell = new Cell(column, row, new TextContent(value));
                        break;
                    default:
                        System.out.println("Unknown content type in the spreadsheet file.");
                        continue; // Skip the rest of the loop for invalid content types
                }

                spreadsheet.addCell(cell);
            } else {
                System.out.println("Invalid format in the spreadsheet file.");
            }
        }

        System.out.println("Spreadsheet read successfully from: " + fileName);
    } catch (IOException e) {
        System.out.println("Error reading the spreadsheet: " + e.getMessage());
    }
    return spreadsheet;
}

    private static void editSpreadsheet(Scanner scanner, Spreadsheet spreadsheet) {
        
        String column;
        do {
            System.out.print("Enter the column (e.g., A): ");
            column = scanner.next().toUpperCase(); // Convert to uppercase for consistency
            if (!isValidColumn(column)) {
                System.out.println("Invalid column. Please enter a single uppercase letter.");
            }
        } while (!isValidColumn(column));

        
        int row;
        do {
            System.out.print("Enter the row (e.g., 1): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid row. Please enter a valid integer.");
                System.out.print("Enter the row (e.g., 1): ");
                scanner.next(); // consume the invalid input
            }
            row = scanner.nextInt();
            if (row < 1) {
                System.out.println("Invalid row. Please enter a positive integer.");
            }
        } while (row < 1);

        System.out.print("Choose the cell content type (1: numerical, 2: text, 3: formula): ");
        int cellTypeChoice = scanner.nextInt();

        switch (cellTypeChoice) {

            case 1:

                System.out.print("Enter the numerical value of the cell: ");

                if (scanner.hasNextDouble()) {
                    double cellValue = scanner.nextDouble();
                    Cell numericalCell = new Cell(column, row, new NumericalContent(cellValue));
                    spreadsheet.addCell(numericalCell);
                    System.out.println("Numerical cell added successfully.");
                } else {
                    System.out.println("Invalid input. Please enter a numerical value.");
                    scanner.nextLine(); 
                }
                break;

            case 2:

                System.out.print("Enter the text value of the cell: ");

                if (scanner.hasNext()) {
                    String textValue = scanner.next();
                    Cell textCell = new Cell(column, row, new TextContent(textValue));
                    spreadsheet.addCell(textCell);
                    System.out.println("Text cell added successfully.");
                } else {
                    System.out.println("Invalid input. Please enter a non-empty text value.");
                    scanner.nextLine(); // Consume the remaining newline
                }
                break;
            
                case 3:

                    boolean validFormula = false;
                    String editCellKey = column + String.valueOf(row) ;

                    while (!validFormula) {

                        System.out.print("Enter the formula of the cell: ");
                        String formulaValue = scanner.next();

                        String[] listCellKeys = formulaValue.split("[+\\-*/]");

                        for (String cellKey : listCellKeys) {
                            if (!isValidCellReference(cellKey) && !isNumeric(cellKey)) {
                                System.out.println("Invalid formula. Each element in the formula must be a valid cell reference or a number.");
                                validFormula = false;
                                break;
                            } 
                        }

                        if (validFormula) {
                            validFormula = checkCellContents(editCellKey, listCellKeys, spreadsheet);
                        }
                        
                        // Add the cell only if the formula is valid
                        if (validFormula) {
                            Cell formulaCell = new Cell(column, row, new FormulaContent(formulaValue));
                            spreadsheet.addCell(formulaCell);
                            System.out.println("Formula cell added successfully.");
                        }
                    }
                    break;
                }
    }
    private static boolean checkCellContents(String editedCellKey, String[] listCellKeys, Spreadsheet spreadsheet) {

        boolean validFormula = true;

        for (String cellKey : listCellKeys) {

            Cell cell = spreadsheet.getCell(cellKey);
        
            if (cell == null) {
                System.out.println(cellKey + " is empty");
                return false;
            }

            if (cellKey.equals(editedCellKey)) {

                System.out.println("Circular Dependancy");
                validFormula = false;
                break;

            } 

            if (cell.getContent() instanceof FormulaContent) {

                String formulaValue = (String) spreadsheet.getCell(cellKey).getContent().getValue();
                System.out.println(formulaValue);
                String[] listCellKeysBis = formulaValue.split("[+\\-*/]");
                validFormula = checkCellContents(editedCellKey,listCellKeysBis, spreadsheet);

            } else if (cell.getContent() instanceof TextContent) {

                System.out.println(cellKey + " Error: Content is a String");
                validFormula = false;
                break;
            }
        }
        return validFormula;
    }
                              
    private static boolean isValidCellReference(String cellKey) {
        return cellKey.matches("[A-Z]\\d+");
    }
    
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidColumn(String column) {
        return column.length() == 1 && Character.isUpperCase(column.charAt(0));
    }
}

    