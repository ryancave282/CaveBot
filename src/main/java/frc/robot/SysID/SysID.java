package frc.robot.SysID;

import java.lang.Thread.State;

// import edu.wpi.first.units.measure.*;
import edu.wpi.first.units.MutableMeasure;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.MutAngle;
import edu.wpi.first.units.measure.MutAngularVelocity;
import edu.wpi.first.units.measure.MutDistance;
import edu.wpi.first.units.measure.MutLinearVelocity;
import edu.wpi.first.units.measure.MutVoltage;
import edu.wpi.first.units.measure.Velocity;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Config;
import frc.robot.utility.motor.CANMotorEx;
import frc.robot.utility.motor.TalonEx;
import edu.wpi.first.units.VoltageUnit;

public class SysID extends SubsystemBase {
  public enum Measurement {
    ANGLE,
    DISTANCE
  }
  // Find Logged Data using FileZilla

  // // Mutable holder for unit-safe voltage values, persisted to avoid
  // reallocation.
  // private final MutVoltage ff = new MutVoltage(0, 0, VoltageUnit.of(0))

  // private final MutVoltage appliedVoltage = MutableMeasure.mutable(Units.Volts.of(0));
  // // Mutable holder for unit-safe linear distance values, persisted to avoid
  // reallocation.
  // private final MutDistance distance =
  // MutableMeasure.mutable(Units.Inches.of(0));
  // private final MutAngle angle = MutableMeasure.mutable(Units.Radians.of(0));
  // // Mutable holder for unit-safe linear velocity values, persisted to avoid
  // reallocation.
  // private final MutLinearVelocity distanceVelocity =
  // MutableMeasure.mutable(Units.InchesPerSecond.of(0));
  // private final MutAngularVelocity angularVelocity =
  // MutableMeasure.mutable(Units.RadiansPerSecond.of(0));
  // // private final MutAngularVelocity angularVelocity =
  // MutableMeasure.mutable(Units.RPM.of(0));

  private SysIdRoutine routine;
  // private final SafeMotor motor;

  public SysID(CANMotorEx motor, Measurement unit) {

    // routine = new SysIdRoutine(new Config(null, null, null, (state)->{

    // }), 
    //   ()->{
    //     // (log) -> {
    //               log.motor(motor.toString())
    //                   .voltage(
    //                       appliedVoltage.mut_replace(
    //                           motor.getVelocity() * RobotController.getBatteryVoltage(), Units.Volts))
    //                   .angularPosition(angle.mut_replace(motor.getPosition(), Units.Rotations))// Meters Degrees, etc?
    //                   .angularVelocity(
    //                       angularVelocity.mut_replace(motor.getVelocity(), Units.RotationsPerSecond));// Meters Degrees,
                                                                                                      
    //   }
    // );


    // this.motor = motor;
    // switch (unit) {
    //   case ANGLE:
        // routine = new SysIdRoutine(
        //     // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
        //     // new SysIdRoutine.Config(null, null, null, (state) ->
        //     // Logger.recordOutput("Drive/SysIdState", state.toString())),
        //     new SysIdRoutine.Config(null, null, null),
        //     new SysIdRoutine.Mechanism((Voltage volts) -> {
        //       motor.setVoltage(volts.in(Units.Volts));
        //     },
        //         (log) -> {
        //           log.motor(motor.toString())
        //               .voltage(
        //                   appliedVoltage.mut_replace(
        //                       motor.getVelocity() * RobotController.getBatteryVoltage(), Units.Volts))
        //               .angularPosition(angle.mut_replace(motor.getPosition(), Units.Rotations))// Meters Degrees, etc?
        //               .angularVelocity(
        //                   angularVelocity.mut_replace(motor.getVelocity(), Units.RotationsPerSecond));// Meters Degrees,
        //                                                                                               // etc?
        //         },
        //         this));
    //     break;
    //   case DISTANCE:
    //     routine = new SysIdRoutine(
    //         // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
    //         // new SysIdRoutine.Config(null, null, null, (state) ->
    //         // Logger.recordOutput("Drive/SysIdState", state.toString())),
    //         new SysIdRoutine.Config(null, null, null),
    //         new SysIdRoutine.Mechanism((Voltage volts) -> {
    //           motor.setVoltage(volts.in(Units.Volts));
    //         },
    //             (log) -> {
    //               log.motor(motor.toString())
    //                   .voltage(
    //                       appliedVoltage.mut_replace(
    //                           motor.getVelocity() * RobotController.getBatteryVoltage(), Units.Volts))
    //                   .linearPosition(distance.mut_replace(motor.getPosition(), Units.Inches))// Meters Degrees, etc?
    //                   .linearVelocity(
    //                       distanceVelocity.mut_replace(motor.getVelocity(), Units.InchesPerSecond));// Meters Degrees,
    //                                                                                                 // etc?
    //             },
    //             this));
    //     break;
    // }
  }

