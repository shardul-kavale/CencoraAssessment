import java.util.*;
public class Solution {
    private static boolean reversedDirections = false;
    private static String currentDirection = "SOUTH";
    private static boolean breakerMode = false;
    private static List<String> path = new ArrayList<>();
    private static int row = -1, col = -1;
    private static char[][] map;
    private static Set<String> visited = new HashSet<>();



    private static void flipDirections() {
        reversedDirections = !reversedDirections;
    }
    private static void flipBreakerMode() {
        breakerMode = !breakerMode;
    }
    //Find starting position
    private static void findStartingPosition(){
        boolean found = false;
        for (int i = 1; i < map.length-1 && !found; i++) {
            for (int j = 1; j < map[i].length-1; j++) {
                if (map[i][j] == '@') {
                    row = i;
                    col = j;
                    found = true; //to break outer loop
                    break;
                }
            }
        }
        if(row == -1 || col == -1){
            throw new NoSuchElementException("TERMINTAING GAME :: Starting position not found in the map. Enter correct inputs during next gameplay");
        }
    }
    private static boolean isNextMoveFeasible(int nextRow, int nextCol) {
        if(breakerMode && map[nextRow][nextCol] == 'X'){
            map[nextRow][nextCol] = ' ';
        }
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
        List<String> directions = Arrays.asList("SOUTH", "EAST", "NORTH", "WEST");
        List<String> directionsInverted = Arrays.asList("WEST", "NORTH","EAST", "SOUTH");

        for (String dir : (reversedDirections? directionsInverted : directions)) {
            currentDirection = dir;
            int[] nextPosition = findNextPosition();
            if (isNextMoveFeasible(nextPosition[0], nextPosition[1])) {
                return Optional.of(dir);
            }
        }
        return Optional.empty();//no feasible direction
    }

    private static void setCurrentDirection(String direction){
        if(Set.of("NORTH", "SOUTH", "EAST", "WEST").contains(direction)){
            currentDirection = direction;
        }
        else {
            throw new IllegalArgumentException("Invalid direction: " + direction + "Try again with correct inputs");
        }
    }
    private static void handleCommand(char command) {
        switch (command) {
            case 'N':
                setCurrentDirection("NORTH");
                break;
            case 'S':
                setCurrentDirection("SOUTH");
                break;
            case 'E':
                setCurrentDirection("EAST");
                break;
            case 'W':
                setCurrentDirection("WEST");
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
        for (int i = 1; i < map.length-1; i++) {
            for (int j = 1; j < map[i].length-1; j++) {
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
            //next potential position:
            int[] nextPosition = findNextPosition();

            if(isNextMoveFeasible(nextPosition[0], nextPosition[1])) {
                //move:
                row = nextPosition[0];
                col = nextPosition[1];

                if(visited.contains(nextPosition.toString() + currentDirection + reversedDirections + breakerMode)){
                    System.out.println("LOOP");
                    return;
                }
                visited.add(nextPosition.toString() + currentDirection + reversedDirections + breakerMode);
                path.add(currentDirection);

                handleCommand(map[row][col]);

            }else{
                Optional<String> newDirection = findNextDirection();
                if (newDirection.isEmpty()) { //no directon is feasible
                    System.out.println("LOOP");
                    return;
                }
                currentDirection = newDirection.get();
            }
        }
    }
}
