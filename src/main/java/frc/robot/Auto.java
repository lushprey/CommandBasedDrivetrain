package frc.robot;

import java.util.Set;

import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.InsideSubsystem;

public class Auto {

    //No terminado

    // ===============================
    // Constantes de nombres Choreo
    // ===============================
    public static final String UP_BLUE     = "AutonomousBpart1_Up_Blue";
    public static final String UP_RED      = "AutonomousBpart1_Up_Red";

    public static final String MID_BLUE    = "AutonomousB_middle_Blue";
    public static final String MID_RED     = "AutonomousB_middle_Red";

    public static final String DOWN_BLUE   = "AutonomousBpart1_Down_Blue";
    public static final String DOWN_RED    = "AutonomousBpart1_Down_Red";

    private final int RPM = 2500;

    // ===============================
    // Choosers
    // ===============================
    private final SendableChooser<Boolean> allianceChooser =
            new SendableChooser<>();

    private final SendableChooser<String> pathChooser =
            new SendableChooser<>();

    private final SendableChooser<Command> autoChooser =
            new SendableChooser<>();

    public Auto(
            AutoFactory autoFactory,
            DrivetrainSubsystem drivetrainSubsystem,
            ShooterSubsystem shooterSubsystem,
            InsideSubsystem insideSubsystem
    ) {

        // -------- Alliance chooser --------
        allianceChooser.setDefaultOption("Blue", false);
        allianceChooser.addOption("Red", true);
        SmartDashboard.putData("Alliance", allianceChooser);

        // -------- Path chooser --------
        pathChooser.setDefaultOption("Up", "UP");
        pathChooser.addOption("Middle", "MID");
        pathChooser.addOption("Down", "DOWN");
        SmartDashboard.putData("Path", pathChooser);

        // -------- Auto chooser (wrapper) --------
        autoChooser.setDefaultOption(
        "Selected Auto",
        Commands.defer(
            () -> buildAuto(
                pathChooser.getSelected(),
                allianceChooser.getSelected(),
                autoFactory,
                drivetrainSubsystem,
                shooterSubsystem,
                insideSubsystem
            ),
            Set.of(
                drivetrainSubsystem,
                shooterSubsystem,
                insideSubsystem
            )
        )
    );
        SmartDashboard.putData("Auto Selector", autoChooser);
    }

    // ===============================
    // Construye el auto según chooser
    // ===============================
    private Command buildAuto(
            String path,
            boolean isRed,
            AutoFactory autoFactory,
            DrivetrainSubsystem drivetrainSubsystem,
            ShooterSubsystem shooterSubsystem,
            InsideSubsystem insideSubsystem
    ) {

        String trajectoryName;

        switch (path) {
            case "MID":
                trajectoryName = isRed ? MID_RED : MID_BLUE;
                break;

            case "DOWN":
                trajectoryName = isRed ? DOWN_RED : DOWN_BLUE;
                break;

            case "UP":
            default:
                trajectoryName = isRed ? UP_RED : UP_BLUE;
                break;
        }

        return singleTrajectoryAuto(
                trajectoryName,
                autoFactory,
                drivetrainSubsystem,
                shooterSubsystem,
                insideSubsystem
        );
    }

    /**
     * Auto genérico para UNA sola trayectoria de Choreo
     */
    private Command singleTrajectoryAuto(
            String trajectoryName,
            AutoFactory autoFactory,
            DrivetrainSubsystem drivetrainSubsystem,
            ShooterSubsystem shooterSubsystem,
            InsideSubsystem insideSubsystem
    ) {
        return Commands.sequence(

            // Resetear odometría
            autoFactory.resetOdometry(trajectoryName),

            // Seguir la trayectoria
            autoFactory.trajectoryCmd(trajectoryName),

            // Preparar shooter
            Commands.runOnce(
                    () -> shooterSubsystem.setAllRPM(RPM),
                    shooterSubsystem
            ),

            // Alimentar y disparar
            Commands.parallel(
                    Commands.run(
                            () -> insideSubsystem.powerBands(0.9),
                            insideSubsystem
                    ),
                    Commands.run(
                            () -> insideSubsystem.powerIndexer(0.9),
                            insideSubsystem
                    )
            )
        );
    }

    public Command getSelectedAuto() {
        return autoChooser.getSelected();
    }
}