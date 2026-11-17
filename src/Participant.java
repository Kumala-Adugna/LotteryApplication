/**
 * Advanced Programming — Lab Assignment I
 * Adama Science and Technology University
 *
 * Group Members:
 *   1. Kumala Adugna     — Ugr/37298/17
 *   2. Shimelis Regassa  — Ugr/37774/17
 *   3. Ziyad Areba       — Ugr/38058/17
 */

import java.util.ArrayList;

public class Participant {

    // ── Attributes ────────────────────────────────────────────────────────────
    private String participantId;
    private String name;
    private ArrayList<Integer> selectedNumbers;
    private boolean isWinner;

    // ── Constructor ───────────────────────────────────────────────────────────
    public Participant(String participantId, String name) {
        this.participantId   = participantId;
        this.name            = name;
        this.selectedNumbers = new ArrayList<>();
        this.isWinner        = false;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getParticipantId() { return participantId; }
    public String getName()          { return name; }
    public ArrayList<Integer> getSelectedNumbers() { return selectedNumbers; }
    public boolean isWinner()        { return isWinner; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setWinner(boolean winner) { this.isWinner = winner; }

    // ── Add a lottery number (validates range + no duplicates) ─────────────
    /**
     * Returns true  → number was added successfully
     * Returns false → rejected (duplicate or out of range)
     */
    public boolean addNumber(int number, int min, int max) {
        if (number < min || number > max) {
            System.out.println("  [!] " + number + " is out of range (" + min + "–" + max + "). Try again.");
            return false;
        }
        if (selectedNumbers.contains(number)) {
            System.out.println("  [!] " + number + " is already selected. Choose a different number.");
            return false;
        }
        selectedNumbers.add(number);
        return true;
    }

    // ── Display participant details ────────────────────────────────────────
    public void displayParticipant() {
        System.out.println("  ID     : " + participantId);
        System.out.println("  Name   : " + name);
        System.out.println("  Numbers: " + selectedNumbers);
        System.out.println("  Winner : " + (isWinner ? "YES 🎉" : "No"));
    }

    // ── toString (used when saving to file) ───────────────────────────────
    @Override
    public String toString() {
        // Format:  P001,John Doe,5-17-23-34-41-49
        StringBuilder nums = new StringBuilder();
        for (int i = 0; i < selectedNumbers.size(); i++) {
            nums.append(selectedNumbers.get(i));
            if (i < selectedNumbers.size() - 1) nums.append("-");
        }
        return participantId + "," + name + "," + nums;
    }
}
