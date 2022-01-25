package org.cyberarm.engine.V2;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Field;
import java.util.HashMap;

public class GamepadChecker {
  private final CyberarmEngine engine;
  private final Gamepad gamepad;
  private final HashMap<String, Boolean> buttons = new HashMap<>();

  public GamepadChecker(CyberarmEngine engine, Gamepad gamepad) {
    this.engine = engine;
    this.gamepad = gamepad;

    buttons.put("a", false);
    buttons.put("b", false);
    buttons.put("x", false);
    buttons.put("y", false);

    buttons.put("start", false);
    buttons.put("guide", false);
    buttons.put("back", false);

    buttons.put("left_bumper", false);
    buttons.put("right_bumper", false);

    buttons.put("left_stick_button", false);
    buttons.put("right_stick_button", false);

    buttons.put("dpad_left", false);
    buttons.put("dpad_right", false);
    buttons.put("dpad_up", false);
    buttons.put("dpad_down", false);
  }

  public void update() {
    for (String btn : buttons.keySet()) {
      try {
        Field field = gamepad.getClass().getDeclaredField(btn);

        if (field.getBoolean(gamepad)) {
          if (!buttons.get(btn)) {
            engine.buttonDown(gamepad, btn);
          }

          buttons.put(btn, true);
        } else {
          if (buttons.get(btn)) {
            engine.buttonUp(gamepad, btn);
          }

          buttons.put(btn, false);
        }
      } catch (NoSuchFieldException|IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }
}
