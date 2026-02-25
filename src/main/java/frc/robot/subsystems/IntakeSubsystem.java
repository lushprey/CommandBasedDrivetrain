package frc.robot.subsystems;

import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase{

    private final int PneumaticCAN = 1; 

    private final SparkMax intake1 = new SparkMax(58, MotorType.kBrushless);
    private final SparkMax intake2 = new SparkMax(59, MotorType.kBrushless);

    private final Compressor compressor = new Compressor(PneumaticCAN,PneumaticsModuleType.REVPH);
    private final DoubleSolenoid doubleSolenoid =
      new DoubleSolenoid(PneumaticCAN, PneumaticsModuleType.REVPH, 1, 2);

    private boolean pistonExtended = false;
    private boolean intakeState = false;
    
    
    public IntakeSubsystem() {
        // Configurar rightFront en modo brake
        SparkMaxConfig intake1Config = new SparkMaxConfig();
        intake1Config.idleMode(IdleMode.kBrake);
        intake1.configure(intake1Config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // Configurar rightBack en modo brake y seguir a rightFront
        SparkMaxConfig intake2Config = new SparkMaxConfig();
        intake2Config.follow(intake1, false);
        intake2Config.idleMode(IdleMode.kBrake);
        intake2.configure(intake2Config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        compressor.enableAnalog(80, 100);
    };
    

    @Override
    public void periodic(){

    }

    public void pistonToggle(){
        pistonExtended = !pistonExtended;
        if (pistonExtended){
            doubleSolenoid.set(DoubleSolenoid.Value.kForward);
        } else {
            doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public void motorToggle(){
        intakeState = !intakeState;
        if (intakeState){
            intake1.set(0.9);
        } else {
            intake1.set(0);
        }
    }

}
