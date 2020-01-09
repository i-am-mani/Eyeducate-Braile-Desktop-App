package sample;

import java.util.Random;
import java.util.Scanner;

public class SnakeAndLadder {

    static Random random = new Random();
    int currentPosition = 0;
    int[] snakeMap = {
            0, 1, 2, 3, 34, 5, 6, 7, 8, 9,
            10, 11, 12, 13, 14, 15, 32, 17, 18, 19,
            20, 21, 22, 23, 9, 25, 26, 27, 28, 11,
            30, 31, 77, 33, 34, 35, 36, 37, 38, 39,
            40, 41, 42, 43, 44, 45, 46, 47, 31, 49,
            50, 51, 52, 53, 54, 89, 56, 57, 58, 59,
            60, 61, 62, 5, 64, 65, 66, 67, 68, 69,
            70, 71, 72, 73, 74, 75, 89, 77, 78, 66,
            80, 81, 82, 44, 84, 85, 93, 87, 88, 89,
            90, 91, 92, 93, 94, 24, 96, 97, 0, 99
    };

    public static void main(String[] args) {
        SnakeAndLadder s1 = new SnakeAndLadder();
        SnakeAndLadder s2 = new SnakeAndLadder();

        while (s1.currentPosition < 101 || s2.currentPosition < 101) {
            System.out.println("Player 1 turn");
            Scanner sc = new Scanner(System.in);
            sc.next();
            int roll = rollDice();
            System.out.println("You Got " + roll);
            s1.nextMove(roll);
            s1.warnSnakes();
            s1.warnLadders();

            System.out.println("Player 2 turn");
            sc.next();
            int roll1 = rollDice();
            System.out.println("You Got " + roll1);
            s2.nextMove(roll1);
            s2.warnSnakes();
            s2.warnLadders();
        }
        System.out.println("You have won");
    }

    public void nextMove(int i) {
        int num = i;
        if ((currentPosition + num) < 101) {
            currentPosition = snakeMap[(currentPosition + num)];
            System.out.println(currentPosition);
        }
    }

    public void warnSnakes() {

        for (int i = 1; i < 6; i++) {
            if ((currentPosition + i) < 100 && (currentPosition > snakeMap[currentPosition + i])) {
                System.out.println("Their is a Snake on " + (currentPosition + i));
            }
        }
    }

    public void warnLadders() {

        for (int i = 1; i < 6; i++) {
            if ((currentPosition + i) < 100 && (currentPosition + i) < snakeMap[currentPosition + i]) {
                System.out.println("Their is a Ladder on " + (currentPosition + i));
            }
        }
    }

    public static int rollDice() {
        int n = Math.abs(random.nextInt(6)) + 1;
        return n;
    }

//    private static void setMap(){
//        int i=0,j=0,col=2,nSnakes=3,nLadders=4;
//        int counter=1;
//        for(i=0;i<10;i++){
//            for(j=0;j<10;j++){
//                snakeMap[i][j]=counter;
//                counter++;
//            }
//        }

//        for(j=0;j<8;j+=col) {
//            int curCol = j + col;
//            for (i = 0; i < nSnakes; i++) {
//                int row=Math.abs(random.nextInt(10));
//                int colum=Math.abs(random.nextInt(curCol)-j);
//                System.out.println(row+" "+colum);
//                snakeMap[colum][row]=Math.abs(random.nextInt(snakeMap[colum][row]));
//            }
//        }
//
//        for(i=0;i<10;i++){
//            for(j=0;j<10;j++){
//                System.out.print(snakeMap[i][j]+" ");
//            }
//            System.out.println();
//        }
//
//    }


}
