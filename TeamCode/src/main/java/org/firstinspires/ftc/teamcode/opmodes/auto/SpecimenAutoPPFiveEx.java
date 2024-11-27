package org.firstinspires.ftc.teamcode.opmodes.auto;
import androidx.collection.ArraySet;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.commands.ActionCommand;
import org.firstinspires.ftc.teamcode.commands.ColourAwareIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeOffCommand;
import org.firstinspires.ftc.teamcode.commands.IntakePivotDownCommand;
import org.firstinspires.ftc.teamcode.commands.IntakePoopChuteOpenCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeSlidesInCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeSlidesOutCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeSlidesOutHalfCommand;
import org.firstinspires.ftc.teamcode.commands.OpenGripplerCommand;
import org.firstinspires.ftc.teamcode.commands.OuttakeOnCommand;
import org.firstinspires.ftc.teamcode.commands.TransferFlipCommand;
import org.firstinspires.ftc.teamcode.commands.TransferStowCommand;
import org.firstinspires.ftc.teamcode.commands.groups.AutoIntakeCommandGroup;
import org.firstinspires.ftc.teamcode.commands.groups.BackDumpCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.AscentSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.RobotStateSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SlidesSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;
import org.firstinspires.ftc.teamcode.utils.PinpointDrive;

@Autonomous(name = "[OLD] Specimen PP Exper", group = "Autonomous")
@Disabled
//grabs the three samples, delivers five specimens
public class SpecimenAutoPPFiveEx extends CommandOpMode {

    TrajectoryActionBuilder dropOffPreload;
    TrajectoryActionBuilder firstSample;
    TrajectoryActionBuilder firstSampleSlow;
    TrajectoryActionBuilder firstSampleDrop;
    TrajectoryActionBuilder secondSample;
    TrajectoryActionBuilder secondSampleSlow;
    TrajectoryActionBuilder secondSampleDrop;
    TrajectoryActionBuilder thirdSample;
    TrajectoryActionBuilder thirdSampleDrop;

    TrajectoryActionBuilder thirdSampleSlow;
    TrajectoryActionBuilder firstSpecimenPickup;
    TrajectoryActionBuilder firstSpecimenDrop;
    TrajectoryActionBuilder secondSpecimenPickup;
    TrajectoryActionBuilder secondSpecimenDrop;
    TrajectoryActionBuilder thirdSpecimenPickup;

    TrajectoryActionBuilder thirdSpecimenDrop;


    IntakeSubsystem intakeSubsystem;

    TransferSubsystem transferSubsystem;

    RobotStateSubsystem robotState;

    SlidesSubsystem slidesSubsystem;

    AscentSubsystem ascentSubsystem;

    //Change these offsets, they can be negative values
    int X_OFFSET = 0; // a larger negative number takes it closer to the basket
    int Y_OFFSET = 0; // a larger number takes it closer to the submersible

