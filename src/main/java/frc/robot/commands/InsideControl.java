package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.InsideSubsystem;

public class InsideControl extends Command {

    private final InsideSubsystem insideSubsystem;
    private final Joystick stick;

    // botones (elige los que quieras)
    private static final int BANDS_BUTTON = 3;
    private static final int INDEXER_BUTTON = 4;

    private boolean bandsOn = false;
    private boolean indexerOn = false;

    // para detectar flanco (press, no hold)
    private boolean lastBandsButton = false;
    private boolean lastIndexerButton = false;

    public InsideControl(InsideSubsystem insideSubsystem, Joystick stick) {
        this.insideSubsystem = insideSubsystem;
        this.stick = stick;
        addRequirements(insideSubsystem);
    }

    @Override
    public void execute() {

        boolean bandsButton = stick.getRawButton(BANDS_BUTTON);
        boolean indexerButton = stick.getRawButton(INDEXER_BUTTON);

        // TOGGLE bandas
        if (bandsButton && !lastBandsButton) {
            bandsOn = !bandsOn;
            insideSubsystem.powerBands(bandsOn ? 0.8 : 0.0);
        }

        // TOGGLE indexer
        if (indexerButton && !lastIndexerButton) {
            indexerOn = !indexerOn;
            insideSubsystem.powerIndexer(indexerOn ? 0.8 : 0.0);
        }

        lastBandsButton = bandsButton;
        lastIndexerButton = indexerButton;
    }

    @Override
    public boolean isFinished() {
        return false; // comando continuo
    }
}