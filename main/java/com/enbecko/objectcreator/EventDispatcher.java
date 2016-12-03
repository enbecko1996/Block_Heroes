package com.enbecko.objectcreator;

import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niclas on 14.11.2016.
 */
public class EventDispatcher {
    private final List<IMouseEventListener> mouseListeners = new ArrayList<IMouseEventListener>();
    private final List<IKeyListener> keyListeners = new ArrayList<IKeyListener>();
    private static EventDispatcher theEventDispatcher;

    private EventDispatcher() {

    }

    public void addMouseListener(@Nonnull IMouseEventListener listener) {
        if(listener != null && !mouseListeners.contains(listener)) {
            mouseListeners.add(listener);
        }
    }

    public void addKeyListener(@Nonnull IKeyListener listener) {
        if(listener != null && !keyListeners.contains(listener)) {
            keyListeners.add(listener);
        }
    }

    public void removeMouseListener(@Nonnull IMouseEventListener listener) {
        if(listener != null && mouseListeners.contains(listener)) {
            mouseListeners.remove(listener);
        }
    }

    public void removeKeyListener(@Nonnull IKeyListener listener) {
        if(listener != null && keyListeners.contains(listener)) {
            keyListeners.remove(listener);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onMouseEvent(MouseEvent event) {
        for (IMouseEventListener listener : mouseListeners) {
            listener.onMouseEvent(event);
        }

    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        for (IKeyListener listener : keyListeners) {
            listener.onKeyEvent(event);
        }
    }

    public static EventDispatcher getTheEventDispatcher() {
        if(theEventDispatcher == null)
            theEventDispatcher = new EventDispatcher();
        return theEventDispatcher;
    }
}
