package com.enbecko.objectcreator;

import com.enbecko.objectcreator.TEBaseCreatorBlock.CreatorBlockRayTraceResult;
import com.enbecko.objectcreator.core.LocalCoords;
import com.enbecko.objectcreator.core.vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.enbecko.objectcreator.BlockSetModes.BlockSetMode.BlockSetModeEvent.*;

/**
 * Created by Niclas on 13.11.2016.
 */
public class BlockSetModes {

    public static final int SINGLE = 0, STRAIGHT_LINE = 1;
    private final BlockSetMode SINGLE_MODE = new BlockSetMode(SINGLE), STRAIGHT_LINE_MODE = new BlockSetMode(STRAIGHT_LINE, null, null);

    public BlockSetModes() {

    }

    @Nonnull
    public BlockSetMode getSetMode(int tst) {
        switch (tst) {
            case SINGLE:
                return SINGLE_MODE;
            case STRAIGHT_LINE:
                return STRAIGHT_LINE_MODE;
        }
        return SINGLE_MODE;
    }

    public static class BlockSetMode {
        private final vec3.Int[] positions;
        private final vec3.Int FAIL = new vec3.Int();
        List<BlockSetMode.BlockSetModeEvent> downs = new ArrayList<BlockSetMode.BlockSetModeEvent>();

        public final int mode;

        BlockSetMode(int mode, vec3.Int ... positions) {
            this.positions = positions;
            this.mode = mode;
        }

        public BlockSetMode setHelperGeometries() {
            return this;
        }

        public boolean setResult(int pos, vec3.Int result) {
            if (pos < this.positions.length) {
                this.positions[pos] = result;
                return true;
            }
            return false;
        }

        public void clicked(BlockSetModeEvent event, CreatorBlockRayTraceResult result, long deltaLastClick, LocalCoords coords) {
            System.out.println("clicked "+event);
            switch (event) {
                case KEY_Y:
                    coords.rotation.y += 1;
                    break;
            }
        }

        public void mouseMoved(MouseEvent event, CreatorBlockRayTraceResult result, LocalCoords coords) {
            System.out.println("moved "+event);
        }

        public void released(BlockSetModeEvent event, CreatorBlockRayTraceResult result, LocalCoords coords) {
            System.out.println("released "+event);
        }

        public void tickDowns(LocalCoords coords, BlockSetModeEvent ... downs) {
            if(downs.length > 0)
                System.out.println("ticked downs: "+ Arrays.toString(downs));
            for (BlockSetModeEvent event : downs) {
                switch (event) {
                    case KEY_Y:
                        coords.rotation.y += 1;
                        break;
                }
            }
        }

        @Nullable
        public BlockSetModeEvent dispatchMouseEvent(MouseEvent event, CreatorBlockRayTraceResult result, LocalCoords coords) {
            switch (event.getButton()) {
                case 0:
                    if(event.isButtonstate()) {
                        MOUSE_LEFT.lastClick = System.currentTimeMillis();
                        MOUSE_LEFT.deltaClicked = 0;
                        MOUSE_LEFT.isDown = true;
                        this.clicked(MOUSE_LEFT, result, 0, coords);
                    }
                    else {
                        MOUSE_LEFT.deltaClicked = System.currentTimeMillis() - MOUSE_LEFT.lastClick;
                        MOUSE_LEFT.isDown = false;
                        this.released(MOUSE_LEFT, result, coords);
                    }
                    return MOUSE_LEFT;
                case 1:
                    if(event.isButtonstate()) {
                        MOUSE_RIGHT.lastClick = System.currentTimeMillis();
                        MOUSE_RIGHT.deltaClicked = 0;
                        MOUSE_RIGHT.isDown = true;
                        this.clicked(MOUSE_RIGHT, result, 0, coords);
                    }
                    else {
                        MOUSE_RIGHT.deltaClicked = System.currentTimeMillis() - MOUSE_RIGHT.lastClick;
                        MOUSE_RIGHT.isDown = false;
                        this.released(MOUSE_RIGHT, result, coords);
                    }
                    return MOUSE_RIGHT;
                case 2:
                    if(event.isButtonstate()) {
                        MOUSE_MIDDLE.lastClick = System.currentTimeMillis();
                        MOUSE_MIDDLE.deltaClicked = 0;
                        MOUSE_MIDDLE.isDown = true;
                        this.clicked(MOUSE_MIDDLE, result, 0, coords);
                    }
                    else {
                        MOUSE_MIDDLE.deltaClicked = System.currentTimeMillis() - MOUSE_MIDDLE.lastClick;
                        MOUSE_MIDDLE.isDown = false;
                        this.released(MOUSE_MIDDLE, result, coords);
                    }
                    return MOUSE_MIDDLE;
            }
            return null;
        }

