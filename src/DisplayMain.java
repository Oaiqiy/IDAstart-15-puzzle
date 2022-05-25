import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.concurrent.*;

public class DisplayMain extends Application {
    private volatile boolean running = false;
    private final int length = 4;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Label label = new Label("ff");


        Button button = new Button("点我");


        VBox vBox = new VBox();

        vBox.getChildren().add(button);
        vBox.getChildren().add(label);


        byte[][] startBoard = {{0,1,2,3},{4,5,6,7},{8,9,10,11},{12,13,14,15}};
        byte[][] endBoard = {{0,1,2,3},{4,5,6,7},{8,9,10,11},{12,13,14,15}};


        BoardDisplay start = new BoardDisplay(length);
        BoardDisplay end = new BoardDisplay(length);
        start.change(startBoard,true);
        end.change(endBoard,true);

        Button randomButton = new Button("随机");

        randomButton.setOnAction(event -> {
            if(running)
                return;

            byte[][] s = RandomBoardGenerator.generate(length);
            byte[][] e = RandomBoardGenerator.generate(length);
            while(!Board.judge(s,e)){
                e = RandomBoardGenerator.generate(length);
            }

            for(int i = 0;i<length;i++)
                for(int j = 0;j<length;j++){
                    startBoard[i][j] = s[i][j];
                    endBoard[i][j] = e[i][j];
                }

            start.change(s,true);
            end.change(e,true);
        });



        Button run = new Button("运行");
        run.setOnAction(event -> {
            if(running)
                return;
            running = true;



            FutureTask<Board> task = new FutureTask<>(() -> {
                Board result = IDAstar.solve(endBoard,startBoard);

                Board finalResult = result;

                while(finalResult!=null){
                    Board f = finalResult;
                    Platform.runLater(()->{
                        System.out.println(f);
                        start.change(f);
                    });
                    Thread.sleep(100);
                    finalResult = finalResult.getParent();
                }

                running = false;
                return result;

            });

            new Thread(task).start();

        });

        HBox main = new HBox(20);
        main.getChildren().addAll(start,randomButton,run ,end);


        Scene scene = new Scene(main,600,480);
        primaryStage.setScene(scene);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}



class BoardDisplay extends VBox{
    private Button[][] pieces;
    private int zeroX = 0, zeroY = 0;
    private static final double size = 30;

    public BoardDisplay(int length){
        pieces = new Button[length][length];

        for(int i = 0;i<length;i++){
            HBox hBox = new HBox();
            for(int j = 0;j<length;j++){
                pieces[i][j] = new Button();
                pieces[i][j].setText(Integer.toString(i*4+j));
                pieces[i][j].setPrefSize(size,size);
                hBox.getChildren().add(pieces[i][j]);
            }
            getChildren().add(hBox);
        }

    }

    public void change(Board board){
        byte[][] b = board.getBoard();

        change(b,false);

    }

    public void change(byte[][] b, boolean newBoard){
        pieces[zeroX][zeroY].setVisible(true);
        if(newBoard){


            for(int i = 0;i<b.length;i++)
                for(int j = 0;j<b.length;j++){
                    if(b[i][j] == 0){
                        zeroX = i;
                        zeroY = j;
                        pieces[i][j].setVisible(false);
                    }

                    pieces[i][j].setText(Byte.toString(b[i][j]));
                }


            return;
        }


        int x = 0,y =0;
        for(int i = 0;i<b.length;i++)
            for(int j = 0;j<b.length;j++){
                if(b[i][j] == 0){
                    x = i;
                    y = j;
                    pieces[x][y].setVisible(false);
                    break;
                }

            }

        String s = pieces[x][y].getText();
        pieces[x][y].setText("0");
        pieces[zeroX][zeroY].setText(s);

        zeroX = x;
        zeroY = y;


    }


}
