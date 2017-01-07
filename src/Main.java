import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.ListIterator;

public class Main extends Application {

    private Font buttonFont = new Font(40);
    private Stage window;

    private Scene menu;

    private int WIDTH = 1500;
    private int HEIGHT = 800;

    private AnimationTimer aT;

    private Group gameLayout;

    private ArrayList<ArrayList<Planeet>> Levels;
    private ArrayList<Planeet> LevelObjects;
    private int level;

    private Line joon;
    private Planeet pall;
    private Planeet pseudoPall;
    private Circle auk;

    private Polyline trajektoor = new Polyline();

    private boolean moving;
    private boolean trajektoorMoving;

    private boolean winner;
    private Text winText;

    //DEBUG
    private boolean debug = true;

    private Text debugText0 = new Text();
    private Text debugText1 = new Text();
    private Text debugText2 = new Text();
    private Text debugText3 = new Text();
    private Text debugText4 = new Text();

    private Line debugLine = new Line();

    private int i = 1;


    public void init() {
        LevelObjects = new ArrayList<>();
        Levels = new ArrayList<>();

        joon = new Line();
        joon.setStroke(Color.WHITE);
        joon.setStrokeWidth(2);

        trajektoor.setStroke(Color.WHITE);
        trajektoor.setStrokeWidth(2);

        pall = new Planeet(10, 1);
        pseudoPall = new Planeet(10, 1);
        pall.setFill(Color.WHITE);

        auk = new Circle(20, Color.GREEN);
        auk.setCenterX(1350);
        auk.setCenterY(400);

        winText = new Text("WINNER");
        winText.setFont(new Font(200));
        winText.setFill(Color.RED);
        winText.setVisible(false);
        winText.setLayoutX(300);
        winText.setLayoutY(300);

        //Hardcoded Levels... For now.
        //Level0
        LevelObjects.add(new Planeet(WIDTH/2, HEIGHT/2, 50, 500));
        LevelObjects.add(new Planeet(WIDTH/2 + 200, HEIGHT/2 - 150, 20, 1000));
        LevelObjects.add(new Planeet(150, 300, 50, 750));
        Levels.add((ArrayList<Planeet>) LevelObjects.clone());
        LevelObjects.clear();

        //Level1
        Levels.add(LevelObjects);

        //Level2



        debugText0.setLayoutX(50);
        debugText0.setLayoutY(50);
        debugText0.setFill(Color.WHITE);

        debugText1.setLayoutX(250);
        debugText1.setLayoutY(50);
        debugText1.setFill(Color.WHITE);

        debugText2.setLayoutX(450);
        debugText2.setLayoutY(50);
        debugText2.setFill(Color.WHITE);

        debugText3.setLayoutX(650);
        debugText3.setLayoutY(50);
        debugText3.setFill(Color.WHITE);

        debugText4.setLayoutX(850);
        debugText4.setLayoutY(50);
        debugText4.setFill(Color.WHITE);

        debugLine.setStroke(Color.RED);
        debugLine.setStrokeWidth(2);
    }

    @Override
    public void start(Stage primaryStage){
        window = primaryStage;
        window.setResizable(false);
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        
        VBox menuLayout = new VBox();
        gameLayout = new Group();

        menu = new Scene(menuLayout, WIDTH, HEIGHT, Color.WHITE);
        Scene game = new Scene(gameLayout, WIDTH, HEIGHT, Color.BLACK);

        window.setScene(menu);

        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setSpacing(10);


        Button playButton = new Button("PLAY");
        Button exitButton = new Button("EXIT");
        playButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);
        menuLayout.getChildren().addAll(playButton, exitButton);

        exitButton.setOnAction(e -> closeProgram());


        game.setOnMousePressed(e -> {
            if(!moving) {
                joon.setStartX(e.getSceneX());
                joon.setStartY(e.getSceneY());
                joon.setEndX(pall.getCenterX());
                joon.setEndY(pall.getCenterY());
                joon.setVisible(true);
                trajektoor.setVisible(true);
            }
        });

        game.setOnMouseDragged(e -> {
            if(!moving) {
                joon.setStartX(e.getSceneX());
                joon.setStartY(e.getSceneY());
                joon.setEndX(pall.getCenterX());
                joon.setEndY(pall.getCenterY());
                debugText0.setText((String.valueOf((Math.atan2((pall.getCenterY() - e.getSceneY()), (pall.getCenterX() - e.getSceneX()))))));

                pseudoPall.setCenterY(pall.getCenterY());
                pseudoPall.setCenterX(pall.getCenterX());
                trajektoor.getPoints().clear();
                kick(pseudoPall, Math.atan2(pseudoPall.getCenterY() - e.getSceneY(), pseudoPall.getCenterX() - e.getSceneX()), Math.sqrt(Math.pow(pseudoPall.getCenterX() - e.getSceneX(), 2) + Math.pow(pseudoPall.getCenterY() - e.getSceneY(), 2)) / 100, false);
                for (int j = 0; j < 2500; j++) {


                    updatePall(pseudoPall);

                    trajektoor.getPoints().addAll(pseudoPall.getCenterX(), pseudoPall.getCenterY());
                    if(!trajektoorMoving) break;

                }
            }


        });

