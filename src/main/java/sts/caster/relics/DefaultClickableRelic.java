package sts.caster.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makeRelicOutlinePath;
import static sts.caster.core.CasterMod.makeRelicPath;

public class DefaultClickableRelic extends CustomRelic implements ClickableRelic { // You must implement things you want to use from StSlib
    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     * StSLib for Clickable Relics
     *
     * At the start of each combat, gain 1 strenght (i.e. Varja)
     */

    // ID, images, text.
    public static final String ID = CasterMod.makeID("DefaultClickableRelic");

    private static final Texture IMG = TextureHelper.getTexture(makeRelicPath("default_clickable_relic.png"));
    private static final Texture OUTLINE = TextureHelper.getTexture(makeRelicOutlinePath("default_clickable_relic.png"));

    private boolean usedThisTurn; // You can also have a relic be only usable once per combat. Check out Hubris for more examples, including other StSlib things.

    public DefaultClickableRelic() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK);

        tips.clear();
        tips.add(new PowerTip(name, description));

    }


    @Override
    public void onRightClick() {// On right click
        if (!isObtained || usedThisTurn) {// If it has been used this turn, or the player doesn't actually have the relic (i.e. it's on display in the shop room)
            return; // Don't do anything.
        }
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) { // Only if you're in combat
            usedThisTurn = true; // Set relic as "Used this turn"
            flash(); // Flash
            stopPulse(); // And stop the pulsing animation (which is started in atPreBattle() below)

            addToBot(new TalkAction(true, DESCRIPTIONS[1], 4.0f, 2.0f)); // Player speech bubble saying "YOU ARE MINE!" (See relic strings)
            addToBot(new SFXAction("MONSTER_COLLECTOR_DEBUFF")); // Sound Effect Action of The Collector Nails
            addToBot(new VFXAction( // Visual Effect Action of the nails applies on a random monster's position.
                    new CollectorCurseEffect(AbstractDungeon.getRandomMonster().hb.cX, AbstractDungeon.getRandomMonster().hb.cY), 2.0F));

            addToBot(new EvokeOrbAction(1)); // Evoke your rightmost orb
        }
    }

    public void atTurnStart() {
        usedThisTurn = false;  // Resets the used this turn. You can remove this whole method to use a relic only once per combat rather than per turn.
        beginLongPulse(); // Pulse while the player can click on it.
    }

    @Override
    public void atPreBattle() {
        usedThisTurn = false; // Make sure usedThisTurn is set to false at the start of each combat.
        beginLongPulse();     // Pulse while the player can click on it.
    }

    @Override
    public void onVictory() {
        stopPulse(); // Don't keep pulsing past the victory screen/outside of combat.
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
