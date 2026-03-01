package frc.robot;

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import choreo.auto.AutoFactory;

import frc.robot.commands.DrivetrainDrive;
import frc.robot.commands.DrivetrainTagAlign;
import frc.robot.commands.DrivetrainTagAlignPIDTunning;
import frc.robot.commands.ShooterAlign;
import frc.robot.commands.ShooterManualControl;
import frc.robot.commands.InsideControl;
import frc.robot.commands.IntakeToggle;

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

  private final DrivetrainDrive drivetrainDrive =
      new DrivetrainDrive(drivetrainSubsystem, stick1);

  private final IntakeToggle intakeToggle =
      new IntakeToggle(intakeSubsystem);

  // Desactivados temporalmente
  // private final ShooterAlign shooterAlign =
  //     new ShooterAlign(shooterSubsystem, cameraSubsystem);

  private final InsideControl insideControl =
      new InsideControl(insideSubsystem, stick2);

  private final ShooterManualControl shooterManualControl =
      new ShooterManualControl(shooterSubsystem, stick2);

  // Desactivados temporalmente
  // private final DrivetrainTagAlign drivetrainTagAlign =
  //     new DrivetrainTagAlign(drivetrainSubsystem, cameraSubsystem);

  // private final DrivetrainTagAlignPIDTunning drivetrainTagAlignPIDTunning =
  //     new DrivetrainTagAlignPIDTunning(drivetrainSubsystem, cameraSubsystem);

  
  // ===============================
  // AUTO (Choreo)
  // ===============================
  private final AutoFactory autoFactory;
  private final Auto auto;

  public RobotContainer() {

    // -------- AutoFactory --------
        autoFactory = new AutoFactory(
            drivetrainSubsystem::getPose, // A function that returns the current robot pose
            drivetrainSubsystem::resetOdometry, // A function that resets the current robot pose to the provided Pose2d
            drivetrainSubsystem::followTrajectory, // The drive subsystem trajectory follower 
            true, // If alliance flipping should be enabled 
            drivetrainSubsystem // The drive subsystem
        );

    // -------- Auto wrapper --------
    auto = new Auto(
        autoFactory,
        drivetrainSubsystem,
        shooterSubsystem,
        insideSubsystem
    );

    configureBindings();
  }

  private void configureBindings() {

    // Desactivados temporalmente
    CommandScheduler.getInstance()
        .setDefaultCommand(drivetrainSubsystem, drivetrainDrive);

    // Elegir entre manual y auto
    // CommandScheduler.getInstance().setDefaultCommand(shooterSubsystem, shooterAlign);
    CommandScheduler.getInstance()
        .setDefaultCommand(shooterSubsystem, shooterManualControl);

    // Elegir entre tuning y pid
    // new JoystickButton(stick1, 1).toggleOnTrue(drivetrainTagAlign);
    // new JoystickButton(stick1, 1).toggleOnTrue(drivetrainTagAlignPIDTunning);

    CommandScheduler.getInstance()
        .setDefaultCommand(insideSubsystem, insideControl);

    // Intake (NO TOCADO)
    new JoystickButton(stick2, 1).onTrue(intakeToggle);
  }

  // ===============================
  // Autonomous (No terminado)
  // ===============================
  public Command getAutonomousCommand() {
    return auto.getSelectedAuto();
  }
}