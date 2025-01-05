package frc.robot.utility.encoder;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;
import frc.robot.utility.encoder.EncoderConstants.EncoderDirection;

public class AbsoluteDutyEncoderRIO { 
    protected final DutyCycleEncoder encoder;
    private boolean isInverted;

    // protected double positionOffset=0;
    public ShuffleboardValue<Double> degreeWriter;
    public ShuffleboardValue<Double> radianWriter;
    public ShuffleboardValue<Double> rawWriter;
    public  ShuffleboardValue<Boolean> isConnectedWriter;
    public String name;
                

    private AbsoluteDutyEncoderRIO(DutyCycleEncoder encoder){
        this.encoder=encoder;
        
    }

    public static DirectionBuilder create(int deviceID) {
        AbsoluteDutyEncoderRIO encoder = new AbsoluteDutyEncoderRIO(
            new DutyCycleEncoder(deviceID));
        return encoder.new DirectionBuilder();
    }
    public class DirectionBuilder {
        public OffsetWriter withDirection(EncoderDirection direction) {
            switch (direction) {
                case Reversed:
                    isInverted = true;
                case Forward:
                    isInverted = false;

            }
            return new OffsetWriter();
        }
    }
    public class OffsetWriter {
        public BaseWriter withOffset(double offset) {
            // encoder.setPositionOffset(offset/(2*Math.PI));
            return new BaseWriter();
        }
    }
    public class BaseWriter {
        @SuppressWarnings("unchecked")
        public <T extends AbsoluteDutyEncoderRIO> T withSubsystemBase(String subsystemBaseName) {
            name = subsystemBaseName;
            rawWriter = ShuffleboardValue   
                .create(0.0, name+"/EncoderPos/Raw", name)
                .build();
            degreeWriter = ShuffleboardValue
                .create(0.0, name+"/EncoderPos/Degree", name)
                .build();
            radianWriter = ShuffleboardValue
                .create(0.0, name+"/EncoderPos/Radian", name)
                .build();
            isConnectedWriter = ShuffleboardValue.create
                (true, name+"/EncoderPos/isConnected", name)
                .build();
            return (T) AbsoluteDutyEncoderRIO.this;
        }
    }

    /**
     * Make sure to put this periodic in 
     */
    public void periodic(){
        rawWriter.set(getPosition());//0-1
        degreeWriter.set(getDegrees());//0-360
        radianWriter.set(getRadian());//PI-2*PI
        isConnectedWriter.set(encoder.isConnected());

    }

    
    public double getDegrees() {
        return getPosition()*(360);
    }
    public double getRadian() {
        return getPosition()*(2*Math.PI);
    }

    /**
     * 
     * @return The Absolute Position of the Encoder 
     */
    public double getPosition(){
        // double givenPos = encoder.getAbsolutePosition();

        // if(isInverted){ //To invert values based on direction - Works
        //     givenPos = 1-givenPos;
        // }

        // // givenPos -= encoder.getPositionOffset();
        
        // if(givenPos<0){ //To account for negative values/rollovers - no work
        //     givenPos=1-Math.abs(givenPos); 
        //     //^^ In this case the givePos will be negative; or 1-Math.abs(givenPos)
        // }

        
        // return givenPos-encoder.getPositionOffset();

        return 0;
    }
}