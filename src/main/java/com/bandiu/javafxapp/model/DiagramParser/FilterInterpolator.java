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
    public void shiftScaleX(double shift){
        for (int i = 0; i < x.length; i++) {
            x[i]=x[i]+shift;
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

    public void interpolateFromToWidthStep (double valueX_From, double valueX_To, double stepX) throws Exception {

        int indexXfrom = getIndexByValue(valueX_From,x);
        int indexXto = getIndexByValue(valueX_To,x);

        checkRange(indexXfrom,indexXto);

        int amountOfValue = indexXto-indexXfrom;

        double[] dataToInterpolateX = new double[indexXto-indexXfrom];
        double[] dataToInterpolateY = new double[indexXto-indexXfrom];
        for (int i = 0; i < dataToInterpolateY.length; i++) {
            dataToInterpolateX[i]=x[indexXfrom+i];
            dataToInterpolateY[i]=y[indexXfrom+i];
        }
        double[][] interpolatedData = interpolate(dataToInterpolateX,dataToInterpolateY,stepX);
        replaceData(indexXfrom,indexXto,interpolatedData);
    }
    private void replaceData(int indexFrom,int indexTo,double[][] data) throws Exception {
        double[] newX = data[0];
        double[] newY = data[1];

        replaceData(indexFrom,indexTo,newX,newY);
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
    public void smoothFromToWithWindow(double valueX_From,double valueX_To,int window) throws Exception {
        int indexFrom = getIndexByValue(valueX_From,x);
        int indexTo = getIndexByValue(valueX_To,x);
        double[][] smoothedData = smooth(indexFrom,indexTo,window);
        replaceData(indexFrom,indexTo,smoothedData);
    }
    private int getIndexByValue(double value,double[] array){
        for (int i = 0; i < array.length; i++) {
            if(value<=array[i])
                return i;
        }
        return array.length-1;
    }
    private void replaceData(int indexFrom, int indexTo, double[] newX, double[] newY) throws Exception {
        checkRange(indexFrom,indexTo);
        int dIndex = indexTo-indexFrom;
        int newLength = x.length-dIndex+newX.length+1;
        double[] resX = new double[newLength];
        double[] resY = new double[newLength];
        int index = -1;
        for (int i = 0; i <= indexFrom; i++) {
            index++;
            resX[index] = x[i];
            resY[index] = y[i];

        }
        for (int i = 0; i < newX.length; i++) {
            index++;
            resX[index]=newX[i];
            resY[index]=newY[i];

        }

        for (int i = 0; i < x.length-indexTo; i++) {
            index++;
            resX[index]=x[i+indexTo];
            resY[index]=y[i+indexTo];

        }
        x=resX;
        y=resY;
    }
    /*private double[][] smooth(int indexFrom,int indexTo,int window) throws Exception {
        checkRange(indexFrom,indexTo);
        if ((indexTo-indexFrom)<window)
            throw new RuntimeException("window is to big");
        int amountNewValue = (int)((double)(indexTo-indexFrom)/(double) window);
        double[] Xnew = new double[amountNewValue];
        double[] Ynew = new double[amountNewValue];

        for (int i = 0; i < amountNewValue; i++) {
            int indStart = i*window;
            int indStop = x.length-1;
            if ((i*window+window)<(x.length-1)){
                indStop = indStart+window;
            }
                Xnew[i]=average(x,indStart,indStop);
                Ynew[i]=average(y,indStart,indStop);

        }

        double res[][] = new double[amountNewValue][2];
        res[0]=Xnew;
        res[1]=Ynew;
        return res;
    }

     */

    private double[][] smooth(int indexFrom, int indexTo, int window) throws Exception {
        checkRange(indexFrom, indexTo);

        int fullWindows = (indexTo - indexFrom) / window;
        int remainder = (indexTo - indexFrom) % window;

        int amountNewValue = remainder == 0 ? fullWindows : fullWindows + 1;
        double[] Xnew = new double[amountNewValue];
        double[] Ynew = new double[amountNewValue];

        for (int i = 0; i < fullWindows; i++) {
            int indStart = indexFrom + i * window;
            int indStop = indStart + window - 1;

            Xnew[i] = average(x, indStart, indStop);
            Ynew[i] = average(y, indStart, indStop);
        }

        if (remainder != 0) {
            int indStart = indexFrom + fullWindows * window;
            int indStop = indexTo;

            Xnew[fullWindows] = average(x, indStart, indStop);
            Ynew[fullWindows] = average(y, indStart, indStop);
        }

        double[][] res = new double[2][];
        res[0] = Xnew;
        res[1] = Ynew;
        return res;
    }

    private double average(double[] array,int indexStart,int indexStop){
        int amount = indexStop-indexStart+1;
        double sum = 0.0;
        for (int i = indexStart; i <= indexStop; i++) {
            sum+=array[i];
        }
        return sum/amount;
    }
    private double[][] interpolate(double[] X,double[] Y,double stepX) throws Exception {
        SplineInterpolator interpolator = new SplineInterpolator();
        PolynomialSplineFunction spline = interpolator.interpolate(X, Y);
        double rangeX = X[X.length-1]-X[0];
        if (stepX>rangeX)
            throw new Exception("xMax-xMin can`t be less than step X");
        int steps = (int)(rangeX/stepX);
        if (steps==0)
            throw new Exception("step is to big");
        double currentX = X[0];
        double[] resX = new double[steps];
        double[] resY = new double[steps];

        for (int i = 0; i < steps; i++) {
            currentX+=stepX;
            resX[i] = currentX;
            resY[i] = spline.value(currentX);

        }
        double[][] res = new double[steps][2];
        res[0] = resX;
        res[1] = resY;

        return res;
    }

    public static void main(String[] args) {
        double[] xV = new double[]{0.0,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0};
        double[] xY = new double[]{0.0,1.0,4.0,9.0,16.0,25.0,36.0,49.0,64.0,81.0,100.0};
        FilterInterpolator filterInterpolator = new FilterInterpolator(xV,xY);

        try {

            filterInterpolator.interpolateFromToWidthStep(1,6,0.1);
            filterInterpolator.smoothFromToWithWindow(0,10,24);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        double[] resX = filterInterpolator.getNewX();
        double[] resY = filterInterpolator.getNewY();

        for (int i = 0; i < resX.length; i++) {
            System.out.println(resX[i] +"  ;  "+resY[i]);
        }
    }

}