  // public SysID(SafeCanSparkMax motor, Measurement unit){
  // // this.motor = motor;
  // switch(unit){
  // case ANGLE:
  // routine = new SysIdRoutine(
  // // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
  // // new SysIdRoutine.Config(null, null, null, (state) ->
  // Logger.recordOutput("Drive/SysIdState", state.toString())),
  // new SysIdRoutine.Config(null, null, null),
  // new SysIdRoutine.Mechanism((Voltage volts) -> {
  // motor.setVoltage(volts.in(Units.Volts));
  // },
  // (log)->{
  // log.motor(motor.toString())
  // .voltage(
  // appliedVoltage.mut_replace(
  // motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
  // .angularPosition(angle.mut_replace(motor.getPosition(),
  // Units.Rotations))//Meters Degrees, etc?
  // .angularVelocity(
  // angularVelocity.mut_replace(motor.getVelocity(),
  // Units.RotationsPerSecond));//Meters Degrees, etc?
  // },
  // this)
  // );
  // break;
  // case DISTANCE:
  // routine = new SysIdRoutine(
  // // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
  // // new SysIdRoutine.Config(null, null, null, (state) ->
  // Logger.recordOutput("Drive/SysIdState", state.toString())),
  // new SysIdRoutine.Config(null, null, null),
  // new SysIdRoutine.Mechanism((Voltage volts) -> {
  // motor.setVoltage(volts.in(Units.Volts));
  // },
  // (log)->{
  // log.motor(motor.toString())
  // .voltage(
  // appliedVoltage.mut_replace(
  // motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
  // .linearPosition(distance.mut_replace(motor.getPosition(),
  // Units.Inches))//Meters Degrees, etc?
  // .linearVelocity(
  // distanceVelocity.mut_replace(motor.getVelocity(),
  // Units.InchesPerSecond));//Meters Degrees, etc?
  // },
  // this)
  // );
  // break;
  // }
  // }

  // public SysID(CANMotorEx motor, CANMotorEx motor2, Measurement unit){
  // // this.motor = motor;
  // // this.motor2 = motor2;
  // switch(unit){
  // case ANGLE:
  // routine = new SysIdRoutine(
  // // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
  // // new SysIdRoutine.Config(null, null, null, (state) ->
  // Logger.recordOutput("Drive/SysIdState", state.toString())),
  // new SysIdRoutine.Config(null, null, null),
  // new SysIdRoutine.Mechanism((Voltage volts) -> {
  // motor.setVoltage(volts.in(Units.Volts));
  // },
  // (log)->{
  // log.motor(motor.toString())
  // .voltage(
  // appliedVoltage.mut_replace(
  // motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
  // .angularPosition(angle.mut_replace(motor.getPosition(), Units.Rotations))
  // .angularVelocity(
  // angularVelocity.mut_replace(motor.getVelocity(), Units.RotationsPerSecond));
  // log.motor(motor2.toString())
  // .voltage(
  // appliedVoltage.mut_replace(
  // motor2.getVelocity() * RobotController.getBatteryVoltage(), Units.Volts))
  // .angularPosition(angle.mut_replace(motor2.getPosition(), Units.Rotations))
  // .angularVelocity(
  // angularVelocity.mut_replace(motor2.getVelocity(), Units.RotationsPerSecond));
  // },
  // this)
  // );
  // break;
  // case DISTANCE:
  // routine = new SysIdRoutine(
  // // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
  // // new SysIdRoutine.Config(null, null, null, (state) ->
  // Logger.recordOutput("Drive/SysIdState", state.toString())),
  // new SysIdRoutine.Config(null, null, null),
  // new SysIdRoutine.Mechanism((Voltage volts) -> {
  // motor.setVoltage(volts.in(Units.Volts));
  // motor2.setVoltage(volts.in(Units.Volts));
  // },
  // (log)->{
  // log.motor(motor.toString())
  // .voltage(
  // appliedVoltage.mut_replace(
  // motor.getVelocity() * RobotController.getBatteryVoltage(), Units.Volts))
  // .linearPosition(distance.mut_replace(motor.getPosition(), Units.Inches))
  // .linearVelocity(
  // distanceVelocity.mut_replace(motor.getVelocity(), Units.InchesPerSecond));
  // log.motor(motor2.toString())
  // .voltage(
  // appliedVoltage.mut_replace(
  // motor2.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
  // .linearPosition(distance.mut_replace(motor2.getPosition(), Units.Inches))
  // .linearVelocity(
  // distanceVelocity.mut_replace(motor2.getVelocity(), Units.InchesPerSecond));
  // },
  // this)
  // );
  // break;
  // }
  // }

