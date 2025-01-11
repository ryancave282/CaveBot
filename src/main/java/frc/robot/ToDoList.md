TO DO:
- Make and extended Subsystems to have protected instead of private
- Make where the absolute encoders discconnect then the mechanism should just turn off
- When transfertoampmech, make it where the ampmach transfers when shooter stalls out: figure Out how to indicate stall; Shooter Stall
- Limelight Tracking, Aligning, etc
- Implement Safe Hardware Encoders
- Add IsEnabled Button to all!!!!!
- How to convert from rotation to distance and rotations through gears to get rotation
- Make all of the writes that are not necessary during a match be in a practice only writers (Helps prevent the likelihood of loop overruns): Instead of relying on the Driver, maybe make a universal switch to turn it all off
- Test Dpad Resets/Heading Direction of Drivetrain after Auto - Works
- Test Lights
- MAKE ROBOT CONTAINER TRACK CYCLES

Test:

Extra Ideas:
- Lights: Add lights to have the robot tell us any errors with can, etc; Make LEDs show Errors
- SubsystemCheck Command??
- Robot Tracking on Field
- Field Centric Turning
- Make a DroidRagelib - search how to do it

Done:
-

Notes:
- Connecting to Roborio using FileZilla
   Host: sftp://roboRIO-3035-frc.local
   Username:lvuser
   Port:22
   Location of SysID Logs: /home/lvuser/logs 
- Without any PID, the arm should stay upright, basically sayign that the kg is good
- System.out.println("setting command."+ targetPosition.name()); <-- Use this to troubleshoot>
- Connect in Pathplanner with Robot: 10.30.35.2
- Color: 5V - Multi COlor; Strip/12V - Single COlor Strip
- CycleTracker: https://docs.wpilib.org/en/stable/docs/software/telemetry/datalog.html; https://www.chiefdelphi.com/t/roborio-wpilib-logging/159434/17
- Drive Feedforward: kS - Increase until the robot very slowly moves forward (this means too much,so lower) Then kV until the robot goes forward the amount needed
- Constants for Motor
   /** Max RPM of NEO */
   public static final int NEO_MAX_RPM = 5676;
   /** Max RPM of Vortex */
   public static final int VORTEX_MAX_RPM = 6784;
   /** Ticks per revolution of NEO built-in encoder */
   public static final int NEO_ENCODER_TICKS_PER_ROTATION = 42;
   /** Ticks per rotation of Vortex built-in encoder */
   public static final int VORTEX_ENCODER_TICKS_PER_ROTATION = 7168;
   /** Ticks per revolution of REV through bore encoder */
   public static final int REV_ENCODER_TICKS_PER_ROTATION = 8192;


Auto Setup
- Align wheels straight or in direction of robot travel
- 


Wiring
- Kraken has a 12V40A
- Talon FX has a 12V30A
- Drive Kraken has a 12V40A
- Drive NEO has a 12V40A
- CTR Absolute encoder has a 5A mini fuse
- Pigeon has a 5A mini fuse
- Limelight is powered by a 10A mini fuse
- mini power modual powered my a 12V 20A fuse
- ethernet switch is powered by a 10A mini fuse
- Radio power modual is powered by a 10A mini fuse
- RoboRio has a 10A mini fuse
 