        //KICK
        game.setOnMouseReleased(e -> {
            if(!moving) {
                joon.setVisible(false);
                trajektoor.setVisible(false);
                kick(pall, Math.atan2(pall.getCenterY() - e.getSceneY(), pall.getCenterX() - e.getSceneX()), Math.sqrt(Math.pow(pall.getCenterX() - e.getSceneX(), 2) + Math.pow(pall.getCenterY() - e.getSceneY(), 2)) / 100, true);
            }
        });

        // START game
        playButton.setOnAction(e -> {

            level = 0;
            initLevel();
            window.setScene(game);

        });

        // STOP game
        game.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                aT.stop();
                window.setScene(menu);
            }

            if(debug && e.getCode() == KeyCode.SPACE && !winner){
                if(i == 1){
                    aT.stop();
                }else{
                    aT.start();
                }
                i = -i;
            }
            if(winner) window.setScene(menu);
        });


        aT = new AnimationTimer() {




            @Override
            public void handle(long now) {



                debugText1.setText(String.valueOf(pall.getAccelX()));
                debugText2.setText(String.valueOf(pall.getAccelY()));
                debugText3.setText(String.valueOf(pall.getFVector().getDir()));

                debugLine.setStartX(pall.getCenterX());
                debugLine.setStartY(pall.getCenterY());
                debugLine.setEndY(pall.getCenterY() + 5*pall.getRadius()*Math.sin(pall.getFVector().getDir()));
                debugLine.setEndX(pall.getCenterX() + 5*pall.getRadius()*Math.cos(pall.getFVector().getDir()));



                updatePall(pall);
            }
        };




        window.show();
    }

    private void updatePall(Planeet pall){

        ListIterator<Planeet> i = Levels.get(level).listIterator();
        Planeet p;
        double dX;
        double dY;
        double diag;
        ForceVector V = new ForceVector();




        pall.getFVector().setDir(0);
        pall.getFVector().setF(0);

        while(i.hasNext()) {
            p = i.next();

            dX = p.getCenterX() - pall.getCenterX();
            dY = p.getCenterY() - pall.getCenterY();
            diag = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

            V.setF(pall.getMass() * p.getMass() / Math.pow(diag, 2));
            V.setDir(Math.atan2(dY, dX));

            debugText4.setText(String.valueOf(V.getDir()));

            //debug
            //System.out.println(V.getDir());

            pall.getFVector().addVector(V);

            //Collision detection
            //Harder than expected.

            if(diag <= (pall.getRadius() + p.getRadius())){
                /*
                pall.setSpeedX(pall.getSpeedX()*Math.cos(Math.atan2(-dY, -dX) + Math.signum(pall.getSpeedX())*Math.PI/2));
                pall.setSpeedY(pall.getSpeedY()*Math.sin(Math.atan2(-dY, -dX) - Math.signum(pall.getSpeedY())*Math.PI/2));
                */

                /*
                s = Math.sqrt(pall.getSpeedX()*pall.getSpeedX() + pall.getSpeedY()*pall.getSpeedY());
                b = Math.PI - Math.atan2(pall.getSpeedY(), pall.getSpeedX());
                a = Math.atan2(dY,dX) + Math.PI/2;
                nurk = Math.PI - 2*a - b;
                pall.setSpeedX(s*Math.cos(nurk));
                pall.setSpeedY(s*Math.sin(nurk));
                */

                //HACK lahendus hetkel
                pall.setCenterY(p.getCenterY() - Math.signum(dY) * (pall.getRadius() + p.getRadius() + 1) * Math.abs(dY)/diag);
                pall.setCenterX(p.getCenterX() - Math.signum(dX) * (pall.getRadius() + p.getRadius() + 1) * Math.abs(dX)/diag);

                moving = false;
                trajektoorMoving = false;

                aT.stop();


            }
        }


        if (trajektoorMoving || moving) {


            pall.setCenterX(pall.getCenterX() + pall.getSpeedX());
            pall.setCenterY(pall.getCenterY() + pall.getSpeedY());

            pall.setSpeedX(pall.getAccelX() + pall.getSpeedX());
            pall.setSpeedY(pall.getAccelY() + pall.getSpeedY());

        }

        if(moving && ((pall.getRadius() + auk.getRadius()) > (Math.sqrt(Math.pow(pall.getCenterX() - auk.getCenterX(),2) + Math.pow(pall.getCenterY() - auk.getCenterY(),2))))){
            aT.stop();
            winner = true;
            winText.setVisible(true);


        }



    }

    private void closeProgram() {
        window.close();
    }

    private void kick(Planeet pall, double dir, double speed, boolean valid) {
        pall.setSpeedX(Math.cos(dir)*speed);

        pall.setSpeedY(Math.sin(dir)*speed);

        if(valid) {
            moving = true;
            trajektoorMoving = false;
            aT.start();
        }else{
            trajektoorMoving= true;
        }
    }

    private void initLevel(){
        gameLayout.getChildren().clear();
        gameLayout.getChildren().addAll(Levels.get(level));
        gameLayout.getChildren().addAll(pall, auk, joon, trajektoor, winText);

        moving = false;
        winner = false;

        if(debug) {
            gameLayout.getChildren().addAll(debugText0, debugText1, debugText3, debugText2, debugText4, debugLine);
        }

        pall.setCenterX(150);
        pall.setCenterY(230);


    }


    public static void main(String[] args) {
        launch(args);
    }
}
