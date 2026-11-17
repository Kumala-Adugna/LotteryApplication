import java.util.*;
import java.io.*;

public class LotteryApplication {

    // ── Constants ─────────────────────────────────────────────────────────────
    static final int NUMBER_COUNT = 6;      // how many numbers each participant picks
    static final int MIN_NUMBER   = 1;      // lottery number range start
    static final int MAX_NUMBER   = 49;     // lottery number range end
    static final String DATA_FILE = "data/participants.txt";

    // ── Shared data structures ─────────────────────────────────────────────
    static HashMap<String, Participant> participants = new HashMap<>();
    static HashSet<Integer> winningNumbers           = new HashSet<>();
    static Scanner scanner                           = new Scanner(System.in);

    // ══════════════════════════════════════════════════════════════════════════
    //  MAIN
    // ══════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        loadFromFile();          // restore saved participants on startup
        int choice;

        do {
            printMenu();
            choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> registerParticipant();
                case 2 -> selectLotteryNumbers();
                case 3 -> generateWinningNumbers();
                case 4 -> identifyWinners();
                case 5 -> displayLotteryResults();
                case 6 -> searchParticipant();
                case 0 -> System.out.println("\nGoodbye!");
                default -> System.out.println("  [!] Invalid option. Try again.");
            }

        } while (choice != 0);

        scanner.close();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  MENU
    // ══════════════════════════════════════════════════════════════════════════
    static void printMenu() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║      LOTTERY APPLICATION     ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Register Participant     ║");
        System.out.println("║  2. Select Lottery Numbers   ║");
        System.out.println("║  3. Generate Winning Numbers ║");
        System.out.println("║  4. Identify Winners         ║");
        System.out.println("║  5. Display Lottery Results  ║");
        System.out.println("║  6. Search Participant       ║");
        System.out.println("║  0. Exit                     ║");
        System.out.println("╚══════════════════════════════╝");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  FEATURE 1: Participant Registration
    // ══════════════════════════════════════════════════════════════════════════
    static void registerParticipant() {
        System.out.println("\n── Register Participant ──");
        String id = readString("Enter Participant ID: ").toUpperCase();

        if (participants.containsKey(id)) {
            System.out.println("  [!] ID '" + id + "' already exists. Use a different ID.");
            return;
        }

        String name = readString("Enter Participant Name: ");
        Participant p = new Participant(id, name);
        participants.put(id, p);
        saveToFile();

        System.out.println("  [✓] Participant registered successfully!");
        System.out.println("      Now use option 2 to select lottery numbers for " + name + ".");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  FEATURE 2: Lottery Number Selection
    // ══════════════════════════════════════════════════════════════════════════
    static void selectLotteryNumbers() {
        System.out.println("\n── Lottery Number Selection ──");

        if (participants.isEmpty()) {
            System.out.println("  [!] No participants registered yet.");
            return;
        }

        String id = readString("Enter Participant ID: ").toUpperCase();
        Participant p = participants.get(id);

        if (p == null) {
            System.out.println("  [!] Participant not found.");
            return;
        }

        if (!p.getSelectedNumbers().isEmpty()) {
            System.out.println("  [!] " + p.getName() + " has already selected numbers: " + p.getSelectedNumbers());
            return;
        }

        System.out.println("  Select " + NUMBER_COUNT + " numbers between " + MIN_NUMBER + " and " + MAX_NUMBER + ".");

        while (p.getSelectedNumbers().size() < NUMBER_COUNT) {
            int remaining = NUMBER_COUNT - p.getSelectedNumbers().size();
            int num = readInt("  Enter number (" + remaining + " remaining): ");
            p.addNumber(num, MIN_NUMBER, MAX_NUMBER);
        }

        saveToFile();
        System.out.println("  [✓] Numbers saved: " + p.getSelectedNumbers());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  FEATURE 3: Winning Number Generation
    // ══════════════════════════════════════════════════════════════════════════
    static void generateWinningNumbers() {
        System.out.println("\n── Generate Winning Numbers ──");
        winningNumbers.clear();
        Random random = new Random();

        while (winningNumbers.size() < NUMBER_COUNT) {
            int num = random.nextInt(MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER;
            winningNumbers.add(num);   // HashSet ignores duplicates automatically
        }

        System.out.println("  [✓] Winning numbers generated: " + winningNumbers);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  FEATURE 4: Winner Identification
    // ══════════════════════════════════════════════════════════════════════════
    static void identifyWinners() {
        System.out.println("\n── Winner Identification ──");

        if (winningNumbers.isEmpty()) {
            System.out.println("  [!] Winning numbers not generated yet. Use option 3 first.");
            return;
        }

        if (participants.isEmpty()) {
            System.out.println("  [!] No participants registered.");
            return;
        }

        boolean anyWinner = false;

        for (Participant p : participants.values()) {
            // Count how many of participant's numbers match the winning set
            long matchCount = p.getSelectedNumbers().stream()
                               .filter(winningNumbers::contains)
                               .count();

            if (matchCount == NUMBER_COUNT) {
                p.setWinner(true);
                System.out.println("\n  🎉 WINNER: " + p.getName() + " (ID: " + p.getParticipantId() + ")");
                System.out.println("     Matched all " + NUMBER_COUNT + " numbers: " + p.getSelectedNumbers());
                anyWinner = true;
            } else {
                p.setWinner(false);
                System.out.println("  " + p.getName() + " matched " + matchCount + "/" + NUMBER_COUNT + " numbers.");
            }
        }

        if (!anyWinner) {
            System.out.println("\n  No winners this round.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  FEATURE 5: Lottery Results
    // ══════════════════════════════════════════════════════════════════════════
    static void displayLotteryResults() {
        System.out.println("\n── Lottery Results ──");

        if (winningNumbers.isEmpty()) {
            System.out.println("  [!] Winning numbers have not been generated yet.");
            return;
        }

        // Sort for display
        List<Integer> sorted = new ArrayList<>(winningNumbers);
        Collections.sort(sorted);
        System.out.println("  Winning Numbers : " + sorted);

        System.out.println("\n  All Participants:");
        System.out.println("  " + "─".repeat(50));

        for (Participant p : participants.values()) {
            p.displayParticipant();
            System.out.println("  " + "─".repeat(50));
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  FEATURE 6: Search Participant
    // ══════════════════════════════════════════════════════════════════════════
    static void searchParticipant() {
        System.out.println("\n── Search Participant ──");
        String id = readString("Enter Participant ID to search: ").toUpperCase();
        Participant p = participants.get(id);

        if (p == null) {
            System.out.println("  [!] No participant found with ID '" + id + "'.");
        } else {
            System.out.println("\n  Participant Found:");
            p.displayParticipant();
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  FILE HANDLING — Save participants to file
    // ══════════════════════════════════════════════════════════════════════════
    static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Participant p : participants.values()) {
                writer.write(p.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("  [!] Error saving to file: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  FILE HANDLING — Load participants from file on startup
    // ══════════════════════════════════════════════════════════════════════════
    static void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Format: P001,John Doe,5-17-23-34-41-49
                String[] parts = line.split(",", 3);
                if (parts.length < 2) continue;

                String id   = parts[0];
                String name = parts[1];
                Participant p = new Participant(id, name);

                if (parts.length == 3 && !parts[2].isEmpty()) {
                    for (String numStr : parts[2].split("-")) {
                        try {
                            p.getSelectedNumbers().add(Integer.parseInt(numStr.trim()));
                        } catch (NumberFormatException ignored) {}
                    }
                }

                participants.put(id, p);
            }
            if (!participants.isEmpty()) {
                System.out.println("  [✓] Loaded " + participants.size() + " participant(s) from file.");
            }

        } catch (IOException e) {
            System.out.println("  [!] Error reading file: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HELPER METHODS
    // ══════════════════════════════════════════════════════════════════════════
    static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid number.");
            }
        }
    }
}
