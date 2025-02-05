// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of--
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.*;
import frc.robot.commands.swervedrive.drivebase.AbsoluteDriveAdv;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import java.io.File;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystem drivebase =
      new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve/neo"));

  // Replace with CommandPS4Controller or CommandJoystick if needed
  final CommandXboxController driverXbox1 = new CommandXboxController(0);
  final CommandXboxController driverXbox2 = new CommandXboxController(1);

  private Shooter shooter = new Shooter();
  private Arm arm = new Arm();
  private Intake intake = new Intake();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    AbsoluteDriveAdv closedAbsoluteDriveAdv =
        new AbsoluteDriveAdv(
            drivebase,
            () ->
                -MathUtil.applyDeadband(driverXbox1.getLeftY(), OperatorConstants.LEFT_Y_DEADBAND),
            () ->
                -MathUtil.applyDeadband(driverXbox1.getLeftX(), OperatorConstants.LEFT_X_DEADBAND),
            () ->
                -MathUtil.applyDeadband(
                    driverXbox1.getRightX(), OperatorConstants.RIGHT_X_DEADBAND),
            driverXbox1.getHID()::getYButtonPressed,
            driverXbox1.getHID()::getAButtonPressed,
            driverXbox1.getHID()::getXButtonPressed,
            driverXbox1.getHID()::getBButtonPressed);

    // Applies deadbands and inverts controls because joysticks
    // are back-right positive while robot
    // controls are front-left positive
    // left stick controls translation
    // right stick controls the desired angle NOT angular rotation
    Command driveFieldOrientedDirectAngle =
        drivebase.driveCommand(
            () ->
                -MathUtil.applyDeadband(driverXbox1.getLeftY(), OperatorConstants.LEFT_Y_DEADBAND),
            () ->
                -MathUtil.applyDeadband(driverXbox1.getLeftX(), OperatorConstants.LEFT_X_DEADBAND),
            () -> -driverXbox1.getRightX(),
            () -> -driverXbox1.getRightY());

    // Applies deadbands and inverts controls because joysticks
    // are back-right positive while robot
    // controls are front-left positive
    // left stick controls translation
    // right stick controls the angular velocity of the robot
    Command driveFieldOrientedAnglularVelocity =
        drivebase.driveCommand(
            () ->
                -MathUtil.applyDeadband(driverXbox1.getLeftY(), OperatorConstants.LEFT_Y_DEADBAND),
            () ->
                -MathUtil.applyDeadband(driverXbox1.getLeftX(), OperatorConstants.LEFT_X_DEADBAND),
            () -> -driverXbox1.getRightX());

    Command driveFieldOrientedDirectAngleSim =
        drivebase.simDriveCommand(
            () ->
                -MathUtil.applyDeadband(driverXbox1.getLeftY(), OperatorConstants.LEFT_Y_DEADBAND),
            () ->
                -MathUtil.applyDeadband(driverXbox1.getLeftX(), OperatorConstants.LEFT_X_DEADBAND),
            () -> -driverXbox1.getRawAxis(2));

    drivebase.setDefaultCommand(
        !RobotBase.isSimulation()
            ? driveFieldOrientedAnglularVelocity
            : driveFieldOrientedDirectAngleSim);
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    // driverXbox.b().whileTrue(
    //     Commands.deferredProxy(() -> drivebase.driveToPose(
    //                                new Pose2d(new Translation2d(4, 4),
    // Rotation2d.fromDegrees(0)))
    //                           ));
    // driverXbox.x().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
    driverXbox1.a().onTrue((Commands.runOnce(drivebase::zeroGyro)));

    driverXbox2.rightBumper().whileTrue(new ShootCommand(shooter, 1));
    driverXbox2.button(1).whileTrue(new ArmDown(arm));

    driverXbox2.button(2).whileTrue(new ArmUp(arm));

    driverXbox2.button(4).whileTrue(new MoveArm(arm, 0.7));
    driverXbox2.button(3).whileTrue(new MoveArm(arm, -0.7));

    driverXbox2.leftBumper().whileTrue(new IntakeRun(intake, -1));
    driverXbox2.leftTrigger().whileTrue(new IntakeRun(intake, 0.5));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return drivebase.getAutonomousCommand("New Auto");
  }

  public void setDriveMode() {
    // drivebase.setDefaultCommand();
  }

  public void setMotorBrake(boolean brake) {
    drivebase.setMotorBrake(brake);
  }

  public void resetHeading() {
    drivebase.zeroGyro();
  }
}
