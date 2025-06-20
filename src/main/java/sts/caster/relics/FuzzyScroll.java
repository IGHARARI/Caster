package sts.caster.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.PowerTip;
import sts.caster.cards.skills.RuneMagic;
import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

public class FuzzyScroll extends CustomRelic {

    private static AbstractCard currentlyMemorizedCard;
    private boolean cardSelected = true;
    private boolean isStartOfgame = true;


    // ID, images, text.
    public static final String ID = CasterMod.makeID("FuzzyScroll");
    private static final Texture IMG = TextureHelper.getTexture(CasterMod.makeRelicPath("magicbook.png"));
    private static final Texture OUTLINE = TextureHelper.getTexture(CasterMod.makeRelicOutlinePath("magicbook_border.png"));

    public FuzzyScroll() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        addToBot(new MakeTempCardInHandAction(new RuneMagic()));
    }

    @Override
    public void onEquip() {
    }

    @Override
    public void onUnequip() {
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
