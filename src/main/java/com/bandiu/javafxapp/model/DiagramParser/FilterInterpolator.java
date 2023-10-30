package com.bandiu.javafxapp.model.DiagramParser;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class FilterInterpolator {
    private double[] x = null;
    private double[] y = null;
    public FilterInterpolator(double[] curves){
        this.y=curves;
        this.x=new double[curves.length];
        for (int i = 0; i < x.length; i++) {
            x[i]=i;
        }
    }
    public FilterInterpolator(double[] x,double[] y){
        this.y=y;
        this.x=x;
    }

    public double[] getNewX() {
        return x;
    }

    public double[] getNewY() {
        return y;
    }

    public void interpolateFromToWidthStep (int indexXfrom, int indexXto, double stepX) throws Exception {
        checkRange(indexXfrom,indexXto);

        int amountOfValue = indexXto-indexXfrom;

        double[] dataToInterpolateX = new double[indexXto-indexXfrom];
        double[] dataToInterpolateY = new double[indexXto-indexXfrom];
        for (int i = 0; i < dataToInterpolateY.length; i++) {
            dataToInterpolateX[i]=x[indexXfrom+i];
            dataToInterpolateY[i]=y[indexXfrom+i];
        }
        double[][] interpolatedData = interpolate(dataToInterpolateX,dataToInterpolateY,stepX);
        double[] interpolatedX = interpolatedData[0];
        double[] interpolatedY = interpolatedData[1];

        replaceData(indexXfrom,indexXto,interpolatedX,interpolatedY);
    }
    private void checkRange(int indexFrom,int indexTo) throws Exception {
        int amountOfValue = indexTo-indexFrom;
        if(indexTo+1>x.length||indexFrom>x.length)
            throw new Exception("index is to big");
        if(indexTo<=0||indexFrom<0)
            throw new Exception("index is to low");
        if (amountOfValue<=0)
            throw new Exception("indexTo must be bigger then indexFrom");

    }

    private void replaceData(int indexFrom, int indexTo, double[] newX, double[] newY) throws Exception {
        checkRange(indexFrom,indexTo);
        int dIndex = indexTo-indexFrom;
        int newLength = x.length-dIndex+newX.length;
        double[] resX = new double[newLength];
        double[] resY = new double[newLength];

        for (int i = 0; i <= indexFrom; i++) {
            resX[i] = x[i];
            resY[i] = y[i];
        }
        for (int i = 0; i < newX.length; i++) {
            resX[indexFrom+1+i]=newX[i];
            resY[indexFrom+1+i]=newY[i];
        }
        for (int i = 0; i < x.length-indexTo; i++) {
            resX[indexFrom+newX.length+1+i]=x[i+indexTo];
            resY[indexFrom+newX.length+1+i]=y[i+indexTo];
        }
        x=resX;
        y=resY;
    }
    private double[][] interpolate(double[] x,double[] y,double stepX) throws Exception {
        SplineInterpolator interpolator = new SplineInterpolator();
        PolynomialSplineFunction spline = interpolator.interpolate(x, y);
        double rangeX = x[x.length-1]-x[0];
        if (stepX>rangeX)
            throw new Exception("xMax-xMin can`t be less than step X");
        int steps = (int)(rangeX/stepX);
        if (steps==0)
            throw new Exception("step is to big");
        double currentX = x[0];
        double[] resX = new double[steps];
        double[] resY = new double[steps];

        for (int i = 0; i < steps; i++) {//toDo check maybe need steps-1
            currentX+=stepX;
            resX[i] = currentX;
            resY[i] = spline.value(currentX);
        }
        double[][] res = new double[steps][2];
        res[0] = resX;
        res[1] = resY;

        return res;
    }

}






