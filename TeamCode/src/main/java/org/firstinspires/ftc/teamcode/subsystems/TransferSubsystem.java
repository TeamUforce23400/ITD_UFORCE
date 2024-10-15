package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class TransferSubsystem extends SubsystemBase {

    //Define motors and servos
    private Servo armLeftServo;
    private Servo armRightServo;
    private Servo gripplerServo;
    private Servo griggleWristServo;

    // Define variables
    private double backwardsTransferPosition = 0;
    private double stowedTransferPosition = 0;
    private double flippedPosition = 1;
    private double middleGripplerRotation = 0;
    private double leftGripplerRotation = 0;
    private double rightGripplerRotation = 0;
    private double closedGripplerPosition = 0;
    private double openGripplerPosition = 0.3;

    public TransferSubsystem(final HardwareMap hMap) {
        armLeftServo = hMap.get(Servo.class, "armLeft");
        armRightServo = hMap.get(Servo.class, "armRight");
        gripplerServo = hMap.get(Servo.class, "grippler");
        griggleWristServo = hMap.get(Servo.class, "griggleWrist");

        armRightServo.setDirection(Servo.Direction.REVERSE);

        stowTransfer();
        griggleWristServo.setPosition(0.5);
        closeGrippler();
    }

    public void backwardsTransfer() {
        armLeftServo.setPosition(backwardsTransferPosition);
        armRightServo.setPosition(backwardsTransferPosition);
    }

    public boolean IsTransferBackwards() {return true;}

    public void stowTransfer() {
        armLeftServo.setPosition(stowedTransferPosition);
        armRightServo.setPosition(stowedTransferPosition);
    }

    public boolean IsTransferStowed() {
        return true;
    }

    public void flipTransfer() {
        armLeftServo.setPosition(flippedPosition);
        armRightServo.setPosition(flippedPosition);
    }

    public boolean IsTransferFlipped() {
        return true;
    }

    public void middleGripplerRotation() {
        griggleWristServo.setPosition(middleGripplerRotation);
    }

    public boolean IsGripplerAtMiddleRotation() {
        return true;
    }

    public void leftGripplerRotation() {
        griggleWristServo.setPosition(leftGripplerRotation);
    }

    public boolean IsGripplerAtLeftRotation() {
        return true;
    }

    public void rightGripplerRotation() {
        griggleWristServo.setPosition(rightGripplerRotation);
    }

    public boolean IsGripplerAtRightRotation() {
        return true;
    }

    public void openGrippler() {
        gripplerServo.setPosition(openGripplerPosition);
    }

    public boolean IsGripplerOpen() {
        return true;
    }

    public void closeGrippler() {
        gripplerServo.setPosition(closedGripplerPosition);
    }

    public boolean IsGripplerClosed() {
        return true;
    }
}