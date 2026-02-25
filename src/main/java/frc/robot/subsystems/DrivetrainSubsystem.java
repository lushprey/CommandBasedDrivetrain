package frc.robot.subsystems;

import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class DrivetrainSubsystem extends SubsystemBase{

    private final SparkMax rightFront = new SparkMax(99, MotorType.kBrushless);
    private final SparkMax rightBack = new SparkMax(98, MotorType.kBrushless);
    private final SparkMax leftFront = new SparkMax(97, MotorType.kBrushless);
    private final SparkMax leftBack = new SparkMax(96, MotorType.kBrushless);
    private final Compressor m_compressor = new Compressor(1, PneumaticsModuleType.REVPH);

    private final DifferentialDrive drive = new DifferentialDrive(rightFront, leftFront);
    public DrivetrainSubsystem() {
        // Configurar rightFront en modo brake
        SparkMaxConfig rightFrontConfig = new SparkMaxConfig();
        rightFrontConfig.idleMode(IdleMode.kBrake);
        rightFront.configure(rightFrontConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // Configurar rightBack en modo brake y seguir a rightFront
        SparkMaxConfig rightBackConfig = new SparkMaxConfig();
        rightBackConfig.follow(rightFront, false);
        rightBackConfig.idleMode(IdleMode.kBrake);
        rightBack.configure(rightBackConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);  
        
        // Configurar leftFront en modo brake
        SparkMaxConfig leftFrontConfig = new SparkMaxConfig();
        leftFrontConfig.idleMode(IdleMode.kBrake);
        leftFront.configure(leftFrontConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // Configurar leftBack en modo brake y seguir a leftFront
        SparkMaxConfig leftBackConfig = new SparkMaxConfig();
        leftBackConfig.follow(leftFront, false);
        leftBackConfig.idleMode(IdleMode.kBrake);
        leftBack.configure(leftBackConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    };
    

    @Override
    public void periodic(){

    }

    public void arcadeDrive(double turn,double forward){
        drive.arcadeDrive(turn, forward);
    }
}
