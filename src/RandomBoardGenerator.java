import java.util.Random;

public class RandomBoardGenerator {
    private static final Random random = new Random();

    public static int[][] generate(int length){
        int[][] board = new int[length][length];
        int total = length * length;
        boolean[] record = new boolean[total];

        for(int i= 0;i<length;i++)
            for(int j = 0;j < length;j++){
                int index = random.nextInt();
                if(index < 0)
                    index = - index;
                while(record[index % total])
                    index++;
                record[index%total] = true;
                board[i][j] = index%total;
            }

        return board;

    }

    public static int[][] generate(int[][] board, int count){
        int length = board.length;
        int[][] n = new int[length][length];
        for(int i = 0;i<length;i++)
            System.arraycopy(board[i], 0, n[i], 0, length);

        int[] loc = Board.findZero(n);
        int x = loc[0], y = loc[1];

        int[][] dir = new int[][]{{1,0},{0,1},{0,-1},{-1,0}};
        int lastDir = 0;

        for(int i = 0 ; i < count;i++){
            int d = random.nextInt(4);
            if(d == 3-lastDir)
                d = (d + 1) % 4;
            while(!(x + dir[d][0] >=0 && x + dir[d][0] <length &&  y + dir[d][1] >=0 && y + dir[d][1] <length))
                d = (d + 1) % 4;

                n[x][y] = n[x+dir[d][0]][y+dir[d][1]];
                n[x+dir[d][0]][y+dir[d][1]] = 0;

                x += dir[d][0];
                y += dir[d][1];

                lastDir = d;
        }

        return n;
    }

}
