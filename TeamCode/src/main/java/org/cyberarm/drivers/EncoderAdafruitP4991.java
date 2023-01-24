package org.cyberarm.drivers;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.TimestampedData;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.timecrafters.testing.engine.AdafruitIMU;

import java.nio.ByteBuffer;
import java.util.Arrays;

@I2cDeviceType
@DeviceProperties(name = "Adafruit P4991 Rotary Encoder QT", xmlTag = "Adafruit_Encoder_P4991")
public class EncoderAdafruitP4991 extends I2cDeviceSynchDevice<I2cDeviceSynch> {
    final Object i2cLock = new Object();
    I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create7bit(0x36);
    private int operation = Register.SEESAW_ENCODER_BASE.bVal;

    public enum Register {
        SEESAW_ENCODER_BASE(0x11),

        SEESAW_ENCODER_STATUS(0x00),
        SEESAW_ENCODER_INTEN_SET(0x10),
        SEESAW_ENCODER_INTEN_CLR(0x20),
        SEESAW_ENCODER_POSITION(0x30),
        SEESAW_ENCODER_DELTA(0x40);

        public final int bVal;

        Register(int bVal)
        {
            this.bVal = bVal;
        }
    }

    public EncoderAdafruitP4991(I2cDeviceSynch i2cDeviceSynch) {
        // SEE: https://learn.adafruit.com/adafruit-seesaw-atsamd09-breakout?view=all#i2c-transactions-2937115

        super(i2cDeviceSynch, true);

        super.registerArmingStateCallback(false);
        this.deviceClient.engage();
    }

    @Override
    protected boolean doInitialize() {
        deviceClient.setI2cAddress(ADDRESS_I2C_DEFAULT);

        return deviceClient.isArmed();
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Adafruit;
    }

    @Override
    public String getDeviceName() {
        return "Adafruit P4991 Rotary Encoder QT";
    }

    public synchronized int getCurrentPosition() {
//            ByteBuffer buf = ByteBuffer.allocate(4);
//            buf.putShort((short) Register.SEESAW_ENCODER_BASE.bVal);
//            buf.putShort((short) Register.SEESAW_ENCODER_POSITION.bVal);

        deviceClient.write8(0x11);
        deviceClient.write8(0x30);

        delay(100);

        RobotLog.vv("Encoder", Arrays.toString(deviceClient.read(4)));
        return 0;
//            TimestampedData data = deviceClient.readTimeStamped(4);
//            RobotLog.vv("ENCODER", "Operation: " + operation + ", Timestamp: " + data.nanoTime + ", Data: " + Arrays.toString(data.data));
//            return TypeConversion.byteArrayToInt(data.data);
    }

    private void delay(long ms) {
        try {
            wait(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}