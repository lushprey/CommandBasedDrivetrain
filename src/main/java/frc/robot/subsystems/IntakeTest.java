package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

  private final PneumaticHub m_pneumaticHub = new PneumaticHub(5);

  private final Compressor m_compressor =
      new Compressor(5, PneumaticsModuleType.REVPH);

  private final DoubleSolenoid m_doubleSolenoid =
      new DoubleSolenoid(
          5,
          PneumaticsModuleType.REVPH,
          0,
          1
      );

  // ✅ Stick en lugar de XboxController
  private final Joystick m_stick = new Joystick(0);

  public Robot() {
    // ✅ Compresor controlado SOLO por pressure switch digital
    m_compressor.enableAnalog(90, 100);
  }

  @Override
  public void robotPeriodic() {

    // ✅ SOLO sensor digital (pin 0 del PH)
    boolean pressureSwitch = m_pneumaticHub.getPressureSwitch();
    double current = m_pneumaticHub.getCompressorCurrent();
    boolean compressorRunning = m_compressor.isEnabled();

    DoubleSolenoid.Value pistonState = m_doubleSolenoid.get();
    String pistonStatus;

    if (pistonState == DoubleSolenoid.Value.kForward) {
      pistonStatus = "EXTENDIDO";
    } else if (pistonState == DoubleSolenoid.Value.kReverse) {
      pistonStatus = "RETRAÍDO";
    } else {
      pistonStatus = "APAGADO";
    }

    System.out.println("=== Sistema Neumático (Digital) ===");
    System.out.println("Pressure Switch (DIO 0): " +
        (pressureSwitch ? "ACTIVADO" : "DESACTIVADO"));
    System.out.println("Corriente del Compresor: " +
        String.format("%.2f", current) + " A");
    System.out.println("Compresor Habilitado: " +
        (compressorRunning ? "SÍ" : "NO"));
    System.out.println("Estado del Pistón: " + pistonStatus);
    System.out.println("==================================\n");
  }

  @Override
  public void teleopInit() {
    m_doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
  }

  @Override
  public void teleopPeriodic() {
    // ✅ Botón 1 del stick
    if (m_stick.getRawButton(1)) {
      m_doubleSolenoid.set(DoubleSolenoid.Value.kForward);
    } else {
      m_doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
  }

  @Override
  public void disabledInit() {
    m_doubleSolenoid.set(DoubleSolenoid.Value.kOff);
  }
}