package frc.robot.subsystems;

import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Choreo / LTV
import edu.wpi.first.math.controller.LTVUnicycleController;
import choreo.trajectory.DifferentialSample;

public class DrivetrainSubsystem extends SubsystemBase {

    // =========================
    // CONSTANTES (AJUSTA A TU ROBOT)
    // =========================
    private static final double TRACK_WIDTH_METERS = 0.60;       // distancia entre ruedas
    private static final double WHEEL_DIAMETER_METERS = 0.1524;  // 6 pulgadas
    private static final double GEAR_RATIO = 10.71;              // de motores del drivetrain
    private static final double MAX_SPEED = 3.0;                // m/s

    // =========================
    // MOTORES
    // =========================
    private final SparkMax rightFront = new SparkMax(99, MotorType.kBrushless);
    private final SparkMax rightBack  = new SparkMax(98, MotorType.kBrushless);
    private final SparkMax leftFront  = new SparkMax(97, MotorType.kBrushless);
    private final SparkMax leftBack   = new SparkMax(96, MotorType.kBrushless);

    private final DifferentialDrive drive =
        new DifferentialDrive(leftFront, rightFront);

    // =========================
    // ENCODERS
    // =========================
    private final RelativeEncoder leftEncoder;
    private final RelativeEncoder rightEncoder;

    // =========================
    // KINEMATICS / ODOMETRY
    // =========================
    private final DifferentialDriveKinematics kinematics =
        new DifferentialDriveKinematics(TRACK_WIDTH_METERS);

    private final DifferentialDriveOdometry odometry;

    // Dead reckoning heading
    private double estimatedHeadingRad = 0.0;
    private double lastLeftMeters = 0.0;
    private double lastRightMeters = 0.0;

    // =========================
    // CONTROLLER (CHOREO)
    // =========================
    private final LTVUnicycleController controller =
        new LTVUnicycleController(0.02);

    // =========================
    // CONSTRUCTOR
    // =========================
    public DrivetrainSubsystem() {

        // Conversión encoder → metros (2026)
        double metersPerRotation =
            Math.PI * WHEEL_DIAMETER_METERS / GEAR_RATIO;

        // -------- RIGHT FRONT --------
        SparkMaxConfig rightFrontConfig = new SparkMaxConfig();
        rightFrontConfig.idleMode(IdleMode.kBrake);
        rightFrontConfig.inverted(true); // AJUSTA SI ES NECESARIO
        rightFrontConfig.encoder
            .positionConversionFactor(metersPerRotation)
            .velocityConversionFactor(metersPerRotation / 60.0);

        rightFront.configure(
            rightFrontConfig,
            ResetMode.kResetSafeParameters,
            PersistMode.kPersistParameters
        );

        // -------- RIGHT BACK --------
        SparkMaxConfig rightBackConfig = new SparkMaxConfig();
        rightBackConfig.follow(rightFront, false);
        rightBackConfig.idleMode(IdleMode.kBrake);

        rightBack.configure(
            rightBackConfig,
            ResetMode.kResetSafeParameters,
            PersistMode.kPersistParameters
        );

        // -------- LEFT FRONT --------
        SparkMaxConfig leftFrontConfig = new SparkMaxConfig();
        leftFrontConfig.idleMode(IdleMode.kBrake);
        leftFrontConfig.encoder
            .positionConversionFactor(metersPerRotation)
            .velocityConversionFactor(metersPerRotation / 60.0);

        leftFront.configure(
            leftFrontConfig,
            ResetMode.kResetSafeParameters,
            PersistMode.kPersistParameters
        );

        // -------- LEFT BACK --------
        SparkMaxConfig leftBackConfig = new SparkMaxConfig();
        leftBackConfig.follow(leftFront, false);
        leftBackConfig.idleMode(IdleMode.kBrake);

        leftBack.configure(
            leftBackConfig,
            ResetMode.kResetSafeParameters,
            PersistMode.kPersistParameters
        );

        // =========================
        // ENCODERS
        // =========================
        leftEncoder = leftFront.getEncoder();
        rightEncoder = rightFront.getEncoder();

        leftEncoder.setPosition(0.0);
        rightEncoder.setPosition(0.0);

        // =========================
        // ODOMETRY INIT (2026)
        // =========================
        odometry = new DifferentialDriveOdometry(
            new Rotation2d(0.0),
            0.0,
            0.0
        );
    }

    // =========================
    // PERIODIC (DEAD RECKONING)
    // =========================
    @Override
    public void periodic() {
        double leftMeters = leftEncoder.getPosition();
        double rightMeters = rightEncoder.getPosition();

        double deltaLeft = leftMeters - lastLeftMeters;
        double deltaRight = rightMeters - lastRightMeters;

        // Estimar heading SIN gyro
        estimatedHeadingRad += (deltaRight - deltaLeft) / TRACK_WIDTH_METERS;

        lastLeftMeters = leftMeters;
        lastRightMeters = rightMeters;

        odometry.update(
            new Rotation2d(estimatedHeadingRad),
            leftMeters,
            rightMeters
        );
    }

    // =========================
    // POSE
    // =========================
    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    // =========================
    // TELEOP
    // =========================
    public void arcadeDrive(double forward, double turn) {
        drive.arcadeDrive(forward, turn);
    }

    // =========================
    // TRAJECTORY FOLLOW (CHOREO)
    // =========================
    public void followTrajectory(DifferentialSample sample) {

        Pose2d pose = getPose();
        ChassisSpeeds ff = sample.getChassisSpeeds();

        ChassisSpeeds speeds = controller.calculate(
            pose,
            sample.getPose(),
            ff.vxMetersPerSecond,
            ff.omegaRadiansPerSecond
        );

        DifferentialDriveWheelSpeeds wheelSpeeds =
            kinematics.toWheelSpeeds(speeds);

        driveWheelSpeeds(wheelSpeeds);
    }

    // =========================
    // DRIVE WHEEL SPEEDS
    // =========================
    private void driveWheelSpeeds(DifferentialDriveWheelSpeeds speeds) {
        drive.tankDrive(
            speeds.leftMetersPerSecond / MAX_SPEED,
            speeds.rightMetersPerSecond / MAX_SPEED
        );
    }

    //Reset encoders
    public void resetOdometry(Pose2d pose) {

    // Reset encoders
    leftEncoder.setPosition(0.0);
    rightEncoder.setPosition(0.0);

    lastLeftMeters = 0.0;
    lastRightMeters = 0.0;

    // Reset heading estimado
    estimatedHeadingRad = pose.getRotation().getRadians();

    // Reset odometry
    odometry.resetPosition(
        pose.getRotation(),
        0.0,
        0.0,
        pose
    );
}
}