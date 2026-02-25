package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterManualControl extends Command {

    private final ShooterSubsystem shooterSubsystem;
    private final Joystick stick;

    // Estados internos
    private int shooterRPM = 0;
    private int hoodAngle = 90;

    // Constantes de ajuste
    private static final int RPM_STEP = 100;
    private static final int MAX_RPM = 6000;
    private static final int MIN_RPM = 0;

    private static final int HOOD_STEP = 2;
    private static final int MAX_ANGLE = 160;
    private static final int MIN_ANGLE = 20;

    public ShooterManualControl(ShooterSubsystem shooterSubsystem, Joystick stick) {
        this.shooterSubsystem = shooterSubsystem;
        this.stick = stick;
        addRequirements(shooterSubsystem);
    }

    @Override
    public void execute() {
        int pov = stick.getPOV();

        /* ---- CONTROL RPM (VERTICAL) ---- */
        if (pov == 0) { // Arriba
            shooterRPM += RPM_STEP;
        } else if (pov == 180) { // Abajo
            shooterRPM -= RPM_STEP;
        }

        shooterRPM = Math.max(MIN_RPM, Math.min(MAX_RPM, shooterRPM));
        shooterSubsystem.setAllRPM(shooterRPM);

        /* ---- CONTROL HOOD (HORIZONTAL) ---- */
        if (pov == 90) { // Derecha
            hoodAngle += HOOD_STEP;
        } else if (pov == 270) { // Izquierda
            hoodAngle -= HOOD_STEP;
        }

        hoodAngle = Math.max(MIN_ANGLE, Math.min(MAX_ANGLE, hoodAngle));
        shooterSubsystem.setServos(hoodAngle);
    }

    @Override
    public boolean isFinished() {
        return false; // comando continuo
    }
}