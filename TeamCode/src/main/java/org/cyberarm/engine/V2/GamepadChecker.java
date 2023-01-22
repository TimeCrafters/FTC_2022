package org.cyberarm.engine.V2;

import android.util.Log;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Field;
import java.util.HashMap;

public class GamepadChecker {
  private final String TAG = "GamepadChecker";
  private final CyberarmEngine engine;
  private final Gamepad gamepad;
  private final HashMap<String, Boolean> buttons = new HashMap<>();
  private final HashMap<String, Long> buttonsDebounceInterval = new HashMap<>();
  private final long debounceTime = 20L; // ms

  public GamepadChecker(CyberarmEngine engine, Gamepad gamepad) {
    this.engine = engine;
    this.gamepad = gamepad;

    buttons.put("a", false); buttonsDebounceInterval.put("a", 0L);
    buttons.put("b", false); buttonsDebounceInterval.put("b", 0L);
    buttons.put("x", false); buttonsDebounceInterval.put("x", 0L);
    buttons.put("y", false); buttonsDebounceInterval.put("y", 0L);

    buttons.put("start", false); buttonsDebounceInterval.put("start", 0L);
    buttons.put("guide", false); buttonsDebounceInterval.put("guide", 0L);
    buttons.put("back", false);  buttonsDebounceInterval.put("back", 0L);

    buttons.put("left_bumper", false); buttonsDebounceInterval.put("left_bumper", 0L);
    buttons.put("right_bumper", false);  buttonsDebounceInterval.put("right_bumper", 0L);

    buttons.put("left_stick_button", false); buttonsDebounceInterval.put("left_stick_button", 0L);
    buttons.put("right_stick_button", false);  buttonsDebounceInterval.put("right_stick_button", 0L);

    buttons.put("dpad_left", false); buttonsDebounceInterval.put("dpad_left", 0L);
    buttons.put("dpad_right", false);  buttonsDebounceInterval.put("dpad_right", 0L);
    buttons.put("dpad_up", false); buttonsDebounceInterval.put("dpad_up", 0L);
    buttons.put("dpad_down", false); buttonsDebounceInterval.put("dpad_down", 0L);
  }

  public void update() {
    long milliseconds = System.currentTimeMillis();

    for (String btn : buttons.keySet()) {
      try {
        Field field = gamepad.getClass().getDeclaredField(btn);
        boolean button = field.getBoolean(gamepad);

        if (button != buttons.get(btn) && milliseconds - buttonsDebounceInterval.get(btn) >= debounceTime) {
          if (button) {
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

          buttonsDebounceInterval.put(btn, milliseconds);

        } else if (button == buttons.get(btn)) {
          buttonsDebounceInterval.put(btn, milliseconds);
        }
      } catch (NoSuchFieldException|IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }
}
