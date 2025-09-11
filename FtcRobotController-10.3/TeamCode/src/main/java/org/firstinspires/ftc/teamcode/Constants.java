package org.firstinspires.ftc.teamcode;

public class Constants {
    // AUTO MOVEMENT

    // goToPosition()
    public static final double HEADING_CORRECTION_SPEED = 0.015;
    public static final double MINIMUM_ROTATIONAL_SPEED = 0.25;
    public static final double MINIMUM_HEADING_CORRECTION_SPEED = 0.05;
    public static final double TURNING_SPEED = 5;
    public static final double MAXIMUM_HEADING_ERROR = 2;
    public static final double DECELERATION_MULTIPLIER_POSITIONAL = 1;
    public static final double MINIMUM_SPEED_ACCURATE = 0.3;

    public static final double MAXIMUM_DISTANCE = 50;
    public static final double DECELERATION_LENGTH = 500;
    public static final double MINIMUM_SPEED = 0.5;

    // rotateToHeading()
    public static final double MINIMUM_TURNING_SPEED = 0.7;
    public static final double DECELERATION_MULTIPLIER_TURNING = 0.01;

    //VERTICAL SLIDES
    public static final int VS_TRANSFER = 1270;
    public static final double VS_DECELERATION_LIMIT = 500;
    public static final double MIN_VS_SPEED = 0;
    public static final double EXTRA_VS_UP_POWER = 0.3;

    //LEFT ARM
    public static final double LEFTARM_TRANSFER = 0.04;
    public static final double LEFTARM_CLIP = 0.35;

    // RIGHT ARM
    public static final double RIGHTARM_TRANSFER = 1;
    public static final double RIGHTARM_CLIP = 0.7;

    //VSCLAW
    public static final double VSCLAW_OPEN = 0.85;

    //HSYAW
    public static final double HSYAW_STRAIGHT = 0.65;
    public static final double HSYAW_LEFT = 0.5;
    public static final double HSYAW_RIGHT = 0.8;

    // HSPITCH
    public static final double HSPITCH_DOWN = 0.2;
    public static final double HSPITCH_TRANSFER = 0.9;

    // VSYAW
    public static final double VSYAW_TRANSFER = 0.4;

    // HSCLAW
    public static final double HSCLAW_OPEN = 0;
    public static final double HSCLAW_CLOSED = 0.33;


    public static double ROBOT_WIDTH = 18;
    public static double ROBOT_LENGTH = 18;

}
