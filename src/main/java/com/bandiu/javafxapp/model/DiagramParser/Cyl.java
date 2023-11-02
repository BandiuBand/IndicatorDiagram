package com.bandiu.javafxapp.model.DiagramParser;

import java.util.ArrayList;

public class Cyl {

        private int cylNumber;
        private double[] pressures = new double[359];
        private double[] pressuresAfterFilter = null;
        private double[] anglesAfterFilter = null;
        private int indicatedPower=0;
        private ArrayList<Integer> peakPressures = new ArrayList<>();
        private Double averagePeakPressure;
        private double rpm=0;
        private double pScav = 0;
        private boolean isOptimised = false;

    public double[] getPressuresAfterFilter() {
        if (!isOptimised)
            applyOptimisation();
        return pressuresAfterFilter;
    }

    public double[] getAnglesAfterFilter() {
        if (!isOptimised)
            applyOptimisation();
        return anglesAfterFilter;
    }

    public Cyl(int cylNumber){
            this.cylNumber = cylNumber;
        }

    public void setP_scav(double pScav) {
        this.pScav = pScav;
    }

    public double getP_Scav() {
        return pScav;
    }

    public double getRpm() {
        return rpm;
    }
    public double getAveragePeakPressure(){
            if (averagePeakPressure == null)
            {
                setAveragePeakPressure();
            }
            return averagePeakPressure.doubleValue();
    }

    private void setAveragePeakPressure(){
            int sumPressures = 0;
            if (peakPressures!=null&&!peakPressures.isEmpty()){
                for (int p:peakPressures) {
                    sumPressures = sumPressures+p;
                }
            }else throw new RuntimeException("peakPressures must be filled");
            averagePeakPressure = (double)sumPressures/(double)peakPressures.size();
    }

    public void setRpm(double rpm) {
        this.rpm = rpm;
    }

    public void setIndicatedPower(int indicatedPower) {
        this.indicatedPower = indicatedPower;
    }
    public int getMaxPressureAngle(){
            int angle = 0;
            double maxPresure = 0;
        for (int i = 0; i < pressures.length; i++) {
            if (pressures[i]>maxPresure){
                angle = i-180;
                maxPresure = pressures[i];
            }
        }
        return angle;
    }
    public double getIndicatedPower() {
        return indicatedPower;
    }
    public void addPeakPressures(int value){
            peakPressures.add(value);
    }

    public int getCylNumber() {
        return cylNumber;
    }

    public ArrayList<Integer> getPeakPressures() {
        return peakPressures;
    }

    public double[] getPressures() {
        return pressures;
    }

    public void setPressure(double pressure, int angle) {this.pressures[angle] = pressure;}
    public double getPressure(int angle){return pressures[angle];}

    public void applyOptimisation(){
            FilterInterpolator filterInterpolator = new FilterInterpolator(pressures);
            filterInterpolator.shiftScaleX(-180.0);
            smoothChart(filterInterpolator);
            splineChart(filterInterpolator);
            anglesAfterFilter = filterInterpolator.getNewX();
            pressuresAfterFilter = filterInterpolator.getNewY();
            isOptimised = true;
    }
    private void smoothChart(FilterInterpolator filterInterpolator){
        try {
            filterInterpolator.smoothFromToWithWindow(-180,-10,6);
            filterInterpolator.smoothFromToWithWindow(25,100,10);
            filterInterpolator.smoothFromToWithWindow(100,180,5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private void splineChart(FilterInterpolator filterInterpolator){
        try {
            filterInterpolator.interpolateFromToWidthStep(-5,3,0.25);
            filterInterpolator.interpolateFromToWidthStep(8,14,0.25);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

