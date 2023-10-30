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
    private void interpolate(double[] x,double[] y,double stepX) throws Exception {
        SplineInterpolator interpolator = new SplineInterpolator();
        PolynomialSplineFunction spline = interpolator.interpolate(x, y);
        double rangeX = x[x.length-1]-x[0];
        if (stepX>rangeX)
            throw new Exception("xMax-xMin can`t be less than step X");
        int steps = (int)(rangeX/stepX);
        if (steps==0)
            throw new Exception("step is to big");
        double x_new = 2.5;
        double y_new = spline.value(x_new);
    }
    private void exsample(){



    }
}






