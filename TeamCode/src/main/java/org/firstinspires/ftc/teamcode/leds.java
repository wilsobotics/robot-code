/*   MIT License
 *   Copyright (c) [2025] [Base 10 Assets, LLC]
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:

 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.

 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver.LayerHeight;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations;

/*
 * This code creates a "Configurator" UI which exposes a somewhat limited amount of the functionality
 * available to create animations and artboards to users without needing to work with very much code.
 *
 * This file is not meant to serve as example code - unless you're trying to create a telemetry-based
 * UI for the drivers station.
 * For example code that your team can leverage in your autonomous/teleop code to recall artboards
 * check out the GoBildaPrismArtboardExample. Run this code on your robot and use it to create
 * the artboards you'd like to be able to switch between. This means that switching between animations
 * can be very fast and easy, and the work of creating animations (and sending this information over
 * I²C) only has to happen once!
 *
 */

@TeleOp(name="Prism dwahiudwa", group="Linear OpMode")
//@Disabled

public class leds extends LinearOpMode {

    GoBildaPrismDriver prism;


    @Override
    public void runOpMode() {
        prism = hardwareMap.get(GoBildaPrismDriver.class,"prism");
        telemetry.addData("", prism.getNumberOfLEDs());
        if (gamepad1.xWasPressed())
        {
            PrismAnimations.Solid ready = configureSolid(0, 23, 0, 255, 0);
            prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, ready);
        }
        if (true)
        {
            PrismAnimations.Solid ready = configureSolid(0, 23, 0, 255, 0);
            prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, ready);
        }
        // look it isn't that difficult literally just repeates
        // don't know how to integrate w. subsystems as i don't know how to add it as a hardware component
        // lwk not that deep though just do it in the teleop probably. export as function then call from there
    }

    private PrismAnimations.Solid configureSolid(int start, int stop, int red, int green, int blue) {
        PrismAnimations.Solid solid = new PrismAnimations.Solid();
        solid.setStartIndex(start);
        solid.setPrimaryColor(red, green, blue);
        solid.setStopIndex(stop);
        solid.setBrightness(100);
        return solid;
    }


}