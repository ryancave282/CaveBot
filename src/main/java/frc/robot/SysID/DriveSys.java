// package frc.robot.SysID;

// import static edu.wpi.first.units.Units.Volts;

// import com.ctre.phoenix6.SignalLogger;
// import com.ctre.phoenix6.swerve.SwerveRequest;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
// import frc.robot.subsystems.drive.SwerveDrive;


// //Spectrum
// public class DriveSys {
//     // private Swerve swerve;
//     private final SysIdRoutine SysIdRoutineTranslation;
//     private final SysIdRoutine SysIdRoutineRotation;
//     private final SysIdRoutine SysIdRoutineSteer;
//     private final SysIdRoutine RoutineToApply;

//     private final SwerveRequest.SysIdSwerveTranslation TranslationCharacterization = new SwerveRequest.SysIdSwerveTranslation();
//     private final SwerveRequest.SysIdSwerveRotation RotationCharacterization = new SwerveRequest.SysIdSwerveRotation();
//     private final SwerveRequest.SysIdSwerveSteerGains SteerCharacterization = new SwerveRequest.SysIdSwerveSteerGains();

//     public DriveSys(SwerveDrive swerve) {
//         // this.swerve = swerve;

//         /* Use one of these sysid routines for your particular test */
//         String stateTxt = "state";
//         SysIdRoutineTranslation = new SysIdRoutine(
//                 new SysIdRoutine.Config(
//                         null,
//                         Volts.of(4),
//                         null,
//                         state -> SignalLogger.writeString(stateTxt, state.toString())),
//                 new SysIdRoutine.Mechanism(
//                         volts -> swerve.setControl(
//                                 TranslationCharacterization.withVolts(volts)),
//                         null,
//                         swerve));

//         SysIdRoutineRotation = new SysIdRoutine(
//                 new SysIdRoutine.Config(
//                         null,
//                         Volts.of(4),
//                         null,
//                         state -> SignalLogger.writeString(stateTxt, state.toString())),
//                 new SysIdRoutine.Mechanism(
//                         roationalRate -> swerve.setControl(
//                                 RotationCharacterization.withRotationalRate(
//                                         roationalRate.baseUnitMagnitude())),
//                         // it actual
//                         // rotational rate
//                         null,
//                         swerve));

//         SysIdRoutineSteer = new SysIdRoutine(
//                 new SysIdRoutine.Config(
//                         null,
//                         Volts.of(7),
//                         null,
//                         state -> SignalLogger.writeString(stateTxt, state.toString())),
//                 new SysIdRoutine.Mechanism(
//                         volts -> swerve.setControl(SteerCharacterization.withVolts(volts)),
//                         null,
//                         swerve));

//         /* Change this to the sysid routine you want to test */
//         RoutineToApply = SysIdRoutineTranslation;
//     }

//     /*
//      * Both the sysid commands are specific to one particular sysid routine, change
//      * which one you're trying to characterize
//      */
//     public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
//         return RoutineToApply.quasistatic(direction);
//     }

//     public Command sysIdDynamic(SysIdRoutine.Direction direction) {
//         return RoutineToApply.dynamic(direction);
//     }
// }
