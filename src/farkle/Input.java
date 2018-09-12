package farkle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * This class is an input listener for the keyboard.
 * <p>
 * This class allows the user to manually check the input at specific times,
 * rather than having to set up action listeners for key pressed and manage which are pressed themselves.
 *
 * @author Joshua DeMoss
 */
public class Input {

    private static final ArrayList<Integer> keys;    // Integer representing the Keyboard ID of a key
    private static final ArrayList<Boolean> keyPressed;    // true is down, false is up This is always current
    private static final ArrayList<Boolean> keyLastState;    // true is down, false is up This is what it last was when it was checked

    public static boolean isKeyPressed(int key) {    // This returns true if the key was pressed. Further calls will return false until the key is Released
        if (keys.contains(key)) {    // If the key is in the list
            int pos = keys.indexOf(key);
            if (keyPressed.get(pos) && !keyLastState.get(pos)) {    // If the current state is Down, but it previously was Up, we know that it was Pressed
                keyLastState.set(pos, true);    //This updates the state, ensuring that its previous state is Down as well, preventing further calls from returning true
                return true;
            }
        }
        return false;    //If it was not in the list, or it was not just pressed, return false
    }

    public static boolean isKeyReleased(int key) {    //This returns true if the key was released. Further calls will return false until the key is Pressed
        if (keys.contains(key)) {
            int pos = keys.indexOf(key);
            if (!keyPressed.get(pos) && keyLastState.get(pos)) {    // If the current state is Up, but it previously was Down, we know that it was Released
                keyLastState.set(pos, false);    //This updates the state, ensuring that its previous state is Up as well, preventing further calls from returning true
                return true;
            }
        }
        return false;    //If it was not in the list, or it was not just released, return false
    }

    public static boolean isKeyDown(int key) {    //This checks if the key is down at any moment. Always returns if the key is down, regardless of previous calls
        if (keys.contains(key)) {    // If the key is in the list, we just need to return the current state
            return keyPressed.get(keys.indexOf(key));
        }
        else {    // If the key is not in the list, add it
            keys.add(key);    //add the entry
            keyPressed.add(false);
            keyLastState.add(false);
            return false;    //return false, as if it was ever pressed, it would be in the list
        }
    }

    private static void setKey(int keyCode, boolean up) {    //Internal method to set the state of a key
        int pos = keys.indexOf(keyCode);    // Get the index of the Key
        keyLastState.set(pos, keyPressed.get(pos));    // Set the previous state to the current one
        keyPressed.set(pos, up);    // update the current state
    }

    static {    // Static blocks are called when the program is run. This "constructs" the class resources
        keys = new ArrayList<>(32);    //This sets up the ArrayLists with a default of 32 keys
        keyPressed = new ArrayList<>(32);
        keyLastState = new ArrayList<>(32);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {    //This sets up the listener, which captures all events and stores them in a more useful manner
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                switch (e.getID()) {    // Switch for if it was pressed or released
                    case KeyEvent.KEY_PRESSED:    // This sets up the entry in the arrays for each key if it is pressed and not already in the array, if it is then it updates the state
                        if (keys.contains(e.getKeyCode())) {    // Updates current state
                            setKey(e.getKeyCode(), true);
                        }
                        else {    // Sets up entry
                            keys.add(e.getKeyCode());
                            keyPressed.add(true);
                            keyLastState.add(false);
                        }
                        break;
                    case KeyEvent.KEY_RELEASED:    // If the key was released, this method already captures that it was pressed, so it is always set up
                        if (keys.contains(e.getKeyCode())) {    // Updates current state
                            setKey(e.getKeyCode(), false);
                        }
                        else {    // Sets up entry
                            keys.add(e.getKeyCode());
                            keyPressed.add(true);
                            keyLastState.add(false);
                        }
                        break;
                }
                return false;
            }
        });
    }
}