package sts.caster.cards.skills;

import basemod.helpers.CardModifierManager;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import sts.caster.cards.CasterCard;
import sts.caster.cards.mods.FrozenCardMod;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makeCardPath;
import static sts.caster.core.CasterMod.makeVFXPath;

public class IceWall extends CasterCard {

    public static final String ID = CasterMod.makeID("IceWall");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("icewall.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int BASE_BLOCK = 22;
    private static final int UPG_BLOCK = 3;
    private static final int BASE_RETAIN_BLOCK = 7;
    private static final int UPG_RETAIN = 3;

    public IceWall() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = block = BASE_BLOCK;
        baseMagicNumber = magicNumber = BASE_RETAIN_BLOCK;
        exhaust = true;
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        Texture iceSpike = TextureHelper.getTexture(makeVFXPath("icespike.png"));

        AbstractGameEffect wallVfx = new VfxBuilder((TextureAtlas.AtlasRegion) null, p.drawX, p.drawY, .8f)
                .moveX(p.drawX + 70f * Settings.scale, p.drawX + 400f * Settings.scale, VfxBuilder.Interpolations.LINEAR)
                .moveY(p.drawY - 90f * Settings.scale, p.drawY + 20f * Settings.scale, VfxBuilder.Interpolations.LINEAR)
                .emitEvery(
                        (x, y) -> {
                            return new VfxBuilder(iceSpike, x, y, 1f)
                                    .setScale(0f)
                                    .scale(0, 1f, VfxBuilder.Interpolations.EXP10)
                                    .moveY(y, y + iceSpike.getHeight() / 2, VfxBuilder.Interpolations.EXP10)
                                    .playSoundAt(0.1f, .5f, "TINGSHA")
                                    .build();
                        },
                        0.2f
                )
                .build();


        addToBot(new VFXAction(wallVfx, 0.6f));
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        CardModifierManager.addModifier(this, new FrozenCardMod());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            upgradeBlock(UPG_BLOCK);
            upgradeMagicNumber(UPG_RETAIN);
            exhaust = false;
            initializeDescription();
        }
    }
}
