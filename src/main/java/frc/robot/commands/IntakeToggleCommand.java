package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeSetCmd extends CommandBase {
    private final Joystick stick;

    private final IntakeSubsystem intakeSubsystem;
    private final boolean open;

    public IntakeSetCmd(IntakeSubsystem intakeSubsystem, boolean open) {
        this.open = open;
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void initialize() {
        System.out.println("Intake has started");
    }

    @Override
    public void execute() {
        intakeSubsystem.setPosition(open);
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.stop(); 
        System.out.println("Intake has ended");
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
}