        @Nullable
        public BlockSetModeEvent dispatchKeyEvent(InputEvent.KeyInputEvent event,  CreatorBlockRayTraceResult result, LocalCoords coords) {
            if(ClientProxy.KEY_X.isKeyDown()) {
                if(!KEY_X.isDown) {
                    KEY_X.lastClick = System.currentTimeMillis();
                    KEY_X.deltaClicked = 0;
                    KEY_X.isDown = true;
                    this.clicked(KEY_X, result, 0, coords);
                    return KEY_X;
                }
            } else {
                if(KEY_X.isDown) {
                    KEY_X.isDown = false;
                    KEY_X.deltaClicked = System.currentTimeMillis() - KEY_X.lastClick;
                    this.released(KEY_X, result, coords);
                    return KEY_X;
                }
            }

            if(ClientProxy.KEY_Y.isKeyDown()) {
                if(!KEY_Y.isDown) {
                    KEY_Y.lastClick = System.currentTimeMillis();
                    KEY_Y.deltaClicked = 0;
                    KEY_Y.isDown = true;
                    this.clicked(KEY_Y, result, 0, coords);
                    return KEY_Y;
                }
            } else {
                if(KEY_Y.isDown) {
                    KEY_Y.isDown = false;
                    KEY_Y.deltaClicked = System.currentTimeMillis() - KEY_Y.lastClick;
                    this.released(KEY_Y, result, coords);
                    return KEY_Y;
                }
            }

            if(ClientProxy.KEY_Z.isKeyDown()) {
                if(!KEY_Z.isDown) {
                    KEY_Z.lastClick = System.currentTimeMillis();
                    KEY_Z.deltaClicked = 0;
                    KEY_Z.isDown = true;
                    this.clicked(KEY_Z, result, 0, coords);
                    return KEY_Z;
                }
            } else {
                if(KEY_Z.isDown) {
                    KEY_Z.isDown = false;
                    KEY_Z.deltaClicked = System.currentTimeMillis() - KEY_Z.lastClick;
                    this.released(KEY_Z, result, coords);
                    return KEY_Z;
                }
            }
            return null;
        }

        public void shouldTickDowns(LocalCoords coords) {
            this.downs.clear();
            for (BlockSetModeEvent event : BlockSetModeEvent.values()) {
                if(event.isDown)
                    this.downs.add(event);
            }
            this.tickDowns(coords, downs.toArray(new BlockSetModeEvent[this.downs.size()]));
        }

        @Nullable
        public vec3.Int getResult(int pos) {
            if (pos < positions.length)
                return positions[pos];
            return FAIL;
        }

        public boolean isSet(int pos) {
            vec3.Int position;
            position = this.getResult(pos);
            return (position != null && position != FAIL);
        }

        public void setupNew() {
            for (int k = 0; k < this.positions.length; k++)
                this.positions[k] = null;
        }

        public enum BlockSetModeEvent {
            MOUSE_LEFT, MOUSE_RIGHT, MOUSE_MIDDLE, KEY_X, KEY_Y, KEY_Z;

            long lastClick, deltaClicked;
            boolean isDown;

            public long getLastClick() {
                return this.lastClick;
            }

            public long getDeltaClicked() {
                return this.deltaClicked;
            }

            public boolean isDown() {
                return this.isDown;
            }

            public String toString() {
                return "{setModeEvent: "+this.name()+", down:"+this.isDown+", last:"+this.lastClick+", delta:"+this.deltaClicked+"}";
            }
        }
    }
}
