// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.DrivetrainDrive;
import frc.robot.commands.DrivetrainTagAlign;
import frc.robot.commands.DrivetrainTagAlignPIDTunning;
import frc.robot.commands.ShooterAlign;
import frc.robot.commands.ShooterManualControl;
import frc.robot.commands.InsideControl;
import frc.robot.commands.IntakeToggle;
import frc.robot.commands.ShooterToggle;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.InsideSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class RobotContainer {

  private final Joystick stick1 = new Joystick(0);
  private final Joystick stick2 = new Joystick(1);

  private final DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final CameraSubsystem cameraSubsystem = new CameraSubsystem();
  private final InsideSubsystem insideSubsystem = new InsideSubsystem();

  private final DrivetrainDrive drivetrainDrive = new DrivetrainDrive(drivetrainSubsystem, stick1);
  private final ShooterToggle shooterToggle = new ShooterToggle(shooterSubsystem, stick2);
  private final IntakeToggle intakeToggle = new IntakeToggle(intakeSubsystem);
  private final ShooterAlign shooterAlign = new ShooterAlign(shooterSubsystem, cameraSubsystem);
  private final InsideControl insideControl = new InsideControl(insideSubsystem, stick2);
  private final ShooterManualControl shooterManualControl = new ShooterManualControl(shooterSubsystem, stick2);
  private final DrivetrainTagAlign drivetrainTagAlign = new DrivetrainTagAlign(drivetrainSubsystem, cameraSubsystem);
  private final DrivetrainTagAlignPIDTunning drivetrainTagAlignPIDTunning = new DrivetrainTagAlignPIDTunning(drivetrainSubsystem, cameraSubsystem);


  public RobotContainer() {
    configureBindings();
  }

  // STICK 1 (Driver)
  // Axis Y: Forward/Back
  // Axis X: Turn
  // Btn 1: Auto align (AprilTag)

  // STICK 2 (Operator)
  // Btn 1: Intake toggle
  // Btn 2: Shooter toggle

  private void configureBindings() {
    //Desactivados temporalmente
    CommandScheduler.getInstance().setDefaultCommand(drivetrainSubsystem, drivetrainDrive);

    //Elegir entre manual y auto
    //CommandScheduler.getInstance().setDefaultCommand(shooterSubsystem, shooterAlign);
    CommandScheduler.getInstance().setDefaultCommand(shooterSubsystem, shooterManualControl);

    //Elegir entre tuning y pid
    //new JoystickButton(stick1, 1).toggleOnTrue(drivetrainTagAlign);
    //new JoystickButton(stick1, 1).toggleOnTrue(drivetrainTagAlignPIDTunning);

    CommandScheduler.getInstance().setDefaultCommand(insideSubsystem, insideControl);

    new JoystickButton(stick2, 1).onTrue(intakeToggle);

    new JoystickButton(stick2, 2).onTrue(shooterToggle);
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}