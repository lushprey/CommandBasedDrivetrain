package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.InsideSubsystem;

public class InsideControlCommand extends Command {
    private final InsideSubsystem insideSubsystem;
    private boolean bandsState = false;
    private boolean indexerState = false;

    public InsideControlCommand(InsideSubsystem insideSubsystem){
        this.insideSubsystem = insideSubsystem;
        addRequirements(insideSubsystem);
    }

    public void toogleBands(){
        bandsState = !bandsState;
        if(bandsState){
            insideSubsystem.powerBands(0.8);
        } else {
            insideSubsystem.powerBands(0);
        }
    }

    public void toogleIndexer(){
        indexerState = !indexerState;
        if(indexerState){
            insideSubsystem.powerIndexer(0.8);
        } else {
            insideSubsystem.powerIndexer(0);
        }

    }
}
