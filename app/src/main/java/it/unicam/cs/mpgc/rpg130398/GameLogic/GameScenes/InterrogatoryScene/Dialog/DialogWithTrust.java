package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene.Dialog;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericDialog;
import it.unicam.cs.mpgc.rpg130398.api.dialog.DialogLoader;
import it.unicam.cs.mpgc.rpg130398.api.dialog.DialogNode;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;

/**
 * Dialog witch uses trust
 *
 * A {@link DialogNode.Connection} can use the attribute name <p>trustDelta to increase or decrease the trust value
 * once used.
 */

public class DialogWithTrust extends GenericDialog implements DialogStateTrust {
    public DialogWithTrust(ArrayList<DialogNode> nodes) {
        super(nodes);
    }
    public DialogWithTrust(@NonNull DialogLoader loader) {
        super(loader);
    }

    float trust;

    @Override
    protected void onChoiceMade(DialogNode.Connection connection) {
        if (connection.attributes() == null)
            return;
        if (!connection.attributes().containsKey("trustDelta"))
            return;
        trust += connection.attributes().get("trustDelta");
    }
    // -- State --
    // DialogStateTrust
    @Override
    public float getTrust() { return trust; }
    @Override
    public void addTrust(float trustDelta) { trust = trust + trustDelta; }
}