    @Override
    public void initialize() {

        intakeSubsystem = new IntakeSubsystem(hardwareMap, telemetry);
        transferSubsystem = new TransferSubsystem(hardwareMap);
        robotState = new RobotStateSubsystem();
        slidesSubsystem = new SlidesSubsystem(hardwareMap);
        ascentSubsystem = new AscentSubsystem(hardwareMap);


        // instantiate your MecanumDrive at a particular pose.
        PinpointDrive drive = new PinpointDrive(hardwareMap,
                new Pose2d(15, -74, Math.toRadians(90)));

        Pose2d firstSamplePose = new Pose2d(36,-68,Math.toRadians(90));

        firstSample = drive.actionBuilder(drive.pose)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(firstSamplePose,Math.toRadians(90))
                .endTrajectory();

        Vector2d firstSampleSlowPose = new Vector2d(36,-58);

        firstSampleSlow = firstSample.fresh()
                .strafeTo(firstSampleSlowPose, new TranslationalVelConstraint(10))
                .endTrajectory();

        firstSampleDrop = firstSampleSlow.fresh()
                .strafeTo(new Vector2d(36, -65))
                .endTrajectory();

        Pose2d secondSamplePose = new Pose2d(46,-68,Math.toRadians(90));

        secondSample = firstSampleDrop.fresh()
                 .setTangent(Math.toRadians(90))
                 .splineToLinearHeading(secondSamplePose, Math.toRadians(90))
                 .endTrajectory();

        Vector2d secondSampleSlowPose = new Vector2d(46,-58);

        secondSampleSlow = secondSample.fresh()
                .strafeTo(secondSampleSlowPose, new TranslationalVelConstraint(10))
                .endTrajectory();

        secondSampleDrop = secondSampleSlow.fresh()
                .strafeTo(new Vector2d(40, -65))
                .endTrajectory();

        Pose2d thirdSamplePose = new Pose2d(48,-68,Math.toRadians(60));

        thirdSample = secondSampleDrop.fresh()
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(thirdSamplePose, Math.toRadians(60))
                .endTrajectory();

        thirdSampleSlow = thirdSample.fresh()
                .strafeTo(new Vector2d(42, -63))
                .endTrajectory();

        Pose2d thirdSampleDropPose = new Pose2d(48,-60,Math.toRadians(90));

        thirdSampleDrop = thirdSample.fresh()
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(thirdSampleDropPose, Math.toRadians(90))
                .endTrajectory();



        //first specimen
        Pose2d firstSpecimenPickupPose = new Pose2d(13,-47,Math.toRadians(-45));

        firstSpecimenPickup = thirdSampleDrop.fresh()
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(firstSpecimenPickupPose, Math.toRadians(-45))
                .endTrajectory();

        Pose2d firstSpecimenDropPose = new Pose2d(-5, -32.5, Math.toRadians(-90));

        firstSpecimenDrop = firstSpecimenPickup.fresh()
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(firstSpecimenDropPose, Math.toRadians(90))
                .endTrajectory();

        //second specimen
        Pose2d secondSpecimenPickupPose = new Pose2d(13,-47,Math.toRadians(-45));

        secondSpecimenPickup = firstSpecimenDrop.fresh()
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(secondSpecimenPickupPose, Math.toRadians(-45))
                .endTrajectory();

        Pose2d secondSpecimenDropPose = new Pose2d(-6, -32.5, Math.toRadians(-90));

        secondSpecimenDrop = secondSpecimenPickup.fresh()
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(secondSpecimenDropPose, Math.toRadians(90))
                .endTrajectory();

        //third specimen
        Pose2d thirdSpecimenPickupPose = new Pose2d(13,-47,Math.toRadians(-45));

        thirdSpecimenPickup = secondSpecimenDrop.fresh()
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(thirdSpecimenPickupPose, Math.toRadians(-45))
                .endTrajectory();

        Pose2d thirdSpecimenDropPose = new Pose2d(-7, -32.5, Math.toRadians(-90));

        thirdSpecimenDrop = thirdSpecimenPickup.fresh()
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(firstSpecimenDropPose, Math.toRadians(90))
                .endTrajectory();

        intakeSubsystem.setDesiredColour(IntakeSubsystem.SampleColour.AUTO_ANY);
        intakeSubsystem.intakePivotDown();
        transferSubsystem.openGrippler();


        CommandScheduler.getInstance().schedule(
                new WaitUntilCommand(this::isStarted).andThen(
                    new SequentialCommandGroup(

                            //first sample
                            new ParallelCommandGroup(
                                new ActionCommand(firstSample.build(), new ArraySet<>()),
                                new SequentialCommandGroup(
                                    new WaitCommand(1200),
                                    new IntakePoopChuteOpenCommand(intakeSubsystem),
                                    new IntakeSlidesOutCommand(intakeSubsystem),
                                    new IntakePivotDownCommand(intakeSubsystem, robotState)
                                )
                            ),
                            new OpenGripplerCommand(transferSubsystem),
                            new ParallelCommandGroup(
                                    new ColourAwareIntakeCommand(intakeSubsystem).withTimeout(400),
                                    new ActionCommand(firstSampleSlow.build(), new ArraySet<>())
                            ),
                            new ParallelCommandGroup(
                                new AutoIntakeCommandGroup(intakeSubsystem,transferSubsystem, robotState),

                                new ActionCommand(firstSampleDrop.build(), new ArraySet<>())
                            ),

                            new BackDumpCommandGroup(slidesSubsystem, transferSubsystem,robotState),


                            //second sample
                            new ParallelCommandGroup(
                                    new ActionCommand(secondSample.build(), new ArraySet<>()),
                                    new SequentialCommandGroup(
                                        new IntakePivotDownCommand(intakeSubsystem, robotState),
                                        new IntakePoopChuteOpenCommand(intakeSubsystem),
                                        new IntakeSlidesOutCommand(intakeSubsystem)
                                    )
                            ),
                            new OpenGripplerCommand(transferSubsystem),
                            new ParallelCommandGroup(

                                    new ColourAwareIntakeCommand(intakeSubsystem).withTimeout(400),
                                    new ActionCommand(secondSampleSlow.build(), new ArraySet<>())
                            ),
                            new ParallelCommandGroup(
                                    new AutoIntakeCommandGroup(intakeSubsystem,transferSubsystem, robotState),
                                    new ActionCommand(secondSampleDrop.build(), new ArraySet<>())
                            ),

                            new BackDumpCommandGroup(slidesSubsystem, transferSubsystem,robotState),


                            //third sample
                            new ParallelCommandGroup(
                                    new ActionCommand(thirdSample.build(), new ArraySet<>()),
                                    new SequentialCommandGroup(
                                        new IntakePivotDownCommand(intakeSubsystem, robotState),
                                        new IntakePoopChuteOpenCommand(intakeSubsystem),
                                        new IntakeSlidesOutCommand(intakeSubsystem)
                                    )
                            ),
                            new OpenGripplerCommand(transferSubsystem),
                            new ParallelCommandGroup(
                                new ColourAwareIntakeCommand(intakeSubsystem).withTimeout(400),
                                new ActionCommand(thirdSampleSlow.build(), new ArraySet<>())
                            ),

                            new ParallelCommandGroup(
                                new ActionCommand(thirdSampleDrop.build(), new ArraySet<>()),
                                new AutoIntakeCommandGroup(intakeSubsystem,transferSubsystem, robotState)

                            ),

                            new BackDumpCommandGroup(slidesSubsystem, transferSubsystem,robotState),


                            //first specimen drop
                            new ActionCommand(firstSpecimenPickup.build(), new ArraySet<>()),
                            new WaitCommand(1000),
                            new ColourAwareIntakeCommand(intakeSubsystem).withTimeout(700),
                            new ParallelCommandGroup(
                                new ActionCommand(firstSpecimenDrop.build(), new ArraySet<>()),
                                new AutoIntakeCommandGroup(intakeSubsystem,transferSubsystem, robotState)
                            ),
                            new WaitCommand(500),
                            new TransferFlipCommand(transferSubsystem),
                            new WaitCommand(500),
                            new OpenGripplerCommand(transferSubsystem),
                            new TransferStowCommand(transferSubsystem),
                            new IntakePoopChuteOpenCommand(intakeSubsystem),
                            new IntakeSlidesOutCommand(intakeSubsystem),

                            //second specimen drop
                            new ActionCommand(secondSpecimenPickup.build(), new ArraySet<>()),
                            new WaitCommand(1000),
                            new ColourAwareIntakeCommand(intakeSubsystem).withTimeout(700),
                            new ParallelCommandGroup(
                                    new ActionCommand(secondSpecimenDrop.build(), new ArraySet<>()),
                                    new AutoIntakeCommandGroup(intakeSubsystem,transferSubsystem, robotState)
                            ),
                            new WaitCommand(500),
                            new TransferFlipCommand(transferSubsystem),
                            new WaitCommand(500),
                            new OpenGripplerCommand(transferSubsystem),
                            new TransferStowCommand(transferSubsystem),
                            new IntakePoopChuteOpenCommand(intakeSubsystem),
                            new IntakeSlidesOutCommand(intakeSubsystem),

                            //third specimen drop
                            new ActionCommand(thirdSpecimenPickup.build(), new ArraySet<>()),
                            new WaitCommand(1000),
                            new ColourAwareIntakeCommand(intakeSubsystem).withTimeout(700),
                            new ParallelCommandGroup(
                                    new ActionCommand(thirdSpecimenDrop.build(), new ArraySet<>()),
                                    new AutoIntakeCommandGroup(intakeSubsystem,transferSubsystem, robotState)
                            ),
                            new WaitCommand(500),
                            new TransferFlipCommand(transferSubsystem),
                            new WaitCommand(500),
                            new OpenGripplerCommand(transferSubsystem),
                            new TransferStowCommand(transferSubsystem),
                            new IntakePoopChuteOpenCommand(intakeSubsystem),

                            //fourth specimen drop
                            new ActionCommand(thirdSpecimenPickup.build(), new ArraySet<>()),
                            new WaitCommand(1000),
                            new ColourAwareIntakeCommand(intakeSubsystem).withTimeout(700),
                            new ParallelCommandGroup(
                                    new ActionCommand(thirdSpecimenDrop.build(), new ArraySet<>()),
                                    new AutoIntakeCommandGroup(intakeSubsystem,transferSubsystem, robotState)
                            ),
                            new WaitCommand(500),
                            new TransferFlipCommand(transferSubsystem),
                            new WaitCommand(500),
                            new OpenGripplerCommand(transferSubsystem),
                            new TransferStowCommand(transferSubsystem),
                            new IntakePoopChuteOpenCommand(intakeSubsystem),

                            //fifth specimen drop
                            new ActionCommand(thirdSpecimenPickup.build(), new ArraySet<>()),
                            new WaitCommand(1000),
                            new ColourAwareIntakeCommand(intakeSubsystem).withTimeout(700),
                            new ParallelCommandGroup(
                                    new ActionCommand(thirdSpecimenDrop.build(), new ArraySet<>()),
                                    new AutoIntakeCommandGroup(intakeSubsystem,transferSubsystem, robotState)
                            ),
                            new WaitCommand(500),
                            new TransferFlipCommand(transferSubsystem),
                            new WaitCommand(500),
                            new OpenGripplerCommand(transferSubsystem),
                            new TransferStowCommand(transferSubsystem),
                            new IntakePoopChuteOpenCommand(intakeSubsystem)
                    )
                )

        );


    }

}
