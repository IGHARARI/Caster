package sts.caster.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makeRelicOutlinePath;
import static sts.caster.core.CasterMod.makeRelicPath;

public class PlaceholderRelic extends CustomRelic {

    public static final String ID = CasterMod.makeID("PlaceholderRelic");

    private static final Texture IMG = TextureHelper.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureHelper.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    public PlaceholderRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
    }

    @Override
    public void onEquip() {
    }

    @Override
    public void onUnequip() {
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
