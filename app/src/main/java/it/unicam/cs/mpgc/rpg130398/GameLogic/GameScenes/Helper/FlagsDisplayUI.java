package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericTextObject;
import it.unicam.cs.mpgc.rpg130398.api.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows the list of flags collected so far in the bottom-left corner of the
 * screen (coordinate space: x in [0,16], y in [0,9]), one row per flag.
 * <p>
 * Rows stack upward as new flags are discovered (the first flag discovered
 * sits at the bottom row, the most recent one is the highest row).
 * <p>
 * Graphics objects are only (re)created when the number of collected flags
 * changes, not on every update call.
 */
public class FlagsDisplayUI {

    private static final float ROW_HEIGHT = 0.6f;
    private static final float BOTTOM_Y = 3.5f;
    private static final float LEFT_X = 0.6f;
    private static final float ICON_OFFSET_X = -0.3f; // space reserved for the flag name before the icon
    private static final float TEXT_SIZE = 1.5f;

    private record FlagRow(RendableText label, RendableObject icon) {}

    private final GraphicsManager graphic;
    private final ModelLoader flagIconModel;

    private final List<FlagRow> rows = new ArrayList<>();
    private int lastKnownFlagCount = -1; // -1 forces the first build

    public FlagsDisplayUI(GraphicsManager graphic) {
        this.graphic = graphic;

        flagIconModel = new PLY_ModelLoader(new float[] {-1,1,1});
        flagIconModel.setPath("models/UIFlag.ply");
        try {
            flagIconModel.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Refreshes the displayed rows if the number of collected flags changed
     * since the last call. Cheap to call every frame.
     */
    public void update(List<String> collectedFlagsInDiscoveryOrder) {
        if (collectedFlagsInDiscoveryOrder.size() == lastKnownFlagCount)
            return;

        rebuildRows(collectedFlagsInDiscoveryOrder);
        lastKnownFlagCount = collectedFlagsInDiscoveryOrder.size();
    }

    private void rebuildRows(List<String> flags) {
        removeRows();

        for (int i = 0; i < flags.size(); i++) {
            float y = BOTTOM_Y + ROW_HEIGHT * i;

            RendableText label = new GenericTextObject();
            label.setSize(TEXT_SIZE);
            label.setPosition(new float[]{LEFT_X, y, 0});
            label.setColor(Color.GRAY);
            label.setText(flags.get(i));
            graphic.addText(label);

            RendableObject icon = new Generic3DObject(flagIconModel);
            icon.setPosition(new float[]{LEFT_X + ICON_OFFSET_X, y, 0});
            graphic.addObject(icon);

            rows.add(new FlagRow(label, icon));
        }
    }

    public void removeRows() {
        for (FlagRow row : rows) {
            graphic.removeText(row.label());
            graphic.removeObject(row.icon());
        }
        rows.clear();
    }
}