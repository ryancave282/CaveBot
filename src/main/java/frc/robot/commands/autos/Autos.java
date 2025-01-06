package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;

public final class Autos {
    public static Command testVision(SwerveDrive drive, Vision vision) {//Top Red/Bottom Blue
        return new SequentialCommandGroup(
            PathPlannerFollow.create(drive, "onePlusFour")
                .setMaxVelocity(6)
                .setAcceleration(6)
                .build()
        );

    }
    private Autos () {}
}