  // public SysID(SafeCanSparkMax motor, SafeCanSparkMax motor2, Measurement
  // unit){
  // // this.motor = motor;
  // switch(unit){
  // case ANGLE:
  // routine = new SysIdRoutine(
  // // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
  // // new SysIdRoutine.Config(null, null, null, (state) ->
  // Logger.recordOutput("Drive/SysIdState", state.toString())),
  // new SysIdRoutine.Config(null, null, null),
  // new SysIdRoutine.Mechanism((Voltage volts) -> {
  // motor.setVoltage(volts.in(Units.Volts));
  // },
  // (log)->{
  // log.motor(motor.toString())
  // .voltage(
  // appliedVoltage.mut_replace(
  // motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
  // .angularPosition(angle.mut_replace(motor.getPosition(), Units.Rotations))
  // .angularVelocity(
  // angularVelocity.mut_replace(motor.getVelocity(), Units.RotationsPerSecond));
  // log.motor(motor2.toString())
  // .voltage(
  // appliedVoltage.mut_replace(
  // motor2.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
  // .angularPosition(angle.mut_replace(motor2.getPosition(), Units.Rotations))
  // .angularVelocity(
  // angularVelocity.mut_replace(motor2.getVelocity(), Units.RotationsPerSecond));
  // },
  // this)
  // );
  // break;
  // case DISTANCE:
  // routine = new SysIdRoutine(
  // // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
  // // new SysIdRoutine.Config(null, null, null, (state) ->
  // Logger.recordOutput("Drive/SysIdState", state.toString())),
  // new SysIdRoutine.Config(null, null, null),
  // new SysIdRoutine.Mechanism((Voltage volts) -> {
  // motor.setVoltage(volts.in(Units.Volts));
  // },
  // (log)->{
  // log.motor(motor.toString())
  // .voltage(
  // appliedVoltage.mut_replace(
  // motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
  // .linearPosition(distance.mut_replace(motor.getPosition(),
  // Units.Inches))//Meters Degrees, etc?
  // .linearVelocity(
  // distanceVelocity.mut_replace(motor.getVelocity(),
  // Units.InchesPerSecond));//Meters Degrees, etc?
  // log.motor(motor2.toString())
  // .voltage(
  // appliedVoltage.mut_replace(
  // motor2.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
  // .linearPosition(distance.mut_replace(motor2.getPosition(), Units.Inches))
  // .linearVelocity(
  // distanceVelocity.mut_replace(motor2.getVelocity(), Units.InchesPerSecond));
  // },
  // this)
  // );
  // break;
  // }
  // }

  // public SysID(SafeMotor motor, Measurement unit){
  // this.motor = motor;
  // switch(unit){
  // case ANGLE:
  // routine = new SysIdRoutine(
  // // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
  // // new SysIdRoutine.Config(null, null, null, (state) ->
  // Logger.recordOutput("Drive/SysIdState", state.toString())),
  // new SysIdRoutine.Config(null, null, null),
  // new SysIdRoutine.Mechanism((Voltage volts) -> {
  // motor.setVoltage(volts.in(Units.Volts));
  // },
  // (log)->{
  // log.motor(motor.toString())
  // .voltage(
  // appliedVoltage.mut_replace(
  // motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
  // .angularPosition(angle.mut_replace(motor.getPosition(),
  // Units.Rotations))//Meters Degrees, etc?
  // .angularVelocity(
  // angularVelocity.mut_replace(motor.getVelocity(),
  // Units.RotationsPerSecond));//Meters Degrees, etc?
  // },
  // this)
  // );
  // break;
  // case DISTANCE:
  // routine = new SysIdRoutine(
  // // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
  // // new SysIdRoutine.Config(null, null, null, (state) ->
  // Logger.recordOutput("Drive/SysIdState", state.toString())),
  // new SysIdRoutine.Config(null, null, null),
  // new SysIdRoutine.Mechanism((Voltage volts) -> {
  // motor.setVoltage(volts.in(Units.Volts));
  // },
  // (log)->{
  // log.motor(motor.toString())
  // .voltage(
  // appliedVoltage.mut_replace(
  // motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
  // .linearPosition(distance.mut_replace(motor.getPosition(),
  // Units.Inches))//Meters Degrees, etc?
  // .linearVelocity(
  // distanceVelocity.mut_replace(motor.getVelocity(),
  // Units.InchesPerSecond));//Meters Degrees, etc?
  // },
  // this)
  // );
  // break;
  // }
  // }

  @Override
  public void periodic() {
    // dataLog.flush();
  }

  public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
    return routine.quasistatic(direction);
  }

  public Command sysIdDynamic(SysIdRoutine.Direction direction) {
    return routine.dynamic(direction);
  }
  // public Command stop(){
  // return new InstantCommand(()->motor.setPower(0));
  // }
}
