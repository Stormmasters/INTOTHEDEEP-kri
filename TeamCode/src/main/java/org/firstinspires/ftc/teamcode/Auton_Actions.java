package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.acmerobotics.roadrunner.Action;

@Autonomous
public class Auton_Actions extends OpMode {

    public DcMotorEx slide1, slide2;
    public Servo claw;
    private long startTime;
    private static final long SPIN_UP_TIME = 2000; // Wait time in milliseconds (2 seconds)

    // Action classes
    public class SpinUp implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                slide1.setPower(0.8);
                slide2.setPower(0.8);
                startTime = System.currentTimeMillis();  // Capture the start time
                initialized = true;
            }

            // Check if the motor has been running for at least SPIN_UP_TIME
            if (System.currentTimeMillis() - startTime >= SPIN_UP_TIME) {
                return true;  // Action completes after the wait
            }

            return false;  // Keep running the action
        }
    }

    public class Grab implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            claw.setPosition(1.0); // Change to fully grab the object
            return true;  // Action completes instantly
        }
    }

    public class UnGrab implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            claw.setPosition(0.5); // Change to fully release the object
            return true;  // Action completes instantly
        }
    }

    public Action spinUp() {
        return new SpinUp();
    }

    public Action grab() {
        return new Grab();
    }

    public Action unGrab() {
        return new UnGrab();
    }

    @Override
    public void init() {
        // Initialize hardware
        slide1 = hardwareMap.get(DcMotorEx.class, "par0");
        slide2 = hardwareMap.get(DcMotorEx.class, "par1");
        claw = hardwareMap.get(Servo.class, "claw");

        // Initialize any other configurations (like motors, sensors, etc.)
    }

    @Override
    public void loop() {
        // Run the sequence of actions
        Actions.runBlocking(new SequentialAction(
                spinUp(),  // Spin up motor
                grab(),    // Grab the object
                new SleepAction(100),  // Small delay after grabbing
                unGrab()   // Release the object
        ));
    }
}
