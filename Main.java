import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

// Main Application Class
public class Main {
    public static void main(String[] args) {
        // Initialize User Profile
        UserProfile userProfile = new UserProfile("John Doe", 30, 75.0);
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println("Welcome to the Fitness Tracking Application!");

        // Application Menu Loop
        while (isRunning) {
            displayMainMenu();
            int userChoice = getUserChoice(scanner);

            switch (userChoice) {
                case 1 -> addExercise(userProfile, scanner);
                case 2 -> userProfile.displayExerciseHistory();
                case 3 -> {
                    System.out.println("Exiting application. Thank you for using Fitness Tracking App!");
                    isRunning = false;
                }
                default -> System.out.println("Invalid choice. Please select a valid option.");
            }
        }
        scanner.close();
    }

    // Method to display the main menu
    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Add Exercise");
        System.out.println("2. View Exercise History");
        System.out.println("3. Exit");
    }

    // Method to get user choice and handle invalid input
    private static int getUserChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1; // Return an invalid choice
        }
    }

    // Method to handle adding an exercise
    private static void addExercise(UserProfile userProfile, Scanner scanner) {
        System.out.println("\nSelect Exercise Type:");
        System.out.println("1. Cardio Exercise");
        System.out.println("2. Strength Exercise");

        int exerciseTypeChoice = getUserChoice(scanner);
        System.out.print("Enter exercise name: ");
        String exerciseName = scanner.nextLine();

        System.out.print("Enter duration (in minutes): ");
        int duration = getValidatedDuration(scanner);
        if (duration <= 0) return;

        Date exerciseDate = new Date(); // Default to the current date

        Exercise exercise = createExercise(exerciseTypeChoice, exerciseName, duration, exerciseDate);
        if (exercise == null) {
            System.out.println("Invalid exercise type selected. Exercise not added.");
            return;
        }

        System.out.print("Enter MET value (optional, default is 8.0): ");
        double metValue = getValidatedMET(scanner);

        exercise.calculateCaloriesBurned(userProfile.getWeight(), metValue);
        userProfile.addExercise(exercise);

        System.out.println("Exercise added successfully!");
    }

    // Method to validate duration input
    private static int getValidatedDuration(Scanner scanner) {
        try {
            int duration = Integer.parseInt(scanner.nextLine());
            if (duration <= 0) throw new IllegalArgumentException();
            return duration;
        } catch (Exception e) {
            System.out.println("Invalid duration. Please enter a positive number.");
            return -1; // Return invalid duration
        }
    }

    // Method to validate MET input
    private static double getValidatedMET(Scanner scanner) {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 8.0; // Default MET value
        }
    }

    // Factory method to create specific exercise types
    private static Exercise createExercise(int exerciseType, String name, int duration, Date date) {
        return switch (exerciseType) {
            case 1 -> new CardioExercise(name, duration, date);
            case 2 -> new StrengthExercise(name, duration, date);
            default -> null; // Invalid choice
        };
    }
}

// Base Interface for Exercise
interface Exercise {
    void calculateCaloriesBurned(double weight, double MET);
    void displayDetails();
}

// Cardio Exercise Class
class CardioExercise implements Exercise {
    private final String name;
    private final int duration;
    private final Date date;
    private double caloriesBurned;

    public CardioExercise(String name, int duration, Date date) {
        this.name = name;
        this.duration = duration;
        this.date = date;
        this.caloriesBurned = 0.0;
    }

    @Override
    public void calculateCaloriesBurned(double weight, double MET) {
        this.caloriesBurned = (MET * weight * duration) / 60;
    }

    @Override
    public void displayDetails() {
        System.out.printf("Exercise Type: Cardio%nName: %s%nDuration: %d minutes%nCalories Burned: %.2f kcal%nDate: %s%n",
                name, duration, caloriesBurned, date);
    }
}

// Strength Exercise Class
class StrengthExercise implements Exercise {
    private final String name;
    private final int duration;
    private final Date date;
    private double caloriesBurned;

    public StrengthExercise(String name, int duration, Date date) {
        this.name = name;
        this.duration = duration;
        this.date = date;
        this.caloriesBurned = 0.0;
    }

    @Override
    public void calculateCaloriesBurned(double weight, double MET) {
        this.caloriesBurned = (MET * weight * duration) / 60;
    }

    @Override
    public void displayDetails() {
        System.out.printf("Exercise Type: Strength%nName: %s%nDuration: %d minutes%nCalories Burned: %.2f kcal%nDate: %s%n",
                name, duration, caloriesBurned, date);
    }
}

// User Profile Class
class UserProfile {
    private final String name;
    private final int age;
    private final double weight;
    private final ArrayList<Exercise> exerciseHistory;

    public UserProfile(String name, int age, double weight) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.exerciseHistory = new ArrayList<>();
    }

    public void addExercise(Exercise exercise) {
        exerciseHistory.add(exercise);
    }

    public void displayExerciseHistory() {
        if (exerciseHistory.isEmpty()) {
            System.out.println("No exercises logged yet.");
            return;
        }
        System.out.println("\n=== Exercise History ===");
        for (Exercise exercise : exerciseHistory) {
            exercise.displayDetails();
            System.out.println("-------------------------");
        }
    }

    public double getWeight() {
        return weight;
    }
}
