package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.ThawCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;

import static sts.caster.core.CasterMod.makeCardPath;

public class Shy extends CasterCard {

    public static final String ID = CasterMod.makeID("Shy");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("sultry.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = -2;
    private static final int THAW_AMOUNT = 1;
    private static final int BLOCK_AMOUNT = 5;
    private static final int THAW_UPGRADE = 1;
    private static final int DRAW_AMOUNT = 1;

    public Shy() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = THAW_AMOUNT;
        m2 = baseM2 = DRAW_AMOUNT;
        block = baseBlock = BLOCK_AMOUNT;

        setCardElement(MagicElement.FIRE);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new ThawCardAction(magicNumber, false, upgraded, this.name));
    }

    public void triggerWhenDrawn() {
        if (upgraded) addToBot(new DrawCardAction(m2));
        addToBot(new ThawCardAction(magicNumber, false, this.name));
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
        addToBot(new DiscardSpecificCardAction(this));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            upgradeMagicNumber(THAW_UPGRADE);
            initializeDescription();
        }
    }
}
