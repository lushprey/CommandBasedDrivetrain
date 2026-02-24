package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.InsideSubsystem;

public class InsideControlCommand extends Command {
    private final InsideSubsystem insideSubsystem;
    private boolean bandsOn = false;
    private boolean indexerOn = false;

    public InsideControlCommand(InsideSubsystem insideSubsystem, boolean bandsOn, boolean indexerOn){
        this.insideSubsystem = insideSubsystem;
        this.bandsOn = bandsOn;
        this.indexerOn = indexerOn;
        addRequirements(insideSubsystem);
    }

    @Override
    public void initialize() {

        if(bandsOn){
            insideSubsystem.powerBands(0.8);
        } else {
            insideSubsystem.powerBands(0);
        }

        if(indexerOn){
            insideSubsystem.powerIndexer(0.8);
        } else {
            insideSubsystem.powerIndexer(0);
        }

    }
    @Override
    public boolean isFinished() {
       return true;
    }
}
