package com.bandiu.javafxapp.model;

import java.util.ArrayList;
import java.util.Arrays;

public class ChartData {
    private ArrayList<Double> X_values = new ArrayList<>();
    private ArrayList<Double> Y_values = new ArrayList<>();

    public void addValue(double X,double Y){
        X_values.add(X);
        Y_values.add(Y);
    }
    public int size(){
        if (X_values.size()!= Y_values.size())
            throw new RuntimeException("Something wrong with sizes of arrays X and Y");
        return X_values.size();
    }
    public double[] getX_values(){
        return X_values.stream().mapToDouble(Double::doubleValue).toArray();

    }

    public double[] getY_values(){
        return Y_values.stream().mapToDouble(Double::doubleValue).toArray();

    }
}
