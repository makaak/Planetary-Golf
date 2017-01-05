/**
 * Created by Rauno on 03.01.2017.
 */
public class ForceVector {
    private double F;
    private double Dir;

    public ForceVector(){
        setF(0);
        setDir(0);
    }

    public void setF(double F){
        this.F = F;
    }

    public void setDir(double Dir){
        this.Dir = Dir;
    }

    public double getF(){
        return F;
    }

    public double getDir(){
        return Dir;
    }

    public double getFX(){
        return Math.cos(Dir)*F;
    }

    public double getFY(){
        return Math.sin(Dir)*F;
    }

    public void addVector(ForceVector V){
        double X = this.getFX()+V.getFX();
        double Y = this.getFY()+V.getFY();
        this.setF(Math.sqrt(Math.pow(X,2) + Math.pow(Y,2)));
        this.setDir(Math.atan2(Y,X));
    }
}