import static java.lang.System.exit;

public class Main {

    static int[][] goal = {

            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8}
    };

    //Second goal state
    /*static int[][] goal = {

            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
    };*/

    public static void main(String[] args) {

        int[][] start = {

                {3, 1, 2},
                {4, 7, 5},
                {6, 8, 0}
        };


        /*int[][] start = {

                {3, 1, 0},
                {7, 5, 2},
                {4, 6, 8}
        };*/



        //              Importance - Nr of moves - Generated states
        //              100 - after 5h run out of array size (requires over 1.000.000 generated states)
        //              90 - 73 - 381.000 (68 minutes)
        //              80 - 73 - 161.000
        //              75 - 73 - 106.000
        //              50 - 73 - 22.000
        //              40 - 73 - 15.000
        //              30 - 73 - 10.000
        //              20 - 73 - 8.000
        //              15 - 53 - 3.000
        //              10 - 43 - 12.000
        //              9 - 41 - 16.000 
        //              8 - 41 - 9.000  
        //              7 - 41 - 20.000
        //              6 - 33 - 25.000   
        //              5 - 38 - 80.000
        //              4 - 33 - 143.000 
        //              3 - 33 - 252.000 (6 minutes)
        //              2 - 31 - 749.000 (47 minutes)

        /*int[][] start = {

                {8, 0, 6},
                {5, 4, 7},
                {2, 3, 1}
        };*/



        //              9 - 41 - 16.000 
        //              8 - 41 - 9.000,     7 - 41 - 20.000,
        //              6 - 33 - 23.000,    5 - 37 - 75.000, 
        //              4 - 33 - 159.000,   3 - 33 - 275.000,
             
        /*int[][] start = {

                {8, 7, 6},
                {0, 4, 1},
                {2, 5, 3}
        };*/



        //The hardest 8-Puzzle state for second goal state
        //Remember to change the goal state!
        /*
        int[][] start = {

                {8, 6, 7},
                {2, 5, 4},
                {3, 0, 1}
        };*/

        int importanceH = 1; //      CHANGE IT IF YOU ARE USING DIFFICULT STARTING STATE

        int[][][] states = new int[1000000][5][3];   // states[x][0][0] - states[x][2][2] - position of numbers in matrices
                                                    // states[x][3][0] = g 
                                                    // states[x][3][1] = h
                                                    // states[x][3][2] = f
                                                    // states[x][4][0] - direction of move
                                                    // states[x][4][1] - has been used?
        int[] zeroPosition;
        int g = 0;
        int nr = 2; //nr of states
        int MinF = 1; //nr of state with the least nr of misplaced titles
        int previousMove; // position on previous move to prevent loops
        int[] correctMinF = new int[500000];
        int howManyMinF = 0;

        // Input from parameters 
        try{

            if(args.length == 0)
            {

            }else if(args.length == 1)
            {
                importanceH = Integer.parseInt(args[0]);
            }else if(args.length == 9)
            {
                start[0][0] = Integer.parseInt(args[0]);
                start[0][1] = Integer.parseInt(args[1]);
                start[0][2] = Integer.parseInt(args[2]);
                start[1][0] = Integer.parseInt(args[3]);
                start[1][1] = Integer.parseInt(args[4]);
                start[1][2] = Integer.parseInt(args[5]);
                start[2][0] = Integer.parseInt(args[6]);
                start[2][1] = Integer.parseInt(args[7]);
                start[2][2] = Integer.parseInt(args[8]);

            }else if(args.length == 10)
            {
                start[0][0] = Integer.parseInt(args[0]);
                start[0][1] = Integer.parseInt(args[1]);
                start[0][2] = Integer.parseInt(args[2]);
                start[1][0] = Integer.parseInt(args[3]);
                start[1][1] = Integer.parseInt(args[4]);
                start[1][2] = Integer.parseInt(args[5]);
                start[2][0] = Integer.parseInt(args[6]);
                start[2][1] = Integer.parseInt(args[7]);
                start[2][2] = Integer.parseInt(args[8]);

                importanceH = Integer.parseInt(args[9]);
            }else
            {
                System.out.println("\nWrong number of parameters ("+args.length+"). Try again.");
                exit(0);
            }
        }catch (Exception e)
        {
            System.out.println(e +"\nWrong data. Try again.");
            exit(0);
        }


        System.out.println("\nStart state: ");
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                states[0][i][j] = goal[i][j];
                states[1][i][j] = start[i][j];
                System.out.print(start[i][j]+ " ");
            }
            System.out.println();
        }
        System.out.println();

        int h = heuristicStates(states, 1)*importanceH;
        int f = h;

        states[1][3][0] = 0;
        states[1][3][1] = h;
        states[1][3][2] = h;

        boolean done = compareStates(states, 0, 1, 2);

        if(done)
        {
            System.out.print("Your start state is the same as the goal state!\n");
            exit(0);
        }


        // Creating tree
        while(!done)
        {

            zeroPosition = findZero(states, MinF);
            g = states[MinF][3][0]+1;
            previousMove = states[MinF][4][0];

            if(isMovePossible("up", zeroPosition) && previousMove != 2)
            {

                for(int i=0; i<3; i++)
                {
                    for(int j=0; j<3; j++)
                    {
                        if(zeroPosition[0] == i && zeroPosition[1] == j)
                        {
                            states[nr][i][j] = states[MinF][i-1][j];
                        }else if(zeroPosition[0]-1 == i && zeroPosition[1] == j)
                        {
                            states[nr][i][j] = states[MinF][i+1][j];
                        }else
                        {
                            states[nr][i][j] = states[MinF][i][j];
                        }
                    }
                }

                states[nr][3][0] = g;
                states[nr][3][1] = heuristicStates(states, nr)*importanceH; // h
                states[nr][3][2] = states[nr][3][0] + states[nr][3][1]; // f
                states[nr][4][0] = 1; //in which direction move

                nr++;

            }

            if(isMovePossible("down", zeroPosition) && previousMove != 1)
            {

                for(int i=0; i<3; i++)
                {
                    for(int j=0; j<3; j++)
                    {
                        if(zeroPosition[0] == i && zeroPosition[1] == j)
                        {
                            states[nr][i][j] = states[MinF][i+1][j];
                        }else if(zeroPosition[0]+1 == i && zeroPosition[1] == j)
                        {
                            states[nr][i][j] = states[MinF][i-1][j];
                        }else
                        {
                            states[nr][i][j] = states[MinF][i][j];
                        }
                    }
                }

                states[nr][3][0] = g;
                states[nr][3][1] = heuristicStates(states, nr)*importanceH;
                states[nr][3][2] = states[nr][3][0] + states[nr][3][1];
                states[nr][4][0] = 2;

                nr++;

            }

            if(isMovePossible("left", zeroPosition) && previousMove != 4)
            {

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (zeroPosition[0] == i && zeroPosition[1] == j) {
                            states[nr][i][j] = states[MinF][i][j - 1];
                        } else if (zeroPosition[0] == i && zeroPosition[1] - 1 == j) {
                            states[nr][i][j] = states[MinF][i][j + 1];
                        } else {
                            states[nr][i][j] = states[MinF][i][j];
                        }
                    }
                }

                states[nr][3][0] = g;
                states[nr][3][1] = heuristicStates(states, nr)*importanceH;
                states[nr][3][2] = states[nr][3][0] + states[nr][3][1];
                states[nr][4][0] = 3;

                nr++;

            }

            if(isMovePossible("right", zeroPosition) && previousMove != 3)
            {

                for(int i=0; i<3; i++)
                {
                    for(int j=0; j<3; j++)
                    {
                        if(zeroPosition[0] == i && zeroPosition[1] == j)
                        {
                            states[nr][i][j] = states[MinF][i][j+1];
                        }else if(zeroPosition[0] == i && zeroPosition[1]+1 == j)
                        {
                            states[nr][i][j] = states[MinF][i][j-1];
                        }else
                        {
                            states[nr][i][j] = states[MinF][i][j];
                        }
                    }
                }

                states[nr][3][0] = g;
                states[nr][3][1] = heuristicStates(states, nr)*importanceH;
                states[nr][3][2] = states[nr][3][0] + states[nr][3][1];
                states[nr][4][0] = 4;

                nr++;

            }


            boolean changed = false;

            while(!changed)
            {
                for(int i=2; i < nr; i++)
                {
                    if(states[i][3][2] <= f && states[i][4][1] != 1)
                    {
                        MinF = i;
                        f = states[i][3][2];
                        changed = true;

                        if(compareStates(states, 0, MinF, 2))
                        {
                            done=true;
                            break;
                        }

                    }
                }

                f++;
            }

            f--;
            states[MinF][4][1] = 1;
            correctMinF[howManyMinF] = MinF;

            howManyMinF++;

        }


        // After finding solution, this part take care of userfriendly output
        int[] answer = new int[g+1];
        int[] moves = new int[g+1];
        int move = states[correctMinF[howManyMinF-1]][4][0];
        answer[g] = correctMinF[howManyMinF-1];
        moves[g] = states[correctMinF[howManyMinF-1]][4][0];

        for(int l = g; l > 1; l--)
        {
            int[] zero1 = findZero(states, answer[l]);

            for(int m = 0; m < howManyMinF; m++)
            {
                int[] zero = findZero(states, correctMinF[m]);

                if(move == 1 && zero1[0] == zero[0]-1 && zero1[1] == zero[1] && states[correctMinF[m]][3][0] == l-1)
                {
                    if(compareStates(states, answer[l], correctMinF[m], 1))
                    {
                        answer[l-1] = correctMinF[m];
                        moves[l-1] = states[correctMinF[m]][4][0];

                    }

                }else if(move == 2 && zero1[0] == zero[0]+1 && zero1[1] == zero[1] && states[correctMinF[m]][3][0] == l-1)
                {
                    if(compareStates(states, answer[l], correctMinF[m], 1))
                    {
                        answer[l-1] = correctMinF[m];
                        moves[l-1] = states[correctMinF[m]][4][0];
                    }

                }else if(move == 3 && zero1[0] == zero[0] && zero1[1] == zero[1]-1 && states[correctMinF[m]][3][0] == l-1)
                {
                    if(compareStates(states, answer[l], correctMinF[m], 1))
                    {
                        answer[l-1] = correctMinF[m];
                        moves[l-1] = states[correctMinF[m]][4][0];
                    }

                }else if(move == 4 && zero1[0] == zero[0] && zero1[1] == zero[1]+1 && states[correctMinF[m]][3][0] == l-1)
                {
                    if(compareStates(states, answer[l], correctMinF[m], 1))
                    {
                        answer[l-1] = correctMinF[m];
                        moves[l-1] = states[correctMinF[m]][4][0];
                    }
                }

            }

            move = states[answer[l-1]][4][0];

        }

        // Displaying solution
        for(int k=1; k<=g; k++)
        {
            for(int i=0; i<3; i++) // To see value of g, h and f for every state change: i < 4
            {
                for(int j=0; j<3; j++)
                {
                    System.out.print(states[answer[k]][i][j]+ " ");
                }
                System.out.println();
            }
            System.out.println();
            if(k == g-1)
                System.out.println("Final state: ");
        }

        System.out.println("Number of generated states: " + (nr-2));
        System.out.println("Number of moves: " + g);
        System.out.println("Importance of h: " + importanceH);
        System.out.println("\nThe correct path: ");

        for(int i=1; i<=g; i++)
        {
            System.out.print(whichDirection(moves[i])+" ");

            if(i%12 == 0)
                System.out.println();
        }

        System.out.println();

    }


    /**
     * Method checks how many tiles are misplaced in state of the table
     */

    static int heuristicStates(int[][][] table, int nr)
    {
        int howMany = 0;

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                if(goal[i][j] != table[nr][i][j] && table[nr][i][j] != 0)
                {
                    howMany++;
                }
            }
        }

        return howMany;

    }


    /**
     * Method finds the position of zero in table
     */

    static int[] findZero(int[][][] table, int which)
    {

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                if(table[which][i][j] == 0)
                {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0, 0};
    }


    /**
     * Checks if move can be done
     */

    static boolean isMovePossible(String where, int[] zeroPosition)
    {
        if(where.contains("up"))
        {
            return !(zeroPosition[0] == 0);
        }else if(where.contains("down"))
        {
            return !(zeroPosition[0] == 2);
        }else if(where.contains("left"))
        {
            return !(zeroPosition[1] == 0);
        }else// if(where.contains("right"))
        {
            return !(zeroPosition[1] == 2);
        }

    }


    /**
     * Convert moves saved in integer to string
     */

    static String whichDirection(int nr)
    {
        if(nr == 1)
            return "up";
        else if(nr == 2)
            return "down";
        else if(nr == 3)
            return "left";
        else if(nr == 4)
            return "right";
        else
            return "ERROR";
    }


    /**
     * Checks if previous state is corrected
     * Used to find the path, from the tree, which leads to the final state
     */

    static boolean compareStates(int[][][] table, int which1, int which2, int type)
    {
        int howMany = 0;

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                if(table[which1][i][j] != table[which2][i][j])
                {
                    howMany++;
                }
            }
        }

        if(type == 1)
        {
            return howMany == 2;
        }else
        {
            return howMany == 0;
        }

    }

}

