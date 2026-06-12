package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.ArrayList;
import java.util.Collections;

public abstract class LocalisationMT1 extends OpMode {

    public Limelight3A limelight;
    public com.pedropathing.follower.Follower follower;

    // MT1 Filter Thresholds
    public double minimumTa = 0.2;
    public double maximumTx = 20.0; // Bumped to 20 to give you a wider usable FOV
    public double maximumDistanceJumped = 6.0; // Tightened track limits to 6 inches

    // Median Filter Setup (5-frame buffer for 0.1s latency)
    private ArrayList<Double> xBuffer = new ArrayList<>();
    private ArrayList<Double> yBuffer = new ArrayList<>();
    private final int FILTER_SIZE = 5;

    //@Override
    public void init() {
    }

    //@Override
    public void loop() {
    }

    public void initHardware(){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(8);
    }

    public void startLimelight(){
        limelight.start();
    }

    // Helper method to calculate the median without throwing off the timeline
    private double getMedian(ArrayList<Double> buffer, double newValue) {
        buffer.add(newValue);
        if (buffer.size() > FILTER_SIZE) {
            buffer.remove(0); // Drop the oldest frame
        }

        // Clone and sort to find the middle value
        ArrayList<Double> sortedBuffer = new ArrayList<>(buffer);
        Collections.sort(sortedBuffer);

        return sortedBuffer.get(sortedBuffer.size() / 2);
    }

    public void updateLocation(){
        LLResult llResult = limelight.getLatestResult();

        if(llResult != null && llResult.isValid()){
            double ta = llResult.getTa();
            double tx = llResult.getTx();

            // 1. Lens Distortion Filter
            if (ta > minimumTa && Math.abs(tx) < maximumTx) {
                Pose3D botpose = llResult.getBotpose();

                // Get coordinates in inches
                double limelightXinInch = botpose.getPosition().x * 39.37;
                double limelightYinInch = botpose.getPosition().y * 39.37;

                // 2. Map Limelight's center-origin (0,0) to Pedro's corner-origin (72,72)
                double mappedPedroX = limelightXinInch + 72.0;
                double mappedPedroY = limelightYinInch + 72.0;

                // 3. Pass through the Median Filter to kill micro-jitters
                double filteredX = getMedian(xBuffer, mappedPedroX);
                double filteredY = getMedian(yBuffer, mappedPedroY);

                // 4. Proximity Check against Pedro odometry using the filtered & mapped coords
                double currentX = follower.getPose().getX();
                double currentY = follower.getPose().getY();
                double distanceDelta = Math.hypot(filteredX - currentX, filteredY - currentY);

                // 5. High-Trust Exception or standard proximity pass
                if (distanceDelta < maximumDistanceJumped || ta > 1.5) {
                    telemetry.addData("MT1 Status", "Accepted");
                    telemetry.addData("Filtered X", filteredX-72.0);
                    telemetry.addData("Filtered Y", filteredY-72.0);
                } else {
                    telemetry.addData("MT1 Status", "Rejected - Jump > 6 inches");
                    telemetry.addData("Attempted X", filteredX);
                    telemetry.addData("Attempted Y", filteredY);
                }
            } else {
                telemetry.addData("MT1 Status", "Rejected - Tag too small/off-center");
            }
        }
        else{
            telemetry.addData("Limelight Pose", false);
        }

        telemetry.update();
    }
}
