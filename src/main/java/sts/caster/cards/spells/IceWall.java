package sts.caster.cards.spells;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import sts.caster.actions.FreezeCardAction;
import sts.caster.actions.FreezeSpecificCardAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.makeCardPath;

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
    private static final int BASE_BLOCK = 24;
    private static final int UPG_BLOCK = 4;
    private static final int BASE_BLUR = 1;
    private boolean wasUsed = false;

    public IceWall() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = block = BASE_BLOCK;
        baseMagicNumber = magicNumber = BASE_BLUR;
        exhaust = true;
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new GainBlockAction(p, p, block));
    	addToBot(new ApplyPowerAction(p, p, new BlurPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            upgradeBlock(UPG_BLOCK);
            exhaust = false;
            freezeOnUse = true;
            initializeDescription();
        }
    }
}
