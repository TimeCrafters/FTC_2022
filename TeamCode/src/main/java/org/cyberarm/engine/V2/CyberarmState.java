package org.cyberarm.engine.V2;

import android.util.Log;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A State for use with CyberarmEngineV2
 */
public abstract class CyberarmState implements Runnable {

  private volatile boolean isRunning, hasFinished;
  public static String TAG = "PROGRAM.STATE";
  public CyberarmEngine engine = CyberarmEngine.instance;
  public CopyOnWriteArrayList<CyberarmState> children = new CopyOnWriteArrayList<>();
  public long startTime = 0;
  public int insertOffset = 1;

  /**
   * Called when INIT button on Driver Station is pushed
   */
  public void init() {
  }

  /**
   * Called just before start to ensure state is in correct state
   */
  protected void prestart() {
    isRunning = true;
  }

  /**
   * Called when state has begin to run
   */
  public void start() {
  }

  /**
   * Called while State is running
   */
  public abstract void exec();

  /**
   * State's main loop, calls exec() until hasFinished is true
   * DO NO OVERRIDE
   */
  @Override
  public void run() {
    while (!hasFinished) {
      exec();
    }
    isRunning = false;
  }

  /**
   * Place telemetry calls in here instead of inside exec() to have them displayed correctly on the Driver Station
   * (States update thousands of times per second, resulting in missing or weirdly formatted telemetry if telemetry is added in exec())
   */
  public void telemetry() {
  }

  /**
   * Called when Engine is finished
   */
  public void stop() {
  }

  /**
   * Called when GamepadChecker detects that a gamepad button has been pressed
   * @param gamepad Gamepad
   * @param button String
   */
  public void buttonDown(Gamepad gamepad, String button) {
  }


  /**
   * Called when GamepadChecker detects that a gamepad button has been released
   * @param gamepad Gamepad
   * @param button String
   */
  public void buttonUp(Gamepad gamepad, String button) {
  }

  /**
   * Add a state which runs in parallel with this one
   */
  public CyberarmState addParallelState(CyberarmState state) {
    Log.i(TAG, "Adding " + state.getClass() + " to " + this.getClass());
    children.add(state);

    if (isRunning()) {
      state.init();
      engine.runState(state);
      Log.i(TAG, "Started " + state.getClass() + " in " + this.getClass());
    }

    return state;
  }

  /**
   * Add a state to engine which will run after this one finishes
   */
  public CyberarmState addState(CyberarmState state) {
    engine.insertState(this, state);

    return state;
  }

  /**
   * Returns whether or not state has children
   * @return True if state has children, false otherwise
   */
  public boolean hasChildren() {
    return (children.size() > 0);
  }

  /**
   * Have all of the states children finished running themselves?
   * @return Whether or not all children have finished running
   */
  public boolean childrenHaveFinished() {
    return childrenHaveFinished(children);
  }

  /**
   * Have all of the states children finished running themselves?
   * @param kids CopyOnWriteArrayList of children to check for hasFinished()
   * @return Whether or not all children have finished running
   */
  public boolean childrenHaveFinished(CopyOnWriteArrayList<CyberarmState> kids) {
    boolean allDone = true;

      for (CyberarmState state : kids) {
        if (!state.hasFinished) {
          allDone = false;
          break;
        } else {
          if (!state.childrenHaveFinished()) {
            allDone = false;
            break;
          }
        }
      }

    return allDone;
  }

  /**
   *
   * @return The number of milliseconds this state has been running for
   */
  public double runTime() {
    return (System.currentTimeMillis() - startTime);
  }

  /**
   * Set whether state has finished or not
   * @param value boolean
   */
  public void setHasFinished(boolean value) {
    hasFinished = value;
  }

  /**
   *
   * @return Get value of hasFinished
   */
  public boolean getHasFinished() {
    return hasFinished;
  }

  /**
   *
   * @return Get value of isRunning
   */
  public boolean isRunning() {
    return isRunning;
  }

  /**
   *
   * @param timems How long to sleep in milliseconds
   */
  public void sleep(long timems) {
    try {
      Thread.sleep(timems);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   *
   * @param width How many characters wide to be
   * @param percentCompleted Number between 0.0 and 100.0
   * @param bar What character to draw the completion bar with
   * @param padding What character to draw non-completed bar with
   * @return A string
   */
  public String progressBar(int width, double percentCompleted, String bar, String padding) {
    String percentCompletedString = "" + Math.round(percentCompleted) + "%";
    double activeWidth = (width - 2) - percentCompletedString.length();

    String string = "[";
    double completed = (percentCompleted / 100.0) * activeWidth;

    for (int i = 0; i <= ((int) activeWidth); i++) {
      if (i == ((int) activeWidth) / 2) {
        string += percentCompletedString;
      } else {
        if (i <= (int) completed && (int) completed > 0) {
          string += bar;
        } else {
          string += padding;
        }
      }
    }

    string += "]";
    return string;
  }

  /**
   *
   * @param width How many characters wide to be
   * @param percentCompleted Number between 0.0 and 100.0
   * @return A string
   */
  public String progressBar(int width, double percentCompleted) {
    return progressBar(width, percentCompleted, "=", "  ");
  }

    public abstract void exac();
}