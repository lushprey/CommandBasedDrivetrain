package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterManualControl extends Command {

    private final ShooterSubsystem shooterSubsystem;
    private final Joystick stick;

    // Estados internos
    private int shooterRPM = 2000;
    private double hoodAngle = 90;

    // Constantes de ajuste
    private static final int RPM_STEP = 100;
    private static final int MAX_RPM = 6000;
    private static final int MIN_RPM = 0;

    private static final double HOOD_STEP = 0.2;
    private static final int MAX_ANGLE = 160;
    private static final int MIN_ANGLE = 20;

    /* ===== PID FIJO VELOCIDAD ===== */
    private static final double kP = 0.000300;
    private static final double kI = 0.000001;
    private static final double kD = 0.000105;

    private boolean shooterOn = false;

    public ShooterManualControl(ShooterSubsystem shooterSubsystem, Joystick stick) {
        this.shooterSubsystem = shooterSubsystem;
        this.stick = stick;
        addRequirements(shooterSubsystem);
        SmartDashboard.putNumber("Shooter kP", kP);
        SmartDashboard.putNumber("Shooter kI", kI);
        SmartDashboard.putNumber("Shooter kD", kD);
        // Aplicar PID
        shooterSubsystem.setPID(kP, kI, kD);
    }

    @Override
    public void execute() {
        //Actualizar PID
        double kP = SmartDashboard.getNumber("Shooter kP", 0.0);
        double kI = SmartDashboard.getNumber("Shooter kI", 0.0);
        double kD = SmartDashboard.getNumber("Shooter kD", 0.0);
        shooterSubsystem.setPID(kP, kI, kD);

        int pov = stick.getPOV();
        boolean buttA = stick.getRawButtonPressed(1);

        /* ---- CONTROL RPM (VERTICAL) ---- */
        if (pov == 0) { // Arriba
            shooterRPM += RPM_STEP;
        } else if (pov == 180) { // Abajo
            shooterRPM -= RPM_STEP;
        }
        if (buttA){
            shooterOn = !shooterOn;
        }
        shooterRPM = Math.max(MIN_RPM, Math.min(MAX_RPM, shooterRPM));
        if (shooterOn){
            shooterSubsystem.setAllRPM(shooterRPM);
        }

        /* ---- CONTROL HOOD (HORIZONTAL) ---- */
        if (pov == 90) { // Derecha
            hoodAngle = HOOD_STEP;
        } else if (pov == 270) { // Izquierda
            hoodAngle = 10-HOOD_STEP;
        }

        hoodAngle = Math.max(MIN_ANGLE, Math.min(MAX_ANGLE, hoodAngle));
        shooterSubsystem.setServos(hoodAngle);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}