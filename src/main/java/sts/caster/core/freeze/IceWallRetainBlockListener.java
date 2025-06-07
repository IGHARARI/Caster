package sts.caster.core.freeze;

import basemod.helpers.CardModifierManager;
import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts.caster.cards.mods.FrozenCardMod;
import sts.caster.cards.skills.IceWall;

public class IceWallRetainBlockListener implements OnPlayerLoseBlockSubscriber {

    private static final IceWallRetainBlockListener INSTANCE = new IceWallRetainBlockListener();
    private IceWallRetainBlockListener() {}
    public static IceWallRetainBlockListener getInstance() {
        return INSTANCE;
    }

    @Override
    public int receiveOnPlayerLoseBlock(int blockToLose) {
        int blockToRetain = AbstractDungeon.player.hand.group.stream()
                .filter(IceWall.class::isInstance)
                .map(IceWall.class::cast)
                .filter(card -> CardModifierManager.hasModifier(card, FrozenCardMod.ID))
                .mapToInt(card -> card.magicNumber)
                .sum();
        // juuuust in case something goes awry, check to make sure it's positive
        if (blockToRetain > 0) {
            return blockToLose - (blockToRetain);
        }
        return blockToLose;
    }
}
