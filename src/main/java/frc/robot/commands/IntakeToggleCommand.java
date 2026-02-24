package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeToggleCommand extends Command {

    private final IntakeSubsystem intakeSubsystem;

    public IntakeToggleCommand(IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void initialize() {
        intakeSubsystem.motorToggle();
        intakeSubsystem.pistonToggle();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}