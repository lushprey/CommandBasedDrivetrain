// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.DrivetrainDriveCommand;
import frc.robot.commands.ShooterToggleCommand;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.commands.IntakeSetCmd;

public class RobotContainer {

  private final Joystick stick1 = new Joystick(0);
  private final Joystick stick2 = new Joystick(1);
  private final Joystick stick3 = new Joystick(2);

  private final DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();
  private final DrivetrainDriveCommand drivetrainDriveCommand = new DrivetrainDriveCommand(drivetrainSubsystem, stick1);
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private final ShooterToggleCommand shooterToggleCommand = new ShooterToggleCommand(shooterSubsystem, stick2);
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();


  public RobotContainer() {
    configureBindings();
  }
  private void configureBindings() {
    CommandScheduler.getInstance().setDefaultCommand(drivetrainSubsystem, drivetrainDriveCommand);
    new JoystickButton(stick2, 1).onTrue(shooterToggleCommand);
    new JoystickButton(stick3, 1).whileTrue(new IntakeSetCmd(intakeSubsystem, true));
    new JoystickButton(stick3, 2).whileTrue(new IntakeSetCmd(intakeSubsystem, false));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
