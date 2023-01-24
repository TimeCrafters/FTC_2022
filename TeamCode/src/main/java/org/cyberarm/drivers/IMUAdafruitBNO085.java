package org.cyberarm.drivers;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class IMUAdafruitBNO085 extends I2cDeviceSynchDevice<I2cDeviceSynch> implements IMU {

    private final Object i2cLock = new Object();
    private final I2cAddr DEFAULT_I2C_ADDRESS = new I2cAddr(0x4A);
    private enum Register {
        PRODUCT_ID(0x00);

        private final int address;

        Register(int address) {
            this.address = address;
        }
    }


    protected IMUAdafruitBNO085(I2cDeviceSynch i2cDeviceSynch, boolean deviceClientIsOwned) {
        super(i2cDeviceSynch, deviceClientIsOwned);

        this.deviceClient.setI2cAddress(DEFAULT_I2C_ADDRESS);
    }

    @Override
    public boolean initialize(Parameters parameters) {
        return false;
    }

    @Override
    public void resetYaw() {
    }

    @Override
    public YawPitchRollAngles getRobotYawPitchRollAngles() {
        return null;
    }

    @Override
    public Orientation getRobotOrientation(AxesReference reference, AxesOrder order, AngleUnit angleUnit) {
        return null;
    }

    @Override
    public Quaternion getRobotOrientationAsQuaternion() {
        return null;
    }

    @Override
    public AngularVelocity getRobotAngularVelocity(AngleUnit angleUnit) {
        return null;
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Adafruit;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    protected boolean doInitialize() {
        synchronized (i2cLock) {
            deviceClient.read(32);
        }

        return false;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {

    }
}
