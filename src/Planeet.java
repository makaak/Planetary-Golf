import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by Rauno on 02.01.2017.
 */
public class Planeet extends Circle {

    private final double mass;
    private ForceVector fV;
    private double speedX;
    private double speedY;

    public Planeet(double centreX, double centreY, double radius, double mass){
        super(centreX, centreY, radius);
        this.mass = mass;
        fV = new ForceVector();
        speedX = 0;
        speedY = 0;
        this.setFill(Color.DODGERBLUE);
    }

    public Planeet(double radius, double mass){
        super(radius);
        this.mass = mass;
        fV = new ForceVector();
        speedX = 0;
        speedY = 0;
        this.setFill(Color.DODGERBLUE);
    }

    double getMass(){
        return mass;
    }

    double getAccelX(){
        return fV.getFX()/mass;
    }
    double getAccelY(){
        return fV.getFY()/mass;
    }

    /*
    void addVector(ForceVector V){
        double X = fV.getFX()+V.getFX();
        double Y = fV.getFY()+V.getFY();
        fV.setF(Math.sqrt(Math.pow(X,2) + Math.pow(Y,2)));
        fV.setDir(Math.atan2(Y,X));
    }*/

    ForceVector getFVector(){
        return fV;
    }

    void setSpeedX(double speedX){
        this.speedX = speedX;
    }

    void setSpeedY(double speedY){
        this.speedY = speedY;
    }

    double getSpeedX(){
        return speedX;
    }

    double getSpeedY(){
        return speedY;
    }
}
