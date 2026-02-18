package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.subsystems.ShooterSubsystem;

public class ShooterToggleCommand extends Command {

    private final ShooterSubsystem shooterSubsystem;
    private final Joystick stick;

    private int rpm = 2000;
    private double power = 0.5;
    private boolean rpmMode = true;

    // Estados anteriores (para evitar spam)
    private boolean lastBumperState = false;
    private int lastPOV = -1;

    public ShooterToggleCommand(ShooterSubsystem shooterSubsystem, Joystick stick) {
        this.shooterSubsystem = shooterSubsystem;
        this.stick = stick;
        addRequirements(shooterSubsystem);
    }

    @Override
    public void initialize() {
        SmartDashboard.putString("ShooterMode", rpmMode ? "RPM" : "Power");
    }

    @Override
    public void execute() {

        // ===== BUMPERS (toggle modo) =====
        boolean leftBumper  = stick.getRawButton(5);
        boolean rightBumper = stick.getRawButton(6);
        boolean bumperPressed = leftBumper || rightBumper;

        if (bumperPressed && !lastBumperState) {
            rpmMode = !rpmMode;
        }
        lastBumperState = bumperPressed;

        // ===== D-PAD =====
        int pov = stick.getPOV();

        if (pov != lastPOV) {
            if (rpmMode) {
                if (pov == 90)  rpm += 200;   // derecha
                if (pov == 270) rpm -= 200;   // izquierda
            } else {
                if (pov == 90)  power += 0.1;
                if (pov == 270) power -= 0.1;
            }
        }
        lastPOV = pov;

        // ===== LIMITES =====
        rpm = Math.max(0, Math.min(10000, rpm));
        power = Math.max(-1.0, Math.min(1.0, power));

        // ===== SET AL SHOOTER =====
        if (rpmMode) {
            shooterSubsystem.setAllRPM(rpm);
        } else {
            shooterSubsystem.setAllPower(power);
        }

        // ===== DASHBOARD =====
        SmartDashboard.putString("ShooterMode", rpmMode ? "RPM" : "Power");
        SmartDashboard.putNumber("Target RPM", rpm);
        SmartDashboard.putNumber("Power", power);
    }

    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.setAllPower(0);
    }

    @Override
    public boolean isFinished() {
        return stick.getRawButtonPressed(1);
    }
}
