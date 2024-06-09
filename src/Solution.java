import java.util.*;
import java.io.*;
import java.math.*;

public class Solution {
    private static boolean reversedDirections = false;
    private static String currentDirection = "SOUTH";
    private static boolean breakerMode = false;
    private static List<String> path = new ArrayList<>();
    private static int row = -1, col = -1;
    private static char[][] map;
    private static Set<Character> pathModifiers = Set.of('N', 'S', 'E', 'W');




    private static void flipDirections() {
        reversedDirections = !reversedDirections;
    }
    private static void flipBreakerMode() {
        breakerMode = !breakerMode;
    }
    //Find starting position
    private static void findStartingPosition(){
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '@') {
                    row = i;
                    col = j;
                }
            }
        }
        if(row == -1 || col == -1){
            throw new NoSuchElementException("TERMINTAING GAME :: Starting position not found in the map. Enter correct inputs during next gameplay");
        }
    }
    private static boolean isNextMoveFeasible(int nextRow, int nextCol) {
        return map[nextRow][nextCol] != '#' && map[nextRow][nextCol] != 'X';
    }
    private static int[] findNextPosition() {
        switch (currentDirection) {
            case "SOUTH":
                return new int[]{row + 1, col};
            case "EAST":
                return new int[]{row, col + 1};
            case "NORTH":
                return new int[]{row - 1, col};
            case "WEST":
                return new int[]{row, col - 1};
            default:
                throw new IllegalArgumentException("Invalid direction: " + currentDirection);
        }
    }
    private static Optional<String> findNextDirection() {

        List<String> directions = reversedDirections ?
                Arrays.asList("WEST", "NORTH","EAST", "SOUTH") :
                Arrays.asList("SOUTH", "EAST", "NORTH", "WEST");

        for (String dir : directions) {
            currentDirection = dir;
            int[] nextPosition = findNextPosition();
            if (isNextMoveFeasible(nextPosition[0], nextPosition[1])) {
                return Optional.of(dir);
            }
        }
        return Optional.empty();
    }
    private static void handleCommand(char command) {
        switch (command) {
            case 'N':
                currentDirection = "NORTH";
                break;
            case 'S':
                currentDirection = "SOUTH";
                break;
            case 'E':
                currentDirection = "EAST";
                break;
            case 'W':
                currentDirection = "WEST";
                break;
            case 'I':
                flipDirections();
                break;
            case 'B':
                flipBreakerMode();
                break;
            case 'T':
                teleport();
                break;
            case '$':
                printPathAndExit();
                break;
        }
    }

    private static void printPathAndExit(){
        path.forEach(p -> System.out.println(p));
        System.exit(0);
    }
    private static void teleport(){
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'T' && (i!=row||j!=col)) {
                    row = i;
                    col = j;
                    return;
                }
            }
        }
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int L = in.nextInt();
        int C = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }
        map = new char[L][C];
        //populate map:
        for (int i = 0; i < L; i++) {
            String row = in.nextLine();
            map[i] = row.toCharArray();
        }
        //find and set starting position and set direction
        findStartingPosition();

        //Start Game:
        while(true){
            int[] nextPosition = findNextPosition();
            if(isNextMoveFeasible(nextPosition[0], nextPosition[1])) {
                //move:
                row = nextPosition[0];
                col = nextPosition[1];
                path.add(currentDirection);

                handleCommand(map[row][col]);

            }else{
                Optional<String> newDirection = findNextDirection();
                if (newDirection.isEmpty()) {
                    System.out.println("LOOP");
                    return;
                }
                currentDirection = newDirection.get();
            }
        }
    }